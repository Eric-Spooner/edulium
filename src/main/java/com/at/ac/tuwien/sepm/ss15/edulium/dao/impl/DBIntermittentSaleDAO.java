package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.IntermittentSale;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * H2 Database Implementation of the IntermittentSaleDAO interface
 */
class DBIntermittentSaleDAO implements DAO<IntermittentSale> {
    private static final Logger LOGGER = LogManager.getLogger(DBIntermittentSaleDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private Validator<IntermittentSale> validator;


    @Override
    public void create(IntermittentSale intermittentSale) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + intermittentSale);

        validator.validateForCreate(intermittentSale);

        final String query = "INSERT INTO IntermittentSale (sale_ID, monday, tuesday, wednesday, thursday, friday, saturday, sunday, fromDayTime, duration, enabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, intermittentSale.getIdentity());
            stmt.setBoolean(2, intermittentSale.getMonday());
            stmt.setBoolean(3, intermittentSale.getTuesday());
            stmt.setBoolean(4, intermittentSale.getWednesday());
            stmt.setBoolean(5, intermittentSale.getThursday());
            stmt.setBoolean(6, intermittentSale.getFriday());
            stmt.setBoolean(7, intermittentSale.getSaturday());
            stmt.setBoolean(8, intermittentSale.getSunday());
            stmt.setTimestamp(9, Timestamp.valueOf(intermittentSale.getFromDayTime()));
            stmt.setInt(10, intermittentSale.getDuration());
            stmt.setBoolean(11, intermittentSale.getEnabled());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Inserting intermittentSale into database failed", e);
            throw new DAOException("Inserting intermittentSale into database failed", e);
        }

        generateHistory(intermittentSale);
    }

    @Override
    public void update(IntermittentSale intermittentSale) throws DAOException, ValidationException {
        LOGGER.debug("Entering update with parameters: " + intermittentSale);

        validator.validateForUpdate(intermittentSale);

        final String query = "UPDATE IntermittentSale SET monday = ?, tuesday = ?, wednesday = ?, thursday = ?, friday = ?, saturday = ?, sunday = ?, fromDayTime = ?, duration = ?, enabled = ? WHERE sale_ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setBoolean(1, intermittentSale.getMonday());
            stmt.setBoolean(2, intermittentSale.getTuesday());
            stmt.setBoolean(3, intermittentSale.getWednesday());
            stmt.setBoolean(4, intermittentSale.getThursday());
            stmt.setBoolean(5, intermittentSale.getFriday());
            stmt.setBoolean(6, intermittentSale.getSaturday());
            stmt.setBoolean(7, intermittentSale.getSunday());
            stmt.setTimestamp(8, Timestamp.valueOf(intermittentSale.getFromDayTime()));
            stmt.setInt(9, intermittentSale.getDuration());
            stmt.setBoolean(10, intermittentSale.getEnabled());
            stmt.setLong(11, intermittentSale.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Updating intermittentSale in database failed, intermittentSale not found");
                throw new DAOException("Updating intermittentSale in database failed, intermittentSale not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Updating intermittentSale in database failed", e);
            throw new DAOException("Updating intermittentSale in database failed", e);
        }

        generateHistory(intermittentSale);
    }

    @Override
    public void delete(IntermittentSale intermittentSale) throws DAOException, ValidationException {
        LOGGER.debug("Entering delete with parameters: " + intermittentSale);

        validator.validateForDelete(intermittentSale);

        final String query = "UPDATE IntermittentSale SET deleted = true WHERE sale_ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, intermittentSale.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Deleting intermittentSale from database failed, intermittentSale not found");
                throw new DAOException("Deleting intermittentSale from database failed, intermittentSale not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Deleting intermittentSale from database failed", e);
            throw new DAOException("Deleting intermittentSale from database failed", e);
        }

        generateHistory(intermittentSale);
    }

    @Override
    public List<IntermittentSale> find(IntermittentSale intermittentSale) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + intermittentSale);

        if (intermittentSale == null) {
            return new ArrayList<>();
        }

        final String query = "SELECT * FROM IntermittentSale WHERE sale_ID = ISNULL(?, sale_ID)" +
                " AND monday = ISNULL(?, monday)"+
                " AND tuesday = ISNULL(?, tuesday)"+
                " AND wednesday = ISNULL(?, wednesday)"+
                " AND thursday = ISNULL(?, thursday)"+
                " AND friday = ISNULL(?, friday)"+
                " AND saturday = ISNULL(?, saturday)"+
                " AND sunday = ISNULL(?, sunday)"+
                " AND fromDayTime = ISNULL(?, fromDayTime)"+
                " AND duration = ISNULL(?, duration)"+
                " AND enabled = ISNULL(?, enabled)"+
                " AND deleted = false";

        final List<IntermittentSale> intermittentSales = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, intermittentSale.getIdentity());
            stmt.setObject(2, intermittentSale.getMonday());
            stmt.setObject(3, intermittentSale.getMonday());
            stmt.setObject(4, intermittentSale.getMonday());
            stmt.setObject(5, intermittentSale.getMonday());
            stmt.setObject(6, intermittentSale.getMonday());
            stmt.setObject(7, intermittentSale.getMonday());
            stmt.setObject(8, intermittentSale.getSunday());
            stmt.setObject(9, intermittentSale.getFromDayTime());
            stmt.setObject(10, intermittentSale.getDuration());
            stmt.setObject(11, intermittentSale.getEnabled());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                intermittentSales.add(intermittentSaleFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for intermittentSales failed", e);
            throw new DAOException("Searching for intermittentSales failed", e);
        }

        return intermittentSales;
    }

    @Override
    public List<IntermittentSale> getAll() throws DAOException {
        LOGGER.debug("Entering getAll");

        final String query = "SELECT * FROM IntermittentSale WHERE deleted = false";

        final List<IntermittentSale> intermittentSales = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                intermittentSales.add(intermittentSaleFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for all intermittentSales failed", e);
            throw new DAOException("Searching for all intermittentSales failed", e);
        }

        return intermittentSales;
    }

    @Override
    public List<History<IntermittentSale>> getHistory(IntermittentSale intermittentSale) throws DAOException, ValidationException {
        LOGGER.debug("Entering getHistory with parameters: " + intermittentSale);

        validator.validateIdentity(intermittentSale);

        final String query = "SELECT * FROM IntermittentSaleHistory WHERE sale_ID = ? ORDER BY changeNr";

        List<History<IntermittentSale>> history = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, intermittentSale.getIdentity());

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


    @Override
    public List<IntermittentSale> populate(List<IntermittentSale> intermittentSales) throws DAOException, ValidationException {
        LOGGER.debug("Entering populate with parameters: " + intermittentSales);

        if (intermittentSales == null || intermittentSales.isEmpty()) {
            return new ArrayList<>();
        }

        for (IntermittentSale order : intermittentSales) {
            validator.validateIdentity(order);
        }

        final String query = "SELECT * FROM IntermittentSale WHERE ID IN (" +
                intermittentSales.stream().map(u -> "?").collect(Collectors.joining(", ")) + ")"; // fake a list of identities

        final List<IntermittentSale> populatedIntermittentSales = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            int index = 1;

            // fill identity list
            for (IntermittentSale intermittentSale : intermittentSales) {
                stmt.setLong(index++, intermittentSale.getIdentity());
            }

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                populatedIntermittentSales.add(intermittentSaleFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Populating intermittentSales failed", e);
            throw new DAOException("Populating intermittentSales failed", e);
        }

        return populatedIntermittentSales;
    }


    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed the changes
     * @param intermittentSale updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(IntermittentSale intermittentSale) throws DAOException {
        LOGGER.debug("Entering generateHistory with parameters: " + intermittentSale);

        final String query = "INSERT INTO IntermittentSaleHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM IntermittentSaleHistory WHERE sale_ID = ?) " +
                "FROM IntermittentSale WHERE sale_ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, intermittentSale.getIdentity());          // dataset id
            stmt.setLong(3, intermittentSale.getIdentity());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }

    /**
     * converts the database query output into a intermittentSale object
     * @param result database output
     * @return IntermittentSale object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private IntermittentSale intermittentSaleFromResultSet(ResultSet result) throws SQLException {
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(result.getLong("sale_ID"));
        intermittentSale.setMonday(result.getBoolean("monday"));
        intermittentSale.setTuesday(result.getBoolean("tuesday"));
        intermittentSale.setWednesday(result.getBoolean("wednesday"));
        intermittentSale.setThursday(result.getBoolean("thursday"));
        intermittentSale.setFriday(result.getBoolean("friday"));
        intermittentSale.setSaturday(result.getBoolean("saturday"));
        intermittentSale.setSunday(result.getBoolean("sunday"));
        intermittentSale.setFromDayTime(result.getTimestamp("fromDayTime").toLocalDateTime());
        intermittentSale.setDuration(result.getInt("duration"));
        intermittentSale.setEnabled(result.getBoolean("enabled"));
        return intermittentSale;
    }

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the sale ocurred
     */
    private History<IntermittentSale> historyFromResultSet(ResultSet result) throws DAOException, SQLException {
        // get user
        List<User> storedUsers = userDAO.find(User.withIdentity(result.getString("changeUser")));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        // create history entry
        History<IntermittentSale> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(intermittentSaleFromResultSet(result));

        return historyEntry;
    }



/*
    @Override
    public void create(Sale sale) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + sale);

        validator.validateForCreate(sale);

        final String query = "INSERT INTO Sale (ID, name, deleted) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, sale.getIdentity());
            stmt.setString(2, sale.getName());
            stmt.setBoolean(3, false);

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
*/
    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed the changes
     * @param sale updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    /*private void generateHistory(Sale sale) throws DAOException {
        LOGGER.debug("Entering generateHistory with parameters: " + sale);

        final String query = "INSERT INTO SaleHistory " +
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
    }*/

    /**
     * converts the database query output into a sale object
     * @param result database output
     * @return Sale object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    /*private Sale saleFromResultSet(ResultSet result) throws SQLException {
        Sale sale = new Sale();
        sale.setIdentity(result.getLong("ID"));
        sale.setName(result.getString("name"));
        return sale;
    }*/

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the sale ocurred
     */
    /*private History<Sale> historyFromResultSet(ResultSet result) throws DAOException, SQLException {
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
    }*/
}
