package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
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
import java.util.ArrayList;
import java.util.List;

/**
 * H2 Database Implementation of the section interface
 */
@PreAuthorize("isAuthenticated()")
class DBSectionDAO implements DAO<Section> {
    private static final Logger LOGGER = LogManager.getLogger(DBSectionDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private Validator<Section> validator;

    /**
     * writes the object into the database and sets the identity parameter of
     * section
     *
     * @param section object to store
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public void create(Section section) throws DAOException, ValidationException {
        LOGGER.debug("entering create with parameters " + section);

        validator.validateForCreate(section);

        final String query = "INSERT INTO RestaurantSection (name) VALUES (?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, section.getName());
            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                section.setIdentity(key.getLong(1));
            }

        } catch (SQLException e) {
            LOGGER.error("inserting section into database failed", e);
            throw new DAOException("inserting section into database failed", e);
        }

        generateHistory(section);
    }

    /**
     * updates the object in the database
     *
     * @param section object to update
     * @throws DAOException if an error accessing the database ocurred or if the
     *                      dataset was not found in the database
     */
    @Override
    public void update(Section section) throws DAOException, ValidationException {
        LOGGER.debug("entering update with parameters " + section);

        validator.validateForUpdate(section);

        final String query = "UPDATE RestaurantSection SET name = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, section.getName());
            stmt.setLong(2, section.getIdentity());

            if (stmt.executeUpdate() == 0) {
                throw new DAOException("updating section failed: dataset not found");
            }

        } catch (SQLException e) {
            LOGGER.error("updating section in database failed", e);
            throw new DAOException("updating section in database failed", e);
        }

        generateHistory(section);
    }

    /**
     * removes the object from the database
     *
     * @param section object to remove
     * @throws DAOException if an error accessing the database occurred or if
     *                      the dataset was not found in the database
     */
    @Override
    public void delete(Section section) throws DAOException, ValidationException {
        LOGGER.debug("entering delete with parameters " + section);

        validator.validateForDelete(section);

        final String query = "UPDATE RestaurantSection SET deleted = true WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, section.getIdentity());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("delete section failed: dataset not found");
            }

        } catch (SQLException e) {
            LOGGER.error("deleting section failed", e);
            throw new DAOException("deleting section failed", e);
        }

        generateHistory(section);
    }

    /**
     * returns all objects from the database which parameters match the
     * parameters of the object section
     * all parameters with value NULL will not be used for matching
     *
     * @param section object used for matching
     * @return returns a list of objects from the database which match the criteria
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public List<Section> find(Section section) throws DAOException {
        LOGGER.debug("entering find with parameters " + section);

        if (section == null) {
            return new ArrayList<>();
        }

        String query = "SELECT * FROM RestaurantSection WHERE ID = ISNULL(?, ID) " +
                "AND name = ISNULL(?, name) AND deleted = false";

        final List<Section> objects = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, section.getIdentity());
            stmt.setString(2, section.getName());
            stmt.execute();

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                objects.add(parseResult(result));
            }

        } catch (SQLException e) {
            LOGGER.error("searching for section failed", e);
            throw new DAOException("searching for section failed", e);
        }

        return objects;
    }

    /**
     * @return returns all objects in the database
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public List<Section> getAll() throws DAOException {
        LOGGER.debug("entering getAll");

        final String query = "SELECT * FROM RestaurantSection WHERE deleted = false";
        final List<Section> objects = new ArrayList<>();

        try (Statement stmt = dataSource.getConnection().createStatement()) {
            stmt.execute(query);

            ResultSet result = stmt.getResultSet();
            while (result.next()) {
                objects.add(parseResult(result));
            }

        } catch (SQLException e) {
            LOGGER.error("searching for all sections failed", e);
            throw new DAOException("searching for all sections failed", e);
        }

        return objects;
    }

    /**
     * @param section object to get the history for
     * @return returns the history of changes for the section object
     * @throws DAOException        if the data couldn't be retrieved
     * @throws ValidationException if the section object parameters are
     *                             not valid for this action
     */
    @Override
    public List<History<Section>> getHistory(Section section) throws DAOException, ValidationException {
        LOGGER.debug("entering getHistory with parameters " + section);

        validator.validateIdentity(section);

        List<History<Section>> history = new ArrayList<>();
        final String query = "SELECT * FROM RestaurantSectionHistory WHERE ID = ? ORDER BY changeNr";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, section.getIdentity());
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
     *
     * @param section updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(Section section) throws DAOException {
        LOGGER.debug("entering generateHistory with parameters " + section);

        final String query = "INSERT INTO RestaurantSectionHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM RestaurantSectionHistory WHERE ID = ?) " +
                "FROM RestaurantSection WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, section.getIdentity());          // dataset id
            stmt.setLong(3, section.getIdentity());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }

    /**
     * converts the database query output into a object
     *
     * @param result database output
     * @return Section object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private Section parseResult(ResultSet result) throws SQLException {
        Section section = new Section();
        section.setIdentity(result.getLong("ID"));
        section.setName(result.getString("name"));
        return section;
    }

    /**
     * converts the database query output into a history entry object
     *
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the user ocurred
     */
    private History<Section> parseHistoryEntry(ResultSet result) throws DAOException, SQLException {
        // get user
        List<User> storedUsers = userDAO.find(User.withIdentity(result.getString("changeUser")));
        if (storedUsers.size() != 1) {
            throw new DAOException("user not found");
        }

        // create history entry
        History<Section> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(parseResult(result));

        return historyEntry;
    }
}
