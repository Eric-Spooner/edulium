package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.GenericDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 Database Implementation of the UserDAO interface
 */
class UserDAOImpl implements GenericDAO<User> {
    private static final Logger LOGGER = LogManager.getLogger(UserDAOImpl.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Validator<User> validator;

    @Override
    public void create(User user) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + user);

        validator.validateForCreate(user);

        final String query = "INSERT INTO RestaurantUser (ID, name, userRole) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getIdentity());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getRole());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Inserting user into database failed", e);
            throw new DAOException("Inserting user into database failed", e);
        }

        generateHistory(user);
    }

    @Override
    public void update(User user) throws DAOException, ValidationException {
        LOGGER.debug("Entering update with parameters: " + user);

        validator.validateForUpdate(user);

        final String query = "UPDATE RestaurantUser SET name = ?, userRole = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getRole());
            stmt.setString(3, user.getIdentity());

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
                             "AND userRole = ISNULL(?, userRole) AND deleted = false";

        final List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, user.getIdentity());
            stmt.setObject(2, user.getName());
            stmt.setObject(3, user.getRole());

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

    /**
     * writes the changes of the user dataset into the database
     * stores the time, the number of the change and the user which executed the changes
     * @param user changed object (must have a valid identity)
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(User user) throws DAOException {

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
        return user;
    }
}
