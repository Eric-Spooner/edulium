package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
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
class DBMenuEntryDAO implements DAO<MenuEntry> {
    private static final Logger LOGGER = LogManager.getLogger(DBMenuEntryDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Validator<MenuEntry> validator;


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
            stmt.setLong(5, menuEntry.getTaxRate().getIdentity());
            stmt.setLong(6, menuEntry.getCategory().getIdentity());
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
    public void update(MenuEntry object) throws DAOException, ValidationException {

    }

    @Override
    public void delete(MenuEntry object) throws DAOException, ValidationException {

    }

    @Override
    public List<MenuEntry> find(MenuEntry object) throws DAOException {
        return null;
    }

    @Override
    public List<MenuEntry> getAll() throws DAOException {
        return null;
    }

    @Override
    public List<History<MenuEntry>> getHistory(MenuEntry object) throws DAOException, ValidationException {
        return null;
    }

    private void generateHistory(MenuEntry menuEntry) throws DAOException {

    }
}
