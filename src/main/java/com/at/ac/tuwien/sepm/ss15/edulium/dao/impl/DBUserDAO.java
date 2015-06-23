package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * H2 Database Implementation of the UserDAO interface
 */
class DBUserDAO implements DAO<User> {
    private static final Logger LOGGER = LogManager.getLogger(DBUserDAO.class);

    @Resource(name = "dataSource")
    private DataSource dataSource;
    @Resource(name = "userValidator")
    private Validator<User> validator;

    @PreAuthorize("isAuthenticated()")
    @Override
    public void create(User user) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + user);

        validator.validateForCreate(user);

        final String query = "INSERT INTO RestaurantUser (ID, name, userRole, tip) VALUES (?, ?, ?, ISNULL(?, 0))";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getIdentity());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getRole());
            stmt.setBigDecimal(4, user.getTip());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Inserting user into database failed", e);
            throw new DAOException("Inserting user into database failed", e);
        }

        generateHistory(user);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public void update(User user) throws DAOException, ValidationException {
        LOGGER.debug("Entering update with parameters: " + user);

        validator.validateForUpdate(user);

        final String query = "UPDATE RestaurantUser SET name = ?, userRole = ?, tip = ISNULL(?, 0)  WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getRole());
            stmt.setBigDecimal(3, user.getTip());
            stmt.setString(4, user.getIdentity());


            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Updating user in database failed, user not found");
                throw new DAOException("Updating user in database failed, user not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Updating user in database failed", e);
            throw new DAOException("Updating user in database failed", e);
        }

        generateHistory(user);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public void delete(User user) throws DAOException, ValidationException {
        LOGGER.debug("Entering delete with parameters: " + user);

        validator.validateForDelete(user);

        final String query = "UPDATE RestaurantUser SET deleted = true WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Deleting user from database failed, user not found");
                throw new DAOException("Deleting user from database failed, user not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Deleting user from database failed", e);
            throw new DAOException("Deleting user from database failed", e);
        }

        generateHistory(user);
    }

    @Override
    public List<User> find(User user) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + user);

        if (user == null) {
            return new ArrayList<>();
        }

        final String query = "SELECT * FROM RestaurantUser WHERE ID = ISNULL(?, ID) AND name = ISNULL(?, name) " +
                             "AND userRole = ISNULL(?, userRole) AND tip = ISNULL(?, tip) AND deleted = false";

        final List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, user.getIdentity());
            stmt.setObject(2, user.getName());
            stmt.setObject(3, user.getRole());
            stmt.setObject(4, user.getTip());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                users.add(userFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for users failed", e);
            throw new DAOException("Searching for users failed", e);
        }

        return users;
    }

    @Override
    public List<User> getAll() throws DAOException {
        LOGGER.debug("Entering getAll");

        final String query = "SELECT * FROM RestaurantUser WHERE deleted = false";

        final List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                users.add(userFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for all users failed", e);
            throw new DAOException("Searching for all users failed", e);
        }

        return users;
    }

    @Override
    public List<History<User>> getHistory(User user) throws DAOException, ValidationException {
        LOGGER.debug("Entering getHistory with parameters: " + user);

        validator.validateIdentity(user);

        final String query = "SELECT * FROM RestaurantUserHistory WHERE ID = ? ORDER BY changeNr";

        List<History<User>> history = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getIdentity());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                history.add(historyFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("retrieving history failed", e);
            throw new DAOException("retrieving history failed", e);
        }

        return history;
    }

    @Override
    public List<User> populate(List<User> users) throws DAOException, ValidationException {
        LOGGER.debug("Entering populate with parameters: " + users);

        if (users == null || users.isEmpty()) {
            return new ArrayList<>();
        }

        for (User user : users) {
            validator.validateIdentity(user);
        }

        final String query = "SELECT * FROM RestaurantUser WHERE ID IN (" +
                users.stream().map(u -> "?").collect(Collectors.joining(", ")) + ")"; // fake a list of identities

        final List<User> populatedUsers = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            int index = 1;

            // fill identity list
            for (User user : users) {
                stmt.setString(index++, user.getIdentity());
            }

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                populatedUsers.add(userFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Populating users failed", e);
            throw new DAOException("Populating users failed", e);
        }

        return populatedUsers;
    }

    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed the changes
     * @param user updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(User user) throws DAOException {
        LOGGER.debug("Entering generateHistory with parameters: " + user);

        final String query = "INSERT INTO RestaurantUserHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM RestaurantUserHistory WHERE ID = ?) " +
                "FROM RestaurantUser WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setString(2, user.getIdentity());          // dataset id
            stmt.setString(3, user.getIdentity());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }

    /**
     * converts the database query output into a user object
     * @param result database output
     * @return User object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private User userFromResultSet(ResultSet result) throws SQLException {
        User user = new User();
        user.setIdentity(result.getString("ID"));
        user.setName(result.getString("name"));
        user.setRole(result.getString("userRole"));
        user.setTip(result.getBigDecimal("tip"));
        return user;
    }

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the user ocurred
     */
    private History<User> historyFromResultSet(ResultSet result) throws DAOException, ValidationException, SQLException {
        // get user
        List<User> storedUsers = populate(Collections.singletonList(User.withIdentity(result.getString("changeUser"))));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        // create history entry
        History<User> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(userFromResultSet(result));

        return historyEntry;
    }
}
