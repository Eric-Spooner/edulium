package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.ReservationDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * H2 Database Implementation of the ReservationDAO interface
 */
@PreAuthorize("isAuthenticated()")
class DBReservationDAO implements ReservationDAO {
    private static final Logger LOGGER = LogManager.getLogger(DBReservationDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private DAO<Table> tableDAO;
    @Autowired
    private Validator<Reservation> validator;

    @Override
    public void create(Reservation reservation) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + reservation);

        validator.validateForCreate(reservation);

        final String query = "INSERT INTO Reservation (reservationTime, name, quantity, duration) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(reservation.getTime()));
            stmt.setString(2, reservation.getName());
            stmt.setInt(3, reservation.getQuantity());
            stmt.setLong(4, reservation.getDuration().toMillis());

            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                reservation.setIdentity(key.getLong(1));
            }
        } catch (SQLException e) {
            LOGGER.error("Inserting reservation into database failed", e);
            throw new DAOException("Inserting reservation into database failed", e);
        }

        updateReservationAssociations(reservation);
        generateHistory(reservation);
    }

    @Override
    public void update(Reservation reservation) throws DAOException, ValidationException {
        LOGGER.debug("Entering update with parameters: " + reservation);

        validator.validateForUpdate(reservation);

        final String query = "UPDATE Reservation SET reservationTime = ?, name = ?, quantity = ?, duration = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(reservation.getTime()));
            stmt.setString(2, reservation.getName());
            stmt.setInt(3, reservation.getQuantity());
            stmt.setLong(4, reservation.getDuration().toMillis());
            stmt.setLong(5, reservation.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Updating reservation in database failed, reservation not found");
                throw new DAOException("Updating reservation in database failed, reservation not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Updating reservation in database failed", e);
            throw new DAOException("Updating reservation in database failed", e);
        }

        updateReservationAssociations(reservation);
        generateHistory(reservation);
    }

    @Override
    public void delete(Reservation reservation) throws DAOException, ValidationException {
        LOGGER.debug("Entering delete with parameters: " + reservation);

        validator.validateForDelete(reservation);

        final String query = "UPDATE Reservation SET deleted = true WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, reservation.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Deleting reservation from database failed, reservation not found");
                throw new DAOException("Deleting reservation from database failed, reservation not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Deleting reservation from database failed", e);
            throw new DAOException("Deleting reservation from database failed", e);
        }

        generateHistory(reservation);
    }

    @Override
    public List<Reservation> find(Reservation reservation) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + reservation);

        if (reservation == null) {
            return new ArrayList<>();
        }

        final List<Reservation> reservations = new ArrayList<>();

        if (reservation.getTables() == null) {  // query without tables - no ReservationAssoc join needed :)
            final String query = "SELECT * FROM Reservation WHERE ID = ISNULL(?, ID) AND name = ISNULL(?, name) " +
                    "AND reservationTime = ISNULL(?, reservationTime) AND quantity = ISNULL(?, quantity) " +
                    "AND duration = ISNULL(?, duration) AND deleted = false";

            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
                stmt.setObject(1, reservation.getIdentity());
                stmt.setObject(2, reservation.getName());
                stmt.setObject(3, reservation.getTime() != null ? Timestamp.valueOf(reservation.getTime()) : null);
                stmt.setObject(4, reservation.getQuantity());
                stmt.setObject(5, reservation.getDuration() != null ? reservation.getDuration().toMillis() : null);

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    try {
                        reservations.add(reservationFromResultSet(result));
                    } catch (ValidationException e) {
                        LOGGER.warn("parsing the result '" + result + "' failed", e);
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("Searching for reservations failed", e);
                throw new DAOException("Searching for reservations failed", e);
            }
        } else { // more complex query with table - ReservationAssoc join required
            // build a complete set of tables (so that we have the section id and table number for each table)
            final Set<Table> tables = new HashSet<>();
            for (Table table : reservation.getTables()) {
                tables.addAll(tableDAO.find(table));
            }

            // sadly we have to provide our own list of pairs - fake it with a list of question marks in the prepared stmt
            final String tablePairs = tables.stream().map(t -> "(?, ?)").collect(Collectors.joining(", "));

            final String query = "SELECT * FROM Reservation WHERE ID = ISNULL(?, ID) AND name = ISNULL(?, name) " +
                    "AND reservationTime = ISNULL(?, reservationTime) AND quantity = ISNULL(?, quantity) " +
                    "AND duration = ISNULL(?, duration) AND deleted = false AND EXISTS (SELECT 1 FROM ReservationAssoc " +
                    "WHERE reservation_ID = ID AND (table_section, table_number) IN (" + tablePairs + ") AND disabled = false)";

            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
                int index = 1;

                stmt.setObject(index++, reservation.getIdentity());
                stmt.setObject(index++, reservation.getName());
                stmt.setObject(index++, reservation.getTime() != null ? Timestamp.valueOf(reservation.getTime()) : null);
                stmt.setObject(index++, reservation.getQuantity());
                stmt.setObject(index++, reservation.getDuration() != null ? reservation.getDuration().toMillis() : null);

                // fill table pairs
                for (Table table : tables) {
                    stmt.setLong(index++, table.getSection().getIdentity());
                    stmt.setLong(index++, table.getNumber());
                }

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    try {
                        reservations.add(reservationFromResultSet(result));
                    } catch (ValidationException e) {
                        LOGGER.warn("parsing the result '" + result + "' failed", e);
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("Searching for reservations failed", e);
                throw new DAOException("Searching for reservations failed", e);
            }
        }

        return reservations;
    }

    @Override
    public List<Reservation> findBetween(LocalDateTime from, LocalDateTime to) throws DAOException, ValidationException {
        LOGGER.debug("Entering findBetween with parameters " + from + " / " + to);

        if(from == null || to == null) {
            LOGGER.error("parameter 'start' or 'duration' is null");
            throw new ValidationException("parameter 'start' or 'duration' is null");
        }

        final String query = "SELECT * FROM Reservation WHERE deleted = false AND " +
                "reservationTime < ? AND DATEADD('MILLISECOND', duration, reservationTime) > ?";

        final List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(to));
            stmt.setTimestamp(2, Timestamp.valueOf(from));

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                try {
                    reservations.add(reservationFromResultSet(result));
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + result + "' failed", e);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for reservations failed", e);
            throw new DAOException("Searching for reservations failed", e);
        }

        return reservations;
    }

    @Override
    public List<Reservation> getAll() throws DAOException {
        LOGGER.debug("Entering getAll");

        final String query = "SELECT * FROM Reservation WHERE deleted = false";

        final List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                try {
                    reservations.add(reservationFromResultSet(result));
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + result + "' failed", e);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for all reservations failed", e);
            throw new DAOException("Searching for all reservations failed", e);
        }

        return reservations;
    }

    @Override
    public List<History<Reservation>> getHistory(Reservation reservation) throws DAOException, ValidationException {
        LOGGER.debug("Entering getHistory with parameters: " + reservation);

        validator.validateIdentity(reservation);

        final String query = "SELECT * FROM ReservationHistory WHERE ID = ? ORDER BY changeNr";

        List<History<Reservation>> reservations = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, reservation.getIdentity());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                reservations.add(historyFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("retrieving history for reservation failed", e);
            throw new DAOException("retrieving history for reservation failed", e);
        }

        return reservations;
    }

    @Override
    public List<Reservation> populate(List<Reservation> reservations) throws DAOException, ValidationException {
        LOGGER.debug("Entering populate with parameters: " + reservations);

        if (reservations == null || reservations.isEmpty()) {
            return new ArrayList<>();
        }

        for (Reservation reservation : reservations) {
            validator.validateIdentity(reservation);
        }

        final String query = "SELECT * FROM Reservation WHERE ID IN (" +
                reservations.stream().map(u -> "?").collect(Collectors.joining(", ")) + ")"; // fake a list of identities

        final List<Reservation> populatedReservations = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            int index = 1;

            // fill identity list
            for (Reservation reservation : reservations) {
                stmt.setLong(index++, reservation.getIdentity());
            }

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                populatedReservations.add(reservationFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Populating reservations failed", e);
            throw new DAOException("Populating reservations failed", e);
        }

        return populatedReservations;
    }

    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed the changes
     * @param reservation updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(Reservation reservation) throws DAOException {
        LOGGER.debug("Entering generateHistory with parameters: " + reservation);

        final String query = "INSERT INTO ReservationHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM ReservationHistory WHERE ID = ?) " +
                "FROM Reservation WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, reservation.getIdentity()); // dataset id
            stmt.setLong(3, reservation.getIdentity()); // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history for reservation failed", e);
            throw new DAOException("generating history for reservation failed", e);
        }

        // get the latest history change number
        final String queryChangeNr = "SELECT MAX(changeNr) FROM ReservationHistory WHERE ID = ?";

        long changeNr = -1;

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryChangeNr)) {
            stmt.setLong(1, reservation.getIdentity());

            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                changeNr = result.getLong(1);
            } else {
                LOGGER.error("retrieving the change number failed, reservation history not found");
                throw new DAOException("retrieving the change number failed, reservation history not found");
            }
        } catch (SQLException e) {
            LOGGER.error("retrieving the change number failed", e);
            throw new DAOException("retrieving the change number failed", e);
        }

        generateReservationAssociationsHistory(reservation, changeNr);
    }

    /**
     * converts the database query output into a Reservation object
     * @param result database output
     * @return Reservation object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private Reservation parseResult(ResultSet result) throws DAOException, SQLException {
        Reservation reservation = new Reservation();
        reservation.setIdentity(result.getLong("ID"));
        reservation.setTime(result.getTimestamp("reservationTime").toLocalDateTime());
        reservation.setName(result.getString("name"));
        reservation.setQuantity(result.getInt("quantity"));
        reservation.setDuration(Duration.ofMillis(result.getLong("duration")));
        return reservation;
    }

    /**
     * converts the database query output into a Reservation object and fetches all associated tables
     * @param result database output
     * @return Reservation object with the data of the resultSet set with all associated tables
     * @throws SQLException if an error accessing the database occurred
     */
    private Reservation reservationFromResultSet(ResultSet result) throws DAOException, ValidationException, SQLException {
        Reservation reservation = parseResult(result);
        reservation.setTables(getTablesForReservation(reservation));
        return reservation;
    }

    /**
     * converts the database query output into a Reservation object and fetches all associated tables from history table
     * with the given change number
     * @param result database output
     * @return Reservation object with the data of the resultSet set with all associated tables (with the given change number)
     * @throws SQLException if an error accessing the database occurred
     */
    private Reservation reservationHistoryFromResultSet(ResultSet result, long changeNumber) throws DAOException, ValidationException, SQLException {
        Reservation reservation = parseResult(result);
        reservation.setTables(getTablesForReservationHistory(reservation, changeNumber));
        return reservation;
    }

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the user ocurred
     */
    private History<Reservation> historyFromResultSet(ResultSet result) throws DAOException, ValidationException, SQLException {
        // get user
        List<User> storedUsers = userDAO.populate(Arrays.asList(User.withIdentity(result.getString("changeUser"))));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        final long changeNumber = result.getLong("changeNr");

        // create history entry
        History<Reservation> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(changeNumber);
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(reservationHistoryFromResultSet(result, changeNumber));

        return historyEntry;
    }

    /**
     * adds and/or updates all the reservation-table associations for the given reservation
     * @param reservation reservation dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void updateReservationAssociations(Reservation reservation) throws DAOException {
        LOGGER.debug("Entering updateReservationAssociations with parameters: " + reservation);

        // disable all reservation-table associations for now, the update sql query will re-enable all valid
        // associations
        final String queryDisableAssocs = "UPDATE ReservationAssoc SET disabled = true WHERE reservation_ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryDisableAssocs)) {
            stmt.setLong(1, reservation.getIdentity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Disabling reservation-table associations from database failed", e);
            throw new DAOException("Disabling reservation-table associations from database failed", e);
        }

        // add (or re-enable) valid assocations
        final String queryMerge = "MERGE INTO ReservationAssoc (reservation_ID, table_section, table_number, disabled) " +
                "KEY (reservation_ID, table_section, table_number) VALUES (?, ?, ?, false)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryMerge)) {
            for (Table table : reservation.getTables()) {
                stmt.setLong(1, reservation.getIdentity());
                stmt.setLong(2, table.getSection().getIdentity());
                stmt.setLong(3, table.getNumber());

                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("Updating reservation-table associations in database failed", e);
            throw new DAOException("Updating reservation-table associations in database failed", e);
        }
    }

    /**
     * searches all tables from the reservation-table associations for the given reservation
     * @param reservation reservation dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private List<Table> getTablesForReservation(Reservation reservation) throws DAOException, ValidationException {
        LOGGER.debug("Entering getTablesForReservation with parameters: " + reservation);

        final String query = "SELECT table_section, table_number FROM ReservationAssoc WHERE reservation_ID = ? " +
                "AND disabled = false";

        final List<Table> tables = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, reservation.getIdentity());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final Long sectionId = result.getLong("table_section");
                final Long tableNumber = result.getLong("table_number");

                // we populate the list of tables before we return it - so the identity of a table is enough for now
                tables.add(Table.withIdentity(Section.withIdentity(sectionId), tableNumber));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for reservation-tables failed", e);
            throw new DAOException("Searching for reservation-tables failed", e);
        }

        return tableDAO.populate(tables);
    }

    /**
     * searches all tables from the reservation-table associations for the given reservation history with the given changeNumber
     * @param reservation reservation history dataset
     * @param changeNumber reservation history change number
     * @throws DAOException if an error accessing the database occurred
     */
    private List<Table> getTablesForReservationHistory(Reservation reservation, long changeNumber) throws DAOException, ValidationException {
        LOGGER.debug("Entering getTablesForHistoryReservation with parameters: " + reservation);

        final String query = "SELECT table_section, table_number FROM ReservationAssocHistory " +
                "WHERE reservation_ID = ? AND changeNr = ? AND disabled = false";

        final List<Table> tables = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, reservation.getIdentity());
            stmt.setLong(2, changeNumber);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final Long sectionId = result.getLong("table_section");
                final Long tableNumber = result.getLong("table_number");

                // we populate the list of tables before we return it - so the identity of a table is enough for now
                tables.add(Table.withIdentity(Section.withIdentity(sectionId), tableNumber));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for reservation-tables history failed", e);
            throw new DAOException("Searching for reservation-tables history failed", e);
        }

        return tableDAO.populate(tables);
    }

    /**
     * writes the association changes of the dataset into the database
     * stores the time; number of the change (uses the given changeNumber) and the user which executed the changes
     * @param reservation updated dataset
     * @param changeNumber reservation history change number
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateReservationAssociationsHistory(Reservation reservation, long changeNumber) throws DAOException {
        LOGGER.debug("Entering generateReservationAssociationsHistory with parameters: " + reservation);

        final String query = "INSERT INTO ReservationAssocHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, ? FROM ReservationAssoc WHERE reservation_ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, changeNumber);
            stmt.setLong(3, reservation.getIdentity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history for reservation-table associations failed", e);
            throw new DAOException("generating history for reservation-table associations failed", e);
        }
    }
}
