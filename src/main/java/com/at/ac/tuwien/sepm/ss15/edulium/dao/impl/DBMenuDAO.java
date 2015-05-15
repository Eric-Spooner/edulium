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
            stmt.setString(2, menu.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("inserting menu into database failed", e);
            throw new DAOException("inserting menu into database failed", e);
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
}
