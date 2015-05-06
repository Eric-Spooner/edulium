package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.MenuCategoryDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * H2 Database Implementation of the MenuCategoryDAO interface
 */
@Repository
class MenuCategoryDAOImpl implements MenuCategoryDAO {
    @Autowired
    private DataSource dataSource;
    private Connection connection;

    public MenuCategoryDAOImpl() throws DAOException {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void create(MenuCategory menuCategory) throws DAOException {
        assert(menuCategory != null);

        try {
            PreparedStatement stmt = connection.prepareStatement("" +
                    "INSERT INTO MenuCategory VALUES (NULL, ?, FALSE);", Statement.RETURN_GENERATED_KEYS);
            try {
                stmt.setString(1, menuCategory.getName());  // name
                stmt.executeUpdate();

                ResultSet key = stmt.getGeneratedKeys();
                try {
                    key.next();
                    menuCategory.setIdentity(key.getLong(1));
                } finally {
                    key.close();
                }

            } finally {
                stmt.close();
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        generateHistory_create(menuCategory);
    }

    @Override
    public void update(MenuCategory menuCategory) throws DAOException {
        assert(menuCategory != null);

    }

    @Override
    public void delete(MenuCategory menuCategory) throws DAOException {
        assert(menuCategory != null);

    }

    @Override
    public List<MenuCategory> find(MenuCategory menuCategory) throws DAOException {
        assert(menuCategory != null);
        return null;
    }

    @Override
    public List<MenuCategory> getAll() throws DAOException {
        return null;
    }

    private void generateHistory_create(MenuCategory menuCategory) throws DAOException {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO MenuCategoryHistory VALUES (?, ?, ?, ?, ?, ?);");
            try {
                stmt.setLong(1, menuCategory.getIdentity());    // dataset id
                stmt.setBoolean(2, false);                      // deleted
                stmt.setString(3, menuCategory.getName());      // name
                stmt.setInt(4, 1);                              // changeNr
                stmt.setTime(5, null);                          // time TODO
                stmt.setInt(6, 0);                              // user TODO

                stmt.executeUpdate();

            } finally {
                stmt.close();
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private void generateHistory_update(MenuCategory menuCategory) throws DAOException {


    }
}
