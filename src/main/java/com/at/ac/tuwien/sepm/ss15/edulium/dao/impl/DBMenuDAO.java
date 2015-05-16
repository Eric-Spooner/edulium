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
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.LongStream;

/**
 * H2 Database Implementation of the MenuEntry DAO interface
 */
public class DBMenuDAO implements DAO<Menu> {
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
        Long changeNr =  generateHistoryMenu(menu);
        for(MenuEntry entry:menu.getEntries()) {
            createMenuAssoc(menu, entry, entry.getPrice());
            generateHistoryMenuAssoc(menu,entry, changeNr);
        }
    }

    @Override
    public void update(Menu menu) throws DAOException, ValidationException {
        LOGGER.debug("entering update with parameters " + menu);

        validator.validateForUpdate(menu);
        final String queryUpdateMenu = "UPDATE Menu SET name = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryUpdateMenu)) {
            stmt.setString(1,menu.getName());
            stmt.setLong(2,menu.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("updating menu in database failed, dataset not found");
                throw new DAOException("updating menu in database failed, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("updating menu in database failed", e);
            throw new DAOException("updating menu in database failed", e);
        }
        Long changeNr = generateHistoryMenu(menu);
        /**
         * Update Entries:
         * Check the MenuAssoc Table. If the entry is found in the MenuEntrys copy, then update the price
         * and delete the entry of the MenuEntrys copy
         * If the entry is not found, then delete it. If there are entries left in the MenuEntrys copy than
         * create new MenuAssoc
         */
        List<MenuEntry> menuEntries = new LinkedList<MenuEntry>(menu.getEntries());
        final String queryGetMenuAssoc = "SELECT * FROM MenuAssoc WHERE menu_ID = ? AND disabled = false";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryGetMenuAssoc)) {
            stmt.setLong(1,menu.getIdentity());
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                MenuEntry entry = MenuEntry.withIdentity(result.getLong("menuEntry_ID"));
                //Look, if the entry ID is in the list
                //Contains is not appropiate, because the entries only have the same ID
                Boolean found = false;
                for(MenuEntry entry1 : menuEntries){
                   if(entry1.getIdentity() == entry.getIdentity()) {
                       found = true;
                       //Delete the entry, in order to be able to check at the end, if new MenuAssoc have to be made
                       menuEntries.remove(entry1);
                   }
                }
                if(found){
                    //The Entry is in the list, so update the price
                    updateMenuAssoc(menu, entry, entry.getPrice());
                }else{
                    //The Entry is not in the list, so it should be deleted
                   deleteMenuAssoc(menu, entry);
                }
                generateHistoryMenuAssoc(menu, entry, changeNr);
            }
        } catch (SQLException e) {
            LOGGER.error("updating menu in database failed", e);
            throw new DAOException("updating menu in database failed", e);
        }

        //Check if there are Entries left
        if(menuEntries.size() >0){
            for(MenuEntry entry : menuEntries){
               createMenuAssoc(menu, entry, entry.getPrice());
               generateHistoryMenuAssoc(menu, entry, changeNr);
            }
        }
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
        Long changeNr = generateHistoryMenu(menu);

        for(MenuEntry entry:menu.getEntries()){
            deleteMenuAssoc(menu, entry);
            generateHistoryMenuAssoc(menu, entry, changeNr);
        }

        menu.setEntries(new LinkedList<MenuEntry>());
    }

    @Override
    public List<Menu> find(Menu menu) throws DAOException {
        LOGGER.debug("entering find with parameters " + menu);

        if (menu == null) {
            return new ArrayList<>();
        }

        String query = "SELECT * FROM Menu WHERE " +
                "ID = ISNULL(?, ID) AND " +
                "name = ISNULL(?, name) AND " +
                "deleted = false";

        final List<Menu> objects = new ArrayList<>();

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
        List<MenuEntry> menuEntries = new LinkedList<MenuEntry>();
        String query = "SELECT * FROM MenuAssoc WHERE " +
                "menu_ID = ? AND " +
                "disabled = false";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, result.getLong("ID"));

            ResultSet resultEntries = stmt.executeQuery();
            while (resultEntries.next()) {
                MenuEntry entry = MenuEntry.withIdentity(resultEntries.getLong("menuEntry_ID"));
                menuEntries.add(menuEntryDAO.find(entry).get(0));
            }
        } catch (SQLException e) {
            LOGGER.error("searching for menu entries failed", e);
            throw new DAOException("searching for menu entries failed", e);
        }

        if(menuEntries.size()>0){
            menu.setEntries(menuEntries);
        }else{
            throw new DAOException("there is no menu Entry for the searched menu");
        }

        return  menu;
    }

    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed
     * the changes
     * @param menu updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private Long generateHistoryMenu(Menu menu) throws DAOException {
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

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryGetChangeNr)) {
            stmt.setLong(1,menu.getIdentity());          // dataset id
            ResultSet result = stmt.executeQuery();
            Long resultNr = -1L;
            while(result.next()){
                resultNr = result.getLong(1);
            }
            if(resultNr == -1){
                LOGGER.error("getting changeNr failed");
                throw new DAOException("getting changeNr failed");
            }else{
                return resultNr;
            }
        } catch (SQLException e) {
            LOGGER.error("getting changeNr failed", e);
            throw new DAOException("getting changeNr failed", e);
        }
    }

    private void generateHistoryMenuAssoc(Menu menu, MenuEntry entry, Long changeNr) throws DAOException {
        LOGGER.debug("entering generateHistory with parameters " + menu);
        final String query2 = "INSERT INTO MenuAssocHistory " +
                   "(SELECT *, CURRENT_TIMESTAMP(), ?, ? " +
                   "FROM MenuAssoc WHERE menu_ID = ? AND menuEntry_ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query2)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, changeNr);
            stmt.setLong(3, menu.getIdentity());          // dataset id
            stmt.setLong(4, entry.getIdentity());          // dataset id
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating MenuAssoc history failed", e);
            throw new DAOException("generating MenuAssoc history failed", e);
        }
    }

    /**
     * converts the database query output into a history entry object
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
        History<Menu> historyEntry = new History<Menu>();
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
        List<MenuEntry> menuEntries = new LinkedList<MenuEntry>();
        String query = "SELECT * FROM MenuAssocHistory WHERE " +
                "menu_ID = ? AND changeNr = ? AND disabled = false";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, result.getLong("ID"));
            stmt.setLong(2, changeNr);

            ResultSet resultEntries = stmt.executeQuery();
            while (resultEntries.next()) {
                MenuEntry entry = MenuEntry.withIdentity(resultEntries.getLong("menuEntry_ID"));
                menuEntries.add(menuEntryDAO.find(entry).get(0));
            }
        } catch (SQLException e) {
            LOGGER.error("searching for menu entries failed", e);
            throw new DAOException("searching for menu entries failed", e);
        }
        menu.setEntries(menuEntries);
        return  menu;
    }


    /**
     * This function is used, to update Menu Assoc Entries
     * if the Menu Assoc is disabled, then enable it again
     * the parameters are used, to identify the Menu Assoc and to set the values
     * @param menu
     * @param entry
     * @param price
     */
    private void updateMenuAssoc(Menu menu, MenuEntry entry, BigDecimal price) throws DAOException{
        //At first, look if the
        findAndEnableMenuAssoc(menu, entry);

        final String queryUpdateMenuAssoc =
                "UPDATE MenuAssoc SET menuPrice = ? WHERE menu_ID = ? AND menuEntry_ID = ? AND disabled = false";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryUpdateMenuAssoc)) {
            stmt.setBigDecimal(1, entry.getPrice());
            stmt.setLong(2, menu.getIdentity());
            stmt.setLong(3,entry.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("updating menuAssoc in database failed, dataset not found");
                throw new DAOException("updating menuAssoc in database failed, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("updating menuAssoc in database failed", e);
            throw new DAOException("updating menuAssoc in database failed", e);
        }
    }
    /**
     * This function is used, to insert Menu Assoc Entries
     * if the Menu Assoc is disabled, then enable it again
     * the parameters are used, to identify the Menu Assoc and to set the values
     * @param menu
     * @param entry
     * @param price
     */
    private void createMenuAssoc(Menu menu, MenuEntry entry, BigDecimal price) throws  DAOException{
        if(findAndEnableMenuAssoc(menu, entry)) {
            //The Menu Assoc is already there, but and if necessary, it has been enabled
            updateMenuAssoc(menu, entry, price);
        }else{
            final String queryInsertIntoMenuAssoc =
                    "INSERT INTO MenuAssoc (menu_ID, menuEntry_ID, menuPrice) VALUES (?,?,?)";

            try (PreparedStatement stmt =
                         dataSource.getConnection().prepareStatement(queryInsertIntoMenuAssoc, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, menu.getIdentity());
                stmt.setLong(2, entry.getIdentity());
                stmt.setBigDecimal(3,entry.getPrice());
                stmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.error("inserting menu association to menuEntry into database failed", e);
                throw new DAOException("inserting menu association to menuEntry into database failed", e);
            }
        }
    }
    /**
     * This function is used, to delete Menu Assoc Entries
     * if the Menu Assoc is enabled, than disable it.
     * @param menu
     * @param entry
     */
    private void deleteMenuAssoc(Menu menu, MenuEntry entry) throws DAOException{
        //The Entry is not in the list, so it should be disabled
        final String query = "UPDATE MenuAssoc SET disabled = true WHERE menu_ID = ? AND menuEntry_ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, menu.getIdentity());
            stmt.setLong(2,entry.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("deleting menuAssoc failed, dataset not found");
                throw new DAOException("deleting menuAssoc failed, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("deleting menuAssoc failed", e);
            throw new DAOException("deleting menuAssoc failed", e);
        }
    }

    /**
     * This function is used to search MenuAssocs and to enable it again, if they are disabled
     * @param menu
     * @param entry
     * @return true, if the MenuAssoc is already there
     */
    private boolean findAndEnableMenuAssoc(Menu menu, MenuEntry entry) throws DAOException{
        final String querySelect =
                "SELECT * FROM MenuAssoc WHERE menu_ID = ? AND menuEntry_ID = ?";

        try (PreparedStatement stmt =
                     dataSource.getConnection().prepareStatement(querySelect)) {
            stmt.setLong(1, menu.getIdentity());
            stmt.setLong(2, entry.getIdentity());
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                //The Menu Assoc has been found
                if(result.getBoolean("disabled")){
                    //Enable the entry again
                    final String query = "UPDATE MenuAssoc SET disabled = false WHERE menu_ID = ? AND menuEntry_ID = ?";

                    try (PreparedStatement stmt2 = dataSource.getConnection().prepareStatement(query)) {
                        stmt2.setLong(1, menu.getIdentity());
                        stmt2.setLong(2,entry.getIdentity());

                        if (stmt2.executeUpdate() == 0) {
                            LOGGER.error("enabling menuAssoc failed, dataset not found");
                            throw new DAOException("enabling menuAssoc failed, dataset not found");
                        }
                    } catch (SQLException e) {
                        LOGGER.error("enabling menuAssoc failed", e);
                        throw new DAOException("enabling menuAssoc failed", e);
                    }
                }
                return true;
            }else{
                //The Menu Assoc has not been found
                return false;
            }
        } catch (SQLException e) {
            LOGGER.error("searching menu association in database failed", e);
            throw new DAOException("searching menu association in database failed", e);
        }
    }
}
