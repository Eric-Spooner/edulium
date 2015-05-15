package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
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
import java.util.List;

/**
 * H2 Database Implementation of the MenuEntry DAO interface
 */
public class DBMenuDAO implements DAO<Menu> {
    private static final Logger LOGGER = LogManager.getLogger(DBMenuDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private Validator<Menu> validator;
    @Override
    public void create(Menu menu) throws DAOException, ValidationException {
        LOGGER.debug("entering create with parameters " + menu);

        validator.validateForCreate(menu);
        final String query = "INSERT INTO Menu (name) VALUES (?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                menu.setIdentity(key.getLong(1));
            }
            key.close();
            stmt.setLong(1,menu.getIdentity());
            stmt.setString(2, menu.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("inserting menu into database failed", e);
            throw new DAOException("inserting menu into database failed", e);
        }

        final String query2 = "INSERT INTO MenuAssoc (name) VALUES (?)";
/*
        menu_ID BIGINT REFERENCES Menu(ID),
                menuEntry_ID BIGINT REFERENCES MenuEntry(ID),
                menuPrice DECIMAL(20, 2),
  */
        for(MenuEntry entry:menu.getEntries()) {
            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, menu.getIdentity());
                stmt.setLong(2,entry.getIdentity());
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
    public List<Menu> find(Menu object) throws DAOException {
        return null;
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
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed
     * the changes
     * @param menu updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(Menu menu) throws DAOException {
        LOGGER.debug("entering generateHistory with parameters " + menu);

        final String query = "INSERT INTO MenuCategoryHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM MenuCategoryHistory Menu ID = ?) " +
                "FROM Menu WHERE ID = ?)";

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
}
