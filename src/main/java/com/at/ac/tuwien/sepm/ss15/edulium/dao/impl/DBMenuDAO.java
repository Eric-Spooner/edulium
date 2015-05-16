package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    @Override
    public void create(Menu menu) throws DAOException, ValidationException {
        LOGGER.debug("entering create with parameters " + menu);

        validator.validateForCreate(menu);
        final String query = "INSERT INTO Menu (name) VALUES (?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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

        final String query2 = "INSERT INTO MenuAssoc (menu_ID, menuEntry_ID, menuPrice) VALUES (?,?,?)";
/*
        menu_ID BIGINT REFERENCES Menu(ID),
                menuEntry_ID BIGINT REFERENCES MenuEntry(ID),
                menuPrice DECIMAL(20, 2),
  */
        for(MenuEntry entry:menu.getEntries()) {
            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query2, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, menu.getIdentity());
                stmt.setLong(2, entry.getIdentity());
                stmt.setBigDecimal(3,entry.getPrice());
                stmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.error("inserting menu association to menuEntry into database failed", e);
                throw new DAOException("inserting menu association to menuEntry into database failed", e);
            }
        }
        //generateHistory(menuCategory);
    }

    @Override
    public void update(Menu object) throws DAOException, ValidationException {

    }

    @Override
    public void delete(Menu object) throws DAOException, ValidationException {
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
        return null;
    }

    @Override
    public List<History<Menu>> getHistory(Menu object) throws DAOException, ValidationException {
        return null;
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
    private void generateHistory(Menu menu) throws DAOException {
        LOGGER.debug("entering generateHistory with parameters " + menu);

        final String query = "INSERT INTO MenuHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM MenuCategoryHistory Menu ID = ?) " +
                "FROM Menu WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, menu.getIdentity());          // dataset id
            stmt.setLong(3, menu.getIdentity());          // dataset id
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }
}
