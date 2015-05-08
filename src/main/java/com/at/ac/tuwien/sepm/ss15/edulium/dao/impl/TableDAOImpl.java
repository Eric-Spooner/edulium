package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.TableDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.TableValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
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
@Repository
class TableDAOImpl implements TableDAO {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private TableValidator validator;

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

        final String query = "INSERT INTO RestaurantTable (section_ID, seats, row, column, user_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, table.getSection_id());
            stmt.setInt(2, table.getSeats());
            stmt.setInt(3, table.getRow());
            stmt.setInt(4, table.getColumn());
            stmt.setLong(5, table.getUser_id());
            stmt.executeUpdate();

            try (ResultSet key = stmt.getGeneratedKeys()) {
                key.next();
                table.setNumber(key.getLong(1));
            }

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
            stmt.setLong(1, table.getSection_id());
            stmt.setInt(2, table.getSeats());
            stmt.setInt(3, table.getRow());
            stmt.setInt(4, table.getColumn());
            stmt.setLong(5, table.getUser_id());
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
            stmt.setLong(3, table.getSection_id());
            stmt.setInt(4, table.getRow());
            stmt.setInt(5, table.getColumn());
            stmt.setLong(6, table.getUser_id());
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

    /**
     * converts the database query output into a object
     * @param result database output
     * @return Table object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private Table parseResult(ResultSet result) throws SQLException {
        Table table = new Table();
        table.setSection_id(result.getLong(1));
        table.setNumber(result.getLong(2));
        table.setSeats(result.getInt(3));
        table.setRow(result.getInt(4));
        table.setColumn(result.getInt(5));
        table.setUser_id(result.getLong(6));
        return table;
    }
}