package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * H2 Database Implementation of the MenuEntry DAO interface
 */
@PreAuthorize("isAuthenticated()")
class DBMenuDAO implements DAO<Menu> {
    private static final Logger LOGGER = LogManager.getLogger(DBMenuDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Validator<Menu> validator;
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private DAO<User> userDAO;

    @Override
    public void create(Menu menu) throws DAOException, ValidationException {
        LOGGER.debug("entering create with parameters " + menu);

        validator.validateForCreate(menu);
        final String queryMenu = "INSERT INTO Menu (name) VALUES (?)";

        try (PreparedStatement stmt =
                     dataSource.getConnection().prepareStatement(queryMenu, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, menu.getName());
            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                menu.setIdentity(key.getLong(1));
            }
            key.close();
        } catch (SQLException e) {
            LOGGER.error("inserting menu into database failed", e);
            throw new DAOException("inserting menu into database failed", e);
        }
        updateMenuAssoc(menu);
        generateHistoryMenu(menu);
    }

    @Override
    public void update(Menu menu) throws DAOException, ValidationException {
        LOGGER.debug("entering update with parameters " + menu);

        validator.validateForUpdate(menu);
        final String queryUpdateMenu = "UPDATE Menu SET name = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryUpdateMenu)) {
            stmt.setString(1, menu.getName());
            stmt.setLong(2, menu.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("updating menu in database failed, dataset not found");
                throw new DAOException("updating menu in database failed, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("updating menu in database failed", e);
            throw new DAOException("updating menu in database failed", e);
        }

        updateMenuAssoc(menu);
        generateHistoryMenu(menu);
    }

    @Override
    public void delete(Menu menu) throws DAOException, ValidationException {
        LOGGER.debug("entering delete with parameters " + menu);

        validator.validateForDelete(menu);
        final String query = "UPDATE Menu SET deleted = true WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, menu.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("deleting menu failed, dataset not found");
                throw new DAOException("deleting menu failed, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("deleting menu failed", e);
            throw new DAOException("deleting menu failed", e);
        }
        generateHistoryMenu(menu);
    }

    @Override
    public List<Menu> find(Menu menu) throws DAOException {
        LOGGER.debug("entering find with parameters " + menu);

        if (menu == null) {
            return new ArrayList<>();
        }

        final List<Menu> objects = new ArrayList<>();

        //Check if the user also wants to search for MenuEntries
        if (menu.getEntries() != null && menu.getEntries().size() > 0) {
            //at first find the MenuEntries, which have been given in the menu
            final String queryByMenuEntry =
                    "SELECT m.ID, m.name FROM Menu m WHERE " +
                            "m.deleted = false AND " +
                            "m.ID = ISNULL(?, ID) AND " +
                            "m.name = ISNULL(?, name) AND " +
                            "EXISTS (SELECT 1 FROM MenuAssoc ma WHERE " +
                            "ma.menu_ID = m.ID AND "+
                            "ma.menuEntry_ID in (" +
                            menu.getEntries().stream().map(m -> "?").collect(Collectors.joining(",")) +
                            ") AND ma.disabled = false)";

            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryByMenuEntry)) {
                stmt.setObject(1, menu.getIdentity());
                stmt.setObject(2, menu.getName());
                int i = 3;
                for (MenuEntry entry:menu.getEntries()){
                    stmt.setLong(i, entry.getIdentity());
                    i++;
                }

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    objects.add(parseResult(result));
                }
            } catch (SQLException e) {
                LOGGER.error("searching for menu entries failed", e);
                throw new DAOException("searching for menu entries failed", e);
            }
        } else { //Only search for the name and/or the id
            final String query = "SELECT * FROM Menu WHERE " +
                    "ID = ISNULL(?, ID) AND " +
                    "name = ISNULL(?, name) AND " +
                    "deleted = false";
            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
                stmt.setObject(1, menu.getIdentity());
                stmt.setObject(2, menu.getName());

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    objects.add(parseResult(result));
                }
            } catch (SQLException e) {
                LOGGER.error("searching for menu entries failed", e);
                throw new DAOException("searching for menu entries failed", e);
            }
        }
        return objects;
    }

    @Override
    public List<Menu> getAll() throws DAOException {
        LOGGER.debug("entering getAll");

        final String query = "SELECT * FROM Menu WHERE deleted = false";
        final List<Menu> objects = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                objects.add(parseResult(result));
            }
        } catch (SQLException e) {
            LOGGER.error("searching for all entries failed", e);
            throw new DAOException("searching for all entries failed", e);
        }
        return objects;
    }

    @Override
    public List<History<Menu>> getHistory(Menu menu) throws DAOException, ValidationException {
        LOGGER.debug("entering getHistory with parameters " + menu);

        validator.validateIdentity(menu);
        List<History<Menu>> history = new ArrayList<>();
        final String query = "SELECT * FROM MenuHistory WHERE ID = ? ORDER BY changeNr";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, menu.getIdentity());
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
     * converts the database query output into a object
     *
     * @param result database output
     * @return Menu object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the taxRate/category occurred
     */
    private Menu parseResult(ResultSet result) throws DAOException, SQLException {
        Menu menu = new Menu();
        menu.setIdentity(result.getLong("ID"));
        menu.setName(result.getString("name"));
        /*Get the MenuEntries over MenuAssoc*/
        menu.setEntries(getResultMenuEntries(menu));
        return menu;
    }

    /**
     * This funktion is used to get the menu entries of the given menu from the database
     * @param menu the menu, the menu Enries should be given back
     * @return
     */
    private List<MenuEntry> getResultMenuEntries(Menu menu) throws DAOException, SQLException{
        List<MenuEntry> entries = new LinkedList<>();

        String query = "SELECT * FROM MenuAssoc WHERE " +
                "menu_ID = ? AND " +
                "disabled = false";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, menu.getIdentity());

            ResultSet resultEntries = stmt.executeQuery();
            while (resultEntries.next()) {
                MenuEntry entry = MenuEntry.withIdentity(resultEntries.getLong("menuEntry_ID"));
                List<MenuEntry> find = menuEntryDAO.find(entry);
                if(find.size()>0){
                    entries.add(menuEntryDAO.find(entry).get(0));
                }else {
                    LOGGER.error("searching for menu entries failed because given ID is not in the Database");
                    throw new DAOException("searching for menu entries failed because given ID is not in the Database");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("searching for menu entries failed", e);
            throw new DAOException("searching for menu entries failed", e);
        }
        return entries;
    }

    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed
     * the changes
     *
     * @param menu updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistoryMenu(Menu menu) throws DAOException {
        LOGGER.debug("entering generateHistory with parameters " + menu);

        final String query = "INSERT INTO MenuHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM MenuHistory WHERE ID = ?) " +
                "FROM Menu WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, menu.getIdentity());          // dataset id
            stmt.setLong(3, menu.getIdentity());          // dataset id
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating Menu history failed", e);
            throw new DAOException("generating Menu history failed", e);
        }
        //get the change Nr, for Menu Assoc
        final String queryGetChangeNr = "SELECT MAX(changeNr) FROM MenuHistory WHERE ID = ?";
        Long resultNr = -1L;
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryGetChangeNr)) {
            stmt.setLong(1, menu.getIdentity());          // dataset id
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                resultNr = result.getLong(1);
            }else {
                LOGGER.error("getting changeNr failed");
                throw new DAOException("getting changeNr failed");
            }
        } catch (SQLException e) {
            LOGGER.error("getting changeNr failed", e);
            throw new DAOException("getting changeNr failed", e);
        }

        generateHistoryMenuAssoc(menu, resultNr);
    }
    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed
     * the changes
     *
     * @param menu     updated dataset
     * @param changeNr The Change Nr of the Menu History
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistoryMenuAssoc(Menu menu, Long changeNr) throws DAOException {
        LOGGER.debug("entering generateHistory with parameters " + menu);
        final String query2 = "INSERT INTO MenuAssocHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, ? " +
                "FROM MenuAssoc WHERE menu_ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query2)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, changeNr);
            stmt.setLong(3, menu.getIdentity());          // dataset id
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating MenuAssoc history failed", e);
            throw new DAOException("generating MenuAssoc history failed", e);
        }
    }
    /**
     * converts the database query output into a history entry object
     *
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the user ocurred
     */
    private History<Menu> parseHistoryEntry(ResultSet result) throws DAOException, SQLException {
        // get user
        List<User> storedUsers = userDAO.find(User.withIdentity(result.getString("changeUser")));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }
        // create history entry
        History<Menu> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(parseResultHistory(result, historyEntry.getChangeNumber()));

        return historyEntry;
    }
    /**
     * converts the database query output into a object
     * get the History Menu with the MenuEntries with the same History ChangeNr
     *
     * @param result database output from History
     * @return Menu object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the taxRate/category occurred
     */
    private Menu parseResultHistory(ResultSet result, Long changeNr) throws DAOException, SQLException {
        Menu menu = new Menu();
        menu.setIdentity(result.getLong("ID"));
        menu.setName(result.getString("name"));
        /*Get the MenuEntries over MenuAssocHistory*/
        menu.setEntries(getResultHistoryMenuEntries(menu, changeNr));
        return menu;
    }

    private List<MenuEntry> getResultHistoryMenuEntries(Menu menu, Long changeNr) throws DAOException, SQLException {
        List<MenuEntry> menuEntries = new LinkedList<>();
        String query = "SELECT * FROM MenuAssocHistory WHERE " +
                "menu_ID = ? AND changeNr = ? AND disabled = false";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, menu.getIdentity());
            stmt.setLong(2, changeNr);

            ResultSet resultEntries = stmt.executeQuery();
            while (resultEntries.next()) {
                MenuEntry entry = MenuEntry.withIdentity(resultEntries.getLong("menuEntry_ID"));
                List<MenuEntry> find = menuEntryDAO.find(entry);
                if(find.size()>0){
                    menuEntries.add(menuEntryDAO.find(entry).get(0));
                }else {
                    LOGGER.error("searching for menu entries failed because given ID is not in the Database");
                    throw new DAOException("searching for menu entries failed because given ID is not in the Database");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("searching for menu entries failed", e);
            throw new DAOException("searching for menu entries failed", e);
        }
        return menuEntries;
    }


    /**
     * This function is used, to update/create Menu Assoc Entries
     * All MenuAssoc with the MenuID are disabled, the one, which are used are enabled again and the rest is added
     * the parameters are used, to identify the Menu Assoc and to set the values
     *
     * @param menu Menu Is used to identify the MenuAssoc
     */
    private void updateMenuAssoc(Menu menu) throws DAOException {
        //At first, look if the
        // disable all MenuAssoc for now, the update sql query will re-enable all valid
        // associations
        final String queryDiasbleMenuAssoc = "UPDATE MenuAssoc SET disabled = true WHERE menu_ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryDiasbleMenuAssoc)) {
            stmt.setLong(1, menu.getIdentity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Disabling Menu-MenuEntry associations from database failed", e);
            throw new DAOException("Disabling Menu-MenuEntry associations from database failed", e);
        }

        final String queryUpdateMenuAssoc =
                "Merge INTO MenuAssoc (menu_ID, menuEntry_ID, menuPrice, disabled) " +
                        "KEY (menu_ID, menuEntry_ID) VALUES (?, ?, ?, false)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryUpdateMenuAssoc)) {
            for (MenuEntry entry : menu.getEntries()) {
                stmt.setLong(1, menu.getIdentity());
                stmt.setLong(2, entry.getIdentity());
                stmt.setBigDecimal(3, entry.getPrice());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("updating menuAssoc in database failed", e);
            throw new DAOException("updating menuAssoc in database failed", e);
        }
    }
}