package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Sale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * H2 Database Implementation of the SaleDAO interface
 */
public class DBSaleDAO implements DAO<Sale> {
    private static final Logger LOGGER = LogManager.getLogger(DBSaleDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private Validator<Sale> validator;


    @Override
    public void create(Sale sale) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + sale);

        validator.validateForCreate(sale);

        final String query = "INSERT INTO Sale (ID, name, deleted) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, sale.getIdentity());
            stmt.setString(2, sale.getName());
            stmt.setString(3, false);

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Inserting sale into database failed", e);
            throw new DAOException("Inserting sale into database failed", e);
        }

        generateHistory(sale);
    }

    @Override
    public void update(Sale sale) throws DAOException, ValidationException {
        LOGGER.debug("Entering update with parameters: " + sale);

        validator.validateForUpdate(sale);

        final String query = "UPDATE Sale SET name = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, sale.getName());
            stmt.setLong(2, sale.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Updating sale in database failed, sale not found");
                throw new DAOException("Updating sale in database failed, sale not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Updating sale in database failed", e);
            throw new DAOException("Updating sale in database failed", e);
        }

        generateHistory(sale);
    }

    @Override
    public void delete(Sale sale) throws DAOException, ValidationException {
        LOGGER.debug("Entering delete with parameters: " + sale);

        validator.validateForDelete(sale);

        final String query = "UPDATE Sale SET deleted = true WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, sale.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Deleting sale from database failed, sale not found");
                throw new DAOException("Deleting sale from database failed, sale not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Deleting sale from database failed", e);
            throw new DAOException("Deleting sale from database failed", e);
        }

        generateHistory(sale);
    }

    @Override
    public List<Sale> find(Sale sale) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + sale);

        if (sale == null) {
            return new ArrayList<>();
        }

        final String query = "SELECT * FROM Sale WHERE ID = ISNULL(?, ID) AND name = ISNULL(?, name) " +
                " AND deleted = false";

        final List<Sale> sales = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, sale.getIdentity());
            stmt.setObject(2, sale.getName());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                sales.add(saleFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for sales failed", e);
            throw new DAOException("Searching for sales failed", e);
        }

        return sales;
    }

    @Override
    public List<Sale> getAll() throws DAOException {
        LOGGER.debug("Entering getAll");

        final String query = "SELECT * FROM Sale WHERE deleted = false";

        final List<Sale> sales = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                sales.add(saleFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for all sales failed", e);
            throw new DAOException("Searching for all sales failed", e);
        }

        return sales;
    }

    @Override
    public List<History<Sale>> getHistory(Sale sale) throws DAOException, ValidationException {
        LOGGER.debug("Entering getHistory with parameters: " + sale);

        validator.validateIdentity(sale);

        final String query = "SELECT * FROM SaleHistory WHERE ID = ? ORDER BY changeNr";

        List<History<Sale>> history = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, sale.getIdentity());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                history.add(historyFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("retrieving history failed", e);
            throw new DAOException("retrieving history failed", e);
        }

        return history;
    }

    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed the changes
     * @param sale updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(Sale sale) throws DAOException {
        LOGGER.debug("Entering generateHistory with parameters: " + sale);

        final String query = "INSERT INTO Sale " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM SaleHistory WHERE ID = ?) " +
                "FROM Sale WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, sale.getIdentity());          // dataset id
            stmt.setLong(3, sale.getIdentity());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }

    /**
     * converts the database query output into a sale object
     * @param result database output
     * @return Sale object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private Sale saleFromResultSet(ResultSet result) throws SQLException {
        Sale sale = new Sale();
        sale.setIdentity(result.getLong("ID"));
        sale.setName(result.getString("name"));
        return sale;
    }

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the sale ocurred
     */
    private History<Sale> historyFromResultSet(ResultSet result) throws DAOException, SQLException {
        // get user
        List<User> storedUsers = userDAO.find(User.withIdentity(result.getString("changeUser")));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        // create history entry
        History<Sale> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(saleFromResultSet(result));

        return historyEntry;
    }
}
