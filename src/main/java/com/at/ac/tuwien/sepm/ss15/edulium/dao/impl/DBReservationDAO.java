package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
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
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * H2 Database Implementation of the ReservationDAO interface
 */
class DBReservationDAO implements DAO<Reservation> {
    private static final Logger LOGGER = LogManager.getLogger(DBReservationDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private DAO<Table> tableDAO;
    @Autowired
    private Validator<Reservation> reservationValidator;
    @Autowired
    private Validator<Table> tableValidator;

    @Override
    public void create(Reservation reservation) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + reservation);

        reservationValidator.validateForCreate(reservation);

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

        generateHistory(reservation);

        createReservationAssociations(reservation);
    }

    @Override
    public void update(Reservation reservation) throws DAOException, ValidationException {
        LOGGER.debug("Entering update with parameters: " + reservation);

        reservationValidator.validateForUpdate(reservation);

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

        generateHistory(reservation);

        updateReservationAssociations(reservation);
    }

    @Override
    public void delete(Reservation reservation) throws DAOException, ValidationException {
        LOGGER.debug("Entering delete with parameters: " + reservation);

        reservationValidator.validateForDelete(reservation);

        final String query = "UPDATE Reservation SET closed = true WHERE ID = ?";

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

        deleteReservationAssociations(reservation);
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
                    "AND duration = ISNULL(?, duration) AND closed = false";

            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
                stmt.setObject(1, reservation.getIdentity());
                stmt.setObject(2, reservation.getName());
                stmt.setObject(3, reservation.getTime() != null ? Timestamp.valueOf(reservation.getTime()) : null);
                stmt.setObject(4, reservation.getQuantity());
                stmt.setObject(5, reservation.getDuration() != null ? reservation.getDuration().toMillis() : null);

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    reservations.add(reservationFromResultSet(result));
                }
            } catch (SQLException e) {
                LOGGER.error("Searching for reservations failed", e);
                throw new DAOException("Searching for reservations failed", e);
            }
        } else { // more complex query with table - ReservationAssoc join required
            // filter out all invalid tables
            final List<Table> tables = reservation.getTables().stream().filter(table -> {
                    try {
                        tableValidator.validateIdentity(table);
                        return true;
                    } catch (ValidationException e) {
                        return false;
                    }
                }).collect(Collectors.toList());

            final String tablePairs = tables.stream().map(t -> "(?, ?)").collect(Collectors.joining(", "));
            final String query = "SELECT * FROM (Reservation INNER JOIN (SELECT reservation_ID FROM ReservationAssoc " +
                    "WHERE (table_section, table_number) IN (" + tablePairs + ") AND disabled = false) " +
                    "ON ID = reservation_ID) WHERE ID = ISNULL(?, ID) AND name = ISNULL(?, name) " +
                    "AND reservationTime = ISNULL(?, reservationTime) AND quantity = ISNULL(?, quantity) " +
                    "AND duration = ISNULL(?, duration) AND closed = false GROUP BY ID";

            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
                int index = 1;

                // fill table pairs
                for (Table table : tables) {
                    stmt.setLong(index++, table.getSection().getIdentity());
                    stmt.setLong(index++, table.getNumber());
                }

                stmt.setObject(index++, reservation.getIdentity());
                stmt.setObject(index++, reservation.getName());
                stmt.setObject(index++, reservation.getTime() != null ? Timestamp.valueOf(reservation.getTime()) : null);
                stmt.setObject(index++, reservation.getQuantity());
                stmt.setObject(index++, reservation.getDuration() != null ? reservation.getDuration().toMillis() : null);

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    reservations.add(reservationFromResultSet(result));
                }
            } catch (SQLException e) {
                LOGGER.error("Searching for reservations failed", e);
                throw new DAOException("Searching for reservations failed", e);
            }
        }

        return reservations;
    }

    @Override
    public List<Reservation> getAll() throws DAOException {
        LOGGER.debug("Entering getAll");

        final String query = "SELECT * FROM Reservation WHERE closed = false";

        final List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                reservations.add(reservationFromResultSet(result));
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

        reservationValidator.validateIdentity(reservation);

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
    }

    /**
     * converts the database query output into a Reservation object
     * @param result database output
     * @return Reservation object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private Reservation reservationFromResultSet(ResultSet result) throws DAOException, SQLException {
        Reservation reservation = new Reservation();
        reservation.setIdentity(result.getLong("ID"));
        reservation.setTime(result.getTimestamp("reservationTime").toLocalDateTime());
        reservation.setName(result.getString("name"));
        reservation.setQuantity(result.getInt("quantity"));
        reservation.setDuration(Duration.ofMillis(result.getLong("duration")));
        reservation.setTables(getTablesForReservation(reservation));
        return reservation;
    }

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the user ocurred
     */
    private History<Reservation> historyFromResultSet(ResultSet result) throws DAOException, SQLException {
        // get user
        List<User> storedUsers = userDAO.find(User.withIdentity(result.getString("changeUser")));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        // create history entry
        History<Reservation> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(reservationFromResultSet(result));

        return historyEntry;
    }

    private void createReservationAssociations(Reservation reservation) throws DAOException {
        LOGGER.debug("Entering createReservationAssociations with parameters: " + reservation);

        final String query = "INSERT INTO ReservationAssoc (reservation_ID, table_section, table_number) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            for (Table table : reservation.getTables()) {
                stmt.setLong(1, reservation.getIdentity());
                stmt.setLong(2, table.getSection().getIdentity());
                stmt.setLong(3, table.getNumber());

                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("Inserting reservation-table associations into database failed", e);
            throw new DAOException("Inserting reservation-table associations into database failed", e);
        }

        //generateHistory(reservation);
    }

    private void updateReservationAssociations(Reservation reservation) throws DAOException {
        LOGGER.debug("Entering updateReservationAssociations with parameters: " + reservation);

        // disable all reservation-table associations for now, the update sql query will re-enable all valid
        // associations
        deleteReservationAssociations(reservation);

        final String query = "MERGE INTO ReservationAssoc (reservation_ID, table_section, table_number, disabled) " +
                "KEY (reservation_ID, table_section, table_number) VALUES (?, ?, ?, false)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
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

        //generateHistory(reservation);
    }

    private void deleteReservationAssociations(Reservation reservation) throws DAOException {
        LOGGER.debug("Entering deleteReservationAssociations with parameters: " + reservation);

        final String query = "UPDATE ReservationAssoc SET disabled = true WHERE reservation_ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, reservation.getIdentity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Deleting reservation-table associations from database failed", e);
            throw new DAOException("Deleting reservation-table associations from database failed", e);
        }

        //generateHistory(reservation);
    }

    private List<Table> getTablesForReservation(Reservation reservation) throws DAOException {
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

                List<Table> storedTables = tableDAO.find(Table.withIdentity(Section.withIdentity(sectionId), tableNumber));
                if (storedTables.size() != 1) {
                    LOGGER.error("table not found");
                    throw new DAOException("table not found");
                }

                tables.add(storedTables.get(0));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for reservation-tables failed", e);
            throw new DAOException("Searching for reservation-tables failed", e);
        }

        return tables;
    }
}
