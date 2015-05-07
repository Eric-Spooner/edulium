package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.MenuCategoryDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Calendar;
import java.util.List;

/**
 * H2 Database Implementation of the MenuCategoryDAO interface
 */
@Repository
class MenuCategoryDAOImpl implements MenuCategoryDAO {
    @Autowired
    private DataSource dataSource;

    @Override
    public void create(MenuCategory menuCategory) throws DAOException {
        assert(menuCategory != null);

        final String query = "INSERT INTO MenuCategory (name) VALUES (?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, menuCategory.getName());
            stmt.executeUpdate();

            try (ResultSet key = stmt.getGeneratedKeys()) {
                key.next();
                menuCategory.setIdentity(key.getLong(1));
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        generateHistory(menuCategory.getIdentity());
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

    private void generateHistory(long identity) throws DAOException {
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
    }
}
