package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.MenuCategoryDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.MenuCategoryValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 Database Implementation of the MenuCategoryDAO interface
 */
class MenuCategoryDAOImpl implements MenuCategoryDAO {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MenuCategoryValidator validator;
    private static final Logger LOGGER = LogManager.getLogger(MenuCategoryDAO.class);

    /**
     * writes the object into the database and sets the identity parameter of
     * menuCategory
     * @param menuCategory object to store
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public void create(MenuCategory menuCategory) throws DAOException, ValidationException {
        LOGGER.debug("entering create with parameters " + menuCategory);

        validator.validateForCreate(menuCategory);
        final String query = "INSERT INTO MenuCategory (name) VALUES (?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, menuCategory.getName());
            if (stmt.executeUpdate() == 0) {
                LOGGER.error("inserting menuCategory into database failed");
                throw new DAOException("inserting menuCategory into database failed");
            }

            try (ResultSet key = stmt.getGeneratedKeys()) {
                key.next();
                menuCategory.setIdentity(key.getLong(1));
            }

        } catch (SQLException e) {
            LOGGER.error("inserting menuCategory into database failed");
            throw new DAOException("inserting menuCategory into database failed", e);
        }

        generateHistory(menuCategory.getIdentity());
    }

    /**
     * updates the object in the database
     * @param menuCategory object to update
     * @throws DAOException if an error accessing the database ocurred or if the
     *         dataset was not found in the database
     */
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
            LOGGER.error("updating menuCategory in database failed");
            throw new DAOException(e);
        }

        generateHistory(menuCategory.getIdentity());
    }

    /**
     * removes the object from the database
     * @param menuCategory object to remove
     * @throws DAOException if an error accessing the database occurred or if
     *         the dataset was not found in the database
     */
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
            LOGGER.error("deleting menuCategory failed");
            throw new DAOException("deleting menuCategory failed", e);
        }

        generateHistory(menuCategory.getIdentity());
    }

    /**
     * returns all objects from the database which parameters match the
     * parameters of the object menuCategory
     * all parameters with value NULL will not be used for matching
     * @param menuCategory object used for matching
     * @return returns a list of objects from the database which match the criteria
     * @throws DAOException if an error accessing the database occurred
     */
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
            stmt.execute();

            try (ResultSet result = stmt.getResultSet()) {
                while (result.next()) {
                    objects.add(parseResult(result));
                }
            }

        } catch (SQLException e) {
            LOGGER.error("retrieving data failed");
            throw new DAOException("retrieving data failed", e);
        }
        LOGGER.debug("return " + objects.size() + " datasets");
        return objects;
    }

    /**
     * @return returns all objects in the database
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public List<MenuCategory> getAll() throws DAOException {
        LOGGER.debug("entering getAll");

        final String query = "SELECT * FROM MenuCategory WHERE deleted = false";
        final List<MenuCategory> objects = new ArrayList<>();

        try (Statement stmt = dataSource.getConnection().createStatement()) {
            stmt.execute(query);

            try (ResultSet result = stmt.getResultSet()) {
                while (result.next()) {
                    objects.add(parseResult(result));
                }
            }

        } catch (SQLException e) {
            LOGGER.error("retrieving data failed");
            throw new DAOException("retrieving data failed", e);
        }
        LOGGER.debug("return " + objects.size() + " datasets");
        return objects;
    }

    // FIXME add implementation when session object is implemented
    /**
     * writes the changes of the dataset with the id IDENTITY into the database
     * stores the time; number of the change and the user which executed
     * the changes
     * @param identity identity of the changed dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(long identity) throws DAOException {
        /*
        final String query = "INSERT INTO MenuCategoryHistory " +
                "(SELECT ID, name, deleted, ?, ?, NULL FROM MenuCategory WHERE ID = ?)";
        final Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setTimestamp(1, timestamp);    // time
            stmt.setInt(2, 0);                  // user TODO
            stmt.setLong(3, identity);          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        */
    }

    /**
     * converts the database query output into a object
     * @param result database output
     * @return MenuCategory object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private MenuCategory parseResult(ResultSet result) throws SQLException {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setIdentity(result.getLong(1));
        menuCategory.setName(result.getString(2));
        return menuCategory;
    }
}
