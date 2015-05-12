package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 Database Implementation of the MenuCategoryDAO interface
 */
public class TableDAOImpl implements DAO<Table> {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private Validator<Table> validator;

    /**
     * writes the object into the database and sets the identity parameter of
     * table
     * @param table object to store
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public void create(Table table) throws DAOException, ValidationException {
        assert(table != null);

        validator.validateForCreate(table);

        final String query = "INSERT INTO RestaurantTable (section_ID, seats, tableRow, tableColumn, user_ID, number) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {

            stmt.setLong(1, table.getSection().getIdentity());
            stmt.setInt(2, table.getSeats());
            stmt.setInt(3, table.getRow());
            stmt.setInt(4, table.getColumn());
            stmt.setString(5, table.getUser().getIdentity());
            stmt.setLong(6, table.getNumber());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        //generateHistory(table.getNumber()); TODO
    }

    /**
     * updates the object in the database
     * @param table object to update
     * @throws DAOException if an error accessing the database ocurred or if the
     *         dataset was not found in the database
     */
    @Override
    public void update(Table table) throws DAOException, ValidationException {
        assert(table != null);

        validator.validateForUpdate(table);

        final String query = "UPDATE RestaurantTable SET " +
                "section_ID = ?, seats = ?, tableRow = ?, tableColumn = ?, user_ID = ? WHERE number = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, table.getSection().getIdentity());
            stmt.setInt(2, table.getSeats());
            stmt.setInt(3, table.getRow());
            stmt.setInt(4, table.getColumn());
            stmt.setString(5, table.getUser().getIdentity());
            stmt.setLong(6, table.getNumber());

            if (stmt.executeUpdate() == 0) {
                throw new DAOException("updating failed: dataset not found");
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        //generateHistory(table.getNumber()); TODO
    }

    /**
     * removes the object from the database
     * @param table object to remove
     * @throws DAOException if an error accessing the database occurred or if
     *         the dataset was not found in the database
     */
    @Override
    public void delete(Table table) throws DAOException, ValidationException {
        assert(table != null);

        validator.validateForDelete(table);

        final String query = "UPDATE RestaurantTable SET deleted = true WHERE number = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, table.getNumber());
            if (stmt.executeUpdate() == 0) {
                throw new DAOException("delete failed: dataset not found");
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        //generateHistory(table.getNumber()); TODO
    }

    /**
     * returns all objects from the database which parameters match the
     * parameters of the object table
     * all parameters with value NULL will not be used for matching
     * @param table object used for matching
     * @return returns a list of objects from the database which match the criteria
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public List<Table> find(Table table) throws DAOException {
        assert(table != null);
        String query = "SELECT * FROM RestaurantTable WHERE number = ISNULL(?, number) " +
                "AND seats = ISNULL(?, seats) AND section_ID = ISNULL(?, section_ID)" +
                "AND tableRow = ISNULL(?, tableRow) AND tableColumn = ISNULL(?, tableColumn)" +
                "AND user_ID = ISNULL(?, user_ID) AND deleted = false";

        final List<Table> objects = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, table.getNumber());
            stmt.setInt(2, table.getSeats());
            stmt.setLong(3, table.getSection().getIdentity());
            stmt.setInt(4, table.getRow());
            stmt.setInt(5, table.getColumn());
            stmt.setString(6, table.getUser().getIdentity());
            stmt.execute();

            try (ResultSet result = stmt.getResultSet()) {
                while (result.next()) {
                    objects.add(parseResult(result));
                }
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return objects;
    }

    /**
     * @return returns all objects in the database
     * @throws DAOException if an error accessing the database occurred
     */
    @Override
    public List<Table> getAll() throws DAOException {
        final String query = "SELECT * FROM RestaurantTable WHERE deleted = false";
        final List<Table> objects = new ArrayList<>();

        try (Statement stmt = dataSource.getConnection().createStatement()) {
            stmt.execute(query);

            try (ResultSet result = stmt.getResultSet()) {
                while (result.next()) {
                    objects.add(parseResult(result));
                }
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return objects;
    }

    @Override
    public List<History<Table>> getHistory(Table object) throws DAOException, ValidationException {
        return null;
    }

    /**
     * converts the database query output into a object
     * @param result database output
     * @return Table object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private Table parseResult(ResultSet result) throws SQLException, DAOException {
        DAO sectionDAO = new SectionDAOImpl();
        Section matcherSection = new Section();
        matcherSection.setIdentity(result.getLong(0));
        DAO userDAO = new UserDAOImpl();
        User matcherUser = new User();
        matcherUser.setIdentity(result.getString(6));
        Table table = new Table();
        table.setSection((Section)sectionDAO.find(matcherSection).get(0));
        table.setNumber(result.getLong(2));
        table.setSeats(result.getInt(3));
        table.setRow(result.getInt(4));
        table.setColumn(result.getInt(5));
        table.setUser((User)userDAO.find(matcherUser).get(0));
        return table;
    }
}