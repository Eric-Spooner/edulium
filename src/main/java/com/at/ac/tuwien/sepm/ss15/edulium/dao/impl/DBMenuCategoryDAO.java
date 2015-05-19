package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * H2 Database Implementation of the MenuCategoryDAO interface
 */
class DBMenuCategoryDAO implements DAO<MenuCategory> {
    private static final Logger LOGGER = LogManager.getLogger(DBMenuCategoryDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private Validator<MenuCategory> validator;

    @Override
    public void create(MenuCategory menuCategory) throws DAOException, ValidationException {
        LOGGER.debug("entering create with parameters " + menuCategory);

        validator.validateForCreate(menuCategory);
        final String query = "INSERT INTO MenuCategory (name) VALUES (?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, menuCategory.getName());
            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                menuCategory.setIdentity(key.getLong(1));
            }
            key.close();
        } catch (SQLException e) {
            LOGGER.error("inserting menuCategory into database failed", e);
            throw new DAOException("inserting menuCategory into database failed", e);
        }

        generateHistory(menuCategory);
    }

    @Override
    public void update(MenuCategory menuCategory) throws DAOException, ValidationException {
        LOGGER.debug("entering update with parameters " + menuCategory);

        validator.validateForUpdate(menuCategory);
        final String query = "UPDATE MenuCategory SET name = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, menuCategory.getName());
            stmt.setLong(2, menuCategory.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("updating menuCategory in database failed, dataset not found");
                throw new DAOException("updating menuCategory in database failed, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("updating menuCategory in database failed", e);
            throw new DAOException("updating menuCategory in database failed", e);
        }

        generateHistory(menuCategory);
    }

    @Override
    public void delete(MenuCategory menuCategory) throws DAOException, ValidationException {
        LOGGER.debug("entering delete with parameters " + menuCategory);

        validator.validateForDelete(menuCategory);
        final String query = "UPDATE MenuCategory SET deleted = true WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, menuCategory.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("deleting menuCategory failed, dataset not found");
                throw new DAOException("deleting menuCategory failed, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("deleting menuCategory failed", e);
            throw new DAOException("deleting menuCategory failed", e);
        }

        generateHistory(menuCategory);
    }

    @Override
    public List<MenuCategory> find(MenuCategory menuCategory) throws DAOException {
        LOGGER.debug("entering find with parameters " + menuCategory);

        if (menuCategory == null) {
            return new ArrayList<>();
        }

        String query = "SELECT * FROM MenuCategory WHERE ID = ISNULL(?, ID) " +
                "AND name = ISNULL(?, name) AND deleted = false";

        final List<MenuCategory> objects = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, menuCategory.getIdentity());
            stmt.setObject(2, menuCategory.getName());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                objects.add(parseResult(result));
            }
        } catch (SQLException e) {
            LOGGER.error("searching for categories failed", e);
            throw new DAOException("searching for categories failed", e);
        }

        return objects;
    }

    @Override
    public List<MenuCategory> getAll() throws DAOException {
        LOGGER.debug("entering getAll");

        final String query = "SELECT * FROM MenuCategory WHERE deleted = false";
        final List<MenuCategory> objects = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                objects.add(parseResult(result));
            }
        } catch (SQLException e) {
            LOGGER.error("searching for all categories failed", e);
            throw new DAOException("searching for all categories failed", e);
        }

        return objects;
    }

    @Override
    public List<History<MenuCategory>> getHistory(MenuCategory menuCategory) throws DAOException, ValidationException {
        LOGGER.debug("entering getHistory with parameters " + menuCategory);

        validator.validateIdentity(menuCategory);
        List<History<MenuCategory>> history = new ArrayList<>();
        final String query = "SELECT * FROM MenuCategoryHistory WHERE ID = ? ORDER BY changeNr";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, menuCategory.getIdentity());
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                history.add(parseHistoryEntry(result));
            }
        } catch (SQLException e) {
            LOGGER.error("retrieving history failed", e);
            throw new DAOException("retrieving history failed", e);
        }

        return history;
    }

    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed
     * the changes
     * @param menuCategory updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(MenuCategory menuCategory) throws DAOException {
        LOGGER.debug("entering generateHistory with parameters " + menuCategory);

        final String query = "INSERT INTO MenuCategoryHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM MenuCategoryHistory WHERE ID = ?) " +
                "FROM MenuCategory WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, menuCategory.getIdentity());          // dataset id
            stmt.setLong(3, menuCategory.getIdentity());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }

    /**
     * converts the database query output into a object
     * @param result database output
     * @return MenuCategory object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private MenuCategory parseResult(ResultSet result) throws SQLException {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setIdentity(result.getLong("ID"));
        menuCategory.setName(result.getString("name"));
        return menuCategory;
    }

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the user ocurred
     */
    private History<MenuCategory> parseHistoryEntry(ResultSet result) throws DAOException, ValidationException, SQLException {
        // get user
        List<User> storedUsers = userDAO.populate(Arrays.asList(User.withIdentity(result.getString("changeUser"))));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        // create history entry
        History<MenuCategory> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(parseResult(result));

        return historyEntry;
    }
}
