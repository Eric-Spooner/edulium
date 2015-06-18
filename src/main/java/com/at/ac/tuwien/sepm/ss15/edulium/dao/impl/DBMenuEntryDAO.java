package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * H2 Database Implementation of the MenuEntry DAO interface
 */
@PreAuthorize("isAuthenticated()")
class DBMenuEntryDAO implements DAO<MenuEntry> {
    private static final Logger LOGGER = LogManager.getLogger(DBMenuEntryDAO.class);

    @Resource(name = "dataSource")
    private DataSource dataSource;
    @Resource(name = "menuEntryValidator")
    private Validator<MenuEntry> validator;
    @Resource(name = "taxRateDAO")
    private DAO<TaxRate> taxRateDAO;
    @Resource(name = "menuCategoryDAO")
    private DAO<MenuCategory> menuCategoryDAO;
    @Resource(name = "userDAO")
    private DAO<User> userDAO;

    @Override
    public void create(MenuEntry menuEntry) throws DAOException, ValidationException {
        LOGGER.debug("entering create with parameters " + menuEntry);

        validator.validateForCreate(menuEntry);

        final String query = "INSERT INTO MenuEntry (name, price, available, description, taxRate_ID, category_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, menuEntry.getName());
            stmt.setBigDecimal(2, menuEntry.getPrice());
            stmt.setBoolean(3, menuEntry.getAvailable());
            stmt.setString(4, menuEntry.getDescription());
            stmt.setObject(5, menuEntry.getTaxRate().getIdentity());
            stmt.setObject(6, menuEntry.getCategory().getIdentity());
            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                menuEntry.setIdentity(key.getLong(1));
            }
            key.close();
        } catch (SQLException e) {
            LOGGER.error("inserting menuEntry into database failed", e);
            throw new DAOException("inserting menuEntry into database failed", e);
        }

        generateHistory(menuEntry);
    }

    @Override
    public void update(MenuEntry menuEntry) throws DAOException, ValidationException {
        LOGGER.debug("entering update with parameters " + menuEntry);

        validator.validateForUpdate(menuEntry);

        final String query = "UPDATE MenuEntry SET name = ?, price = ?, available = ?, " +
                "description = ?, taxRate_ID = ?, category_ID = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, menuEntry.getName());
            stmt.setBigDecimal(2, menuEntry.getPrice());
            stmt.setBoolean(3, menuEntry.getAvailable());
            stmt.setString(4, menuEntry.getDescription());
            stmt.setObject(5, menuEntry.getTaxRate().getIdentity());
            stmt.setObject(6, menuEntry.getCategory().getIdentity());
            stmt.setLong(7, menuEntry.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("updating menuEntry in database failed, dataset not found");
                throw new DAOException("updating menuEntry in database failed, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("updating menuEntry in database failed", e);
            throw new DAOException("updating menuEntry in database failed", e);
        }

        generateHistory(menuEntry);
    }

    @Override
    public void delete(MenuEntry menuEntry) throws DAOException, ValidationException {
        LOGGER.debug("entering delete with parameters " + menuEntry);

        validator.validateForDelete(menuEntry);

        final String query = "UPDATE MenuEntry SET deleted = true WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, menuEntry.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("deleting menuEntry failed, dataset not found");
                throw new DAOException("deleting menuEntry failed, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("deleting menuEntry failed", e);
            throw new DAOException("deleting menuEntry failed", e);
        }

        generateHistory(menuEntry);
    }

    @Override
    public List<MenuEntry> find(MenuEntry menuEntry) throws DAOException {
        LOGGER.debug("entering find with parameters " + menuEntry);

        if (menuEntry == null) {
            return new ArrayList<>();
        }

        final String query = "SELECT * FROM MenuEntry WHERE " +
                    "ID = ISNULL(?, ID) AND " +
                    "name = ISNULL(?, name) AND " +
                    "price = ISNULL(?, price) AND " +
                    "available = ISNULL(?, available) AND " +
                    "description = ISNULL(?, description) AND " +
                    "taxRate_ID = ISNULL(?, taxRate_ID) AND " +
                    "category_ID = ISNULL(?, category_ID) AND " +
                    "deleted = false";

        final List<MenuEntry> objects = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            TaxRate taxRate = menuEntry.getTaxRate();
            MenuCategory category = menuEntry.getCategory();

            stmt.setObject(1, menuEntry.getIdentity());
            stmt.setObject(2, menuEntry.getName());
            stmt.setObject(3, menuEntry.getPrice());
            stmt.setObject(4, menuEntry.getAvailable());
            stmt.setObject(5, menuEntry.getDescription());
            stmt.setObject(6, taxRate == null ? null : taxRate.getIdentity());
            stmt.setObject(7, category == null ? null : category.getIdentity());

            ResultSet result = stmt.executeQuery();
            LOGGER.info(stmt.toString());
            while (result.next()) {
                try {
                    objects.add(parseResult(result));
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + result + "' failed", e);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("searching for menu entries failed", e);
            throw new DAOException("searching for menu entries failed", e);
        }

        return objects;
    }

    @Override
    public List<MenuEntry> getAll() throws DAOException {
        LOGGER.debug("entering getAll");

        final String query = "SELECT * FROM MenuEntry WHERE deleted = false";

        final List<MenuEntry> objects = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                try {
                    objects.add(parseResult(result));
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + result + "' failed", e);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("searching for all entries failed", e);
            throw new DAOException("searching for all entries failed", e);
        }

        return objects;
    }

    @Override
    public List<History<MenuEntry>> getHistory(MenuEntry menuEntry) throws DAOException, ValidationException {
        LOGGER.debug("entering getHistory with parameters " + menuEntry);

        validator.validateIdentity(menuEntry);

        final String query = "SELECT * FROM MenuEntryHistory WHERE ID = ? ORDER BY changeNr";

        List<History<MenuEntry>> history = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, menuEntry.getIdentity());
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

    @Override
    public List<MenuEntry> populate(List<MenuEntry> menuEntries) throws DAOException, ValidationException {
        LOGGER.debug("Entering populate with parameters: " + menuEntries);

        if (menuEntries == null || menuEntries.isEmpty()) {
            return new ArrayList<>();
        }

        for (MenuEntry menuEntry : menuEntries) {
            validator.validateIdentity(menuEntry);
        }

        final String query = "SELECT * FROM MenuEntry WHERE ID IN (" +
                menuEntries.stream().map(u -> "?").collect(Collectors.joining(", ")) + ")"; // fake a list of identities

        final List<MenuEntry> populatedMenuEntries = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            int index = 1;

            // fill identity list
            for (MenuEntry menuEntry : menuEntries) {
                stmt.setLong(index++, menuEntry.getIdentity());
            }

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                populatedMenuEntries.add(parseResult(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Populating menu entries failed", e);
            throw new DAOException("Populating menu entries failed", e);
        }

        return populatedMenuEntries;
    }

    private void generateHistory(MenuEntry menuEntry) throws DAOException {
        LOGGER.debug("entering generateHistory with parameters " + menuEntry);

        final String query = "INSERT INTO MenuEntryHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM MenuEntryHistory WHERE ID = ?) " +
                "FROM MenuEntry WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, menuEntry.getIdentity());          // dataset id
            stmt.setLong(3, menuEntry.getIdentity());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }

    /**
     * converts the database query output into a object
     * @param result database output
     * @return MenuEntry object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the taxRate/category occurred
     */
    private MenuEntry parseResult(ResultSet result) throws DAOException, ValidationException, SQLException {
        List<TaxRate> taxRates = taxRateDAO.populate(Arrays.asList(TaxRate.withIdentity(result.getLong("taxRate_ID"))));
        if (taxRates.size() != 1) {
            LOGGER.error("retrieving taxRate failed");
            throw new DAOException("retrieving taxRate failed");
        }

        List<MenuCategory> categories = menuCategoryDAO.populate(Arrays.asList(MenuCategory.withIdentity(result.getLong("category_ID"))));
        if (categories.size() != 1) {
            LOGGER.error("retrieving category failed");
            throw new DAOException("retrieving category failed");
        }

        MenuEntry entry = new MenuEntry();
        entry.setIdentity(result.getLong("ID"));
        entry.setName(result.getString("name"));
        entry.setPrice(result.getBigDecimal("price"));
        entry.setAvailable(result.getBoolean("available"));
        entry.setDescription(result.getString("description"));
        entry.setCategory(categories.get(0));
        entry.setTaxRate(taxRates.get(0));

        return entry;
    }

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the user ocurred
     */
    private History<MenuEntry> parseHistoryEntry(ResultSet result) throws DAOException, ValidationException, SQLException {
        // get user
        List<User> storedUsers = userDAO.populate(Arrays.asList(User.withIdentity(result.getString("changeUser"))));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        // create history entry
        History<MenuEntry> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(parseResult(result));

        return historyEntry;
    }
}
