package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.OnetimeSale;
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
 * H2 Database Implementation of the OnetimeSaleDAO interface
 */
class DBOnetimeSaleDAO extends DBAbstractSaleDAO<OnetimeSale> {
    private static final Logger LOGGER = LogManager.getLogger(DBOnetimeSaleDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private Validator<OnetimeSale> validator;

    @Override
    public void create(OnetimeSale onetimeSale) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + onetimeSale);

        validator.validateForCreate(onetimeSale);

        super.create(onetimeSale);

        //Create OnetimeSale
        final String query = "INSERT INTO OnetimeSale (sale_ID, fromTime, toTime) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, onetimeSale.getIdentity());
            stmt.setTimestamp(2, Timestamp.valueOf(onetimeSale.getFromTime()));
            stmt.setTimestamp(3, Timestamp.valueOf(onetimeSale.getToTime()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Inserting onetimeSale into database failed", e);
            throw new DAOException("Inserting onetimeSale into database failed", e);
        }

        generateHistory(onetimeSale);
    }

    @Override
    public void update(OnetimeSale onetimeSale) throws DAOException, ValidationException {
        LOGGER.debug("Entering update with parameters: " + onetimeSale);

        validator.validateForUpdate(onetimeSale);

        super.update(onetimeSale);

        //Update OnetimeSale
        final String query = "UPDATE OnetimeSale SET fromTime = ?, toTime = ? WHERE sale_ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(onetimeSale.getFromTime()));
            stmt.setTimestamp(2, Timestamp.valueOf(onetimeSale.getToTime()));
            stmt.setLong(3, onetimeSale.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Updating onetimeSale in database failed, intermittentSale not found");
                throw new DAOException("Updating onetimeSale in database failed, intermittentSale not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Updating onetimeSale in database failed", e);
            throw new DAOException("Updating onetimeSale in database failed", e);
        }

        generateHistory(onetimeSale);
    }

    @Override
    public void delete(OnetimeSale onetimeSale) throws DAOException, ValidationException {
        LOGGER.debug("Entering delete with parameters: " + onetimeSale);

        validator.validateForDelete(onetimeSale);

        super.delete(onetimeSale);

        generateHistory(onetimeSale);
    }

    @Override
    public List<OnetimeSale> find(OnetimeSale onetimeSale) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + onetimeSale);

        if (onetimeSale == null) {
            return new ArrayList<>();
        }

        final List<OnetimeSale> sales = new ArrayList<>();

        if (onetimeSale.getEntries() == null) {  // query without entries - no SaleAssoc join needed :)
            final String query = "SELECT * FROM OnetimeSale JOIN Sale" +
                    " ON OnetimeSale.sale_ID = Sale.ID" +
                    " WHERE sale_ID = ISNULL(?, sale_ID)" +
                    " AND fromTime = ISNULL(?, fromTime)"+
                    " AND toTime = ISNULL(?, toTime)"+
                    " AND name = ISNULL(?, name) " +
                    " AND deleted = false";

            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
                stmt.setObject(1, onetimeSale.getIdentity());
                stmt.setObject(2, onetimeSale.getFromTime()==null ? null : Timestamp.valueOf(onetimeSale.getFromTime()));
                stmt.setObject(3, onetimeSale.getToTime()==null ? null : Timestamp.valueOf(onetimeSale.getToTime()));
                stmt.setObject(4, onetimeSale.getName());

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    OnetimeSale saleFromResult = onetimeSaleFromResultSet(result);
                    try {
                        saleFromResultSet(saleFromResult, result);
                    } catch (ValidationException e) {
                        LOGGER.warn("parsing the result '" + result + "' failed", e);
                    }
                    sales.add(saleFromResult);
                }
            } catch (SQLException e) {
                LOGGER.error("Searching for OneTimeSales failed", e);
                throw new DAOException("Searching for OneTimeSales failed", e);
            }

        } else { // more complex query with table - SaleAssoc join required

            // sadly we have to provide our own list of pairs - fake it with a list of question marks in the prepared stmt
            final String entries = onetimeSale.getEntries().stream().map(t -> "?").collect(Collectors.joining(", "));

            final String query = "SELECT * FROM OnetimeSale JOIN Sale" +
                    " ON OnetimeSale.sale_ID = Sale.ID" +
                    " WHERE sale_ID = ISNULL(?, sale_ID)" +
                    " AND fromTime = ISNULL(?, fromTime)"+
                    " AND toTime = ISNULL(?, toTime)"+
                    " AND name = ISNULL(?, name) " +
                    " AND deleted = false" +
                    " AND EXISTS (SELECT 1 FROM SaleAssoc " +
                    " WHERE SaleAssoc.sale_ID = OnetimeSale.sale_ID AND menuEntry_ID IN (" + entries + ") AND disabled = false)";

            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
                int index = 1;
                stmt.setObject(index++, onetimeSale.getIdentity());
                stmt.setObject(index++, onetimeSale.getFromTime()==null ? null : Timestamp.valueOf(onetimeSale.getFromTime()));
                stmt.setObject(index++, onetimeSale.getToTime()==null ? null : Timestamp.valueOf(onetimeSale.getToTime()));
                stmt.setObject(index++, onetimeSale.getName());


                // fill entries
                for (MenuEntry entry : onetimeSale.getEntries()) {
                    stmt.setLong(index++, entry.getIdentity());
                }

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    OnetimeSale sale = onetimeSaleFromResultSet(result);
                    try {
                        saleFromResultSet(sale, result);
                    } catch (ValidationException e) {
                        LOGGER.warn("parsing the result '" + result + "' failed", e);
                    }
                    sales.add(sale);
                }
            } catch (SQLException e) {
                LOGGER.error("Searching for intermittentSales failed", e);
                throw new DAOException("Searching for intermittentSales failed", e);
            }
        }

        return sales;
    }

    @Override
    public List<OnetimeSale> getAll() throws DAOException {
        LOGGER.debug("Entering getAll");

        final String query = "SELECT * FROM OnetimeSale JOIN Sale" +
                " ON OnetimeSale.sale_ID = Sale.ID" +
                " WHERE EXISTS (SELECT * FROM Sale WHERE OnetimeSale.sale_ID = Sale.ID AND deleted = false)";

        final List<OnetimeSale> onetimeSales = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                OnetimeSale onetimeSaleFromResult = onetimeSaleFromResultSet(result);
                try {
                    saleFromResultSet(onetimeSaleFromResult, result);
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + result + "' failed", e);
                }
                onetimeSales.add(onetimeSaleFromResult);
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for all onetimeSales failed", e);
            throw new DAOException("Searching for all onetimeSales failed", e);
        }

        return onetimeSales;
    }

    @Override
    public List<History<OnetimeSale>> getHistory(OnetimeSale onetimeSale) throws DAOException, ValidationException {
        LOGGER.debug("Entering getHistory with parameters: " + onetimeSale);

        validator.validateIdentity(onetimeSale);

        final String query = "SELECT * FROM OnetimeSaleHistory WHERE sale_ID = ? ORDER BY changeNr";

        List<History<OnetimeSale>> history = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, onetimeSale.getIdentity());

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
    public List<OnetimeSale> populate(List<OnetimeSale> onetimeSales) throws DAOException, ValidationException {
        LOGGER.debug("Entering populate with parameters: " + onetimeSales);

        if (onetimeSales == null || onetimeSales.isEmpty()) {
            return new ArrayList<>();
        }

        for (OnetimeSale onetimeSale : onetimeSales) {
            validator.validateIdentity(onetimeSale);
        }

        final String query = "SELECT * FROM OnetimeSale JOIN Sale" +
                " ON OnetimeSale.sale_ID = Sale.ID" +
                " WHERE sale_ID IN (" +
                onetimeSales.stream().map(u -> "?").collect(Collectors.joining(", ")) + ")"; // fake a list of identities

        final List<OnetimeSale> populatedOnetimeSales = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            int index = 1;

            // fill identity list
            for (OnetimeSale onetimeSale : onetimeSales) {
                stmt.setLong(index++, onetimeSale.getIdentity());
            }

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                OnetimeSale onetimeSaleFromResult = onetimeSaleFromResultSet(result);
                try {
                    saleFromResultSet(onetimeSaleFromResult, result);
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + result + "' failed", e);
                }
                populatedOnetimeSales.add(onetimeSaleFromResult);
            }
        } catch (SQLException e) {
            LOGGER.error("Populating onetimeSales failed", e);
            throw new DAOException("Populating onetimeSales failed", e);
        }

        return populatedOnetimeSales;
    }

    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed the changes
     * @param onetimeSale updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(OnetimeSale onetimeSale) throws DAOException {
        LOGGER.debug("Entering generateHistory with parameters: " + onetimeSale);

        final String query = "INSERT INTO OnetimeSaleHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM OnetimeSaleHistory WHERE sale_ID = ?) " +
                "FROM OnetimeSale WHERE sale_ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, onetimeSale.getIdentity());          // dataset id
            stmt.setLong(3, onetimeSale.getIdentity());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }

    /**
     * converts the database query output into a onetimeSale object
     * @param result database output
     * @return OnetimeSale object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private OnetimeSale onetimeSaleFromResultSet(ResultSet result) throws SQLException {
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(result.getLong("sale_ID"));
        onetimeSale.setFromTime(result.getTimestamp("fromTime").toLocalDateTime());
        onetimeSale.setToTime(result.getTimestamp("toTime").toLocalDateTime());
        return onetimeSale;
    }

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the sale ocurred
     */
    private History<OnetimeSale> historyFromResultSet(ResultSet result) throws DAOException, SQLException {
        // get user
        List<User> storedUsers = userDAO.find(User.withIdentity(result.getString("changeUser")));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        // create history entry
        History<OnetimeSale> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(onetimeSaleFromResultSet(result));

        setSaleHistoryParameters(historyEntry);

        return historyEntry;
    }
}