package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
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
            throw new DAOException("Inserting reservatoin into database failed", e);
        }

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

        generateHistory(reservation);
    }

    @Override
    public void delete(Reservation reservation) throws DAOException, ValidationException {
        LOGGER.debug("Entering delete with parameters: " + reservation);

        validator.validateForDelete(reservation);

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
    }

    @Override
    public List<Reservation> find(Reservation reservation) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + reservation);

        if (reservation == null) {
            return new ArrayList<>();
        }

        final String query = "SELECT * FROM Reservation WHERE ID = ISNULL(?, ID) AND name = ISNULL(?, name) " +
                "AND reservationTime = ISNULL(?, reservationTime) AND quantity = ISNULL(?, quantity) " +
                "AND duration = ISNULL(?, duration) AND closed = false";

        final List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, reservation.getIdentity());
            stmt.setObject(2, reservation.getName());
            stmt.setObject(3, Timestamp.valueOf(reservation.getTime()));
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
    private Reservation reservationFromResultSet(ResultSet result) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setIdentity(result.getLong("ID"));
        reservation.setTime(result.getTimestamp("reservationTime").toLocalDateTime());
        reservation.setName(result.getString("name"));
        reservation.setQuantity(result.getInt("quantity"));
        reservation.setDuration(Duration.ofMillis(result.getLong("duration")));
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
}
