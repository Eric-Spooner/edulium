package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * H2 Database Implementation of the IntermittentSaleDAO interface
 */
class DBIntermittentSaleDAO extends DBAbstractSaleDAO<IntermittentSale> {
    private static final Logger LOGGER = LogManager.getLogger(DBIntermittentSaleDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private Validator<IntermittentSale> validator;

    @Override
    public void create(IntermittentSale intermittentSale) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + intermittentSale);

        validator.validateForCreate(intermittentSale);

        super.create(intermittentSale);

        //Save IntermittentSale
        final String query = "INSERT INTO IntermittentSale (ID, monday, tuesday, wednesday, thursday, friday, saturday, sunday, fromDayTime, duration, enabled) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, intermittentSale.getIdentity());

            int index = 2;
            for(DayOfWeek day : DayOfWeek.values()) {
                stmt.setBoolean(index++, intermittentSale.getDaysOfSale().contains(day));
            }

            stmt.setTime(9, Time.valueOf(intermittentSale.getFromDayTime()));
            stmt.setLong(10, intermittentSale.getDuration().toMillis());
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

        super.update(intermittentSale);

        //Save IntermittentSale
        final String query = "UPDATE IntermittentSale SET monday = ?, tuesday = ?, wednesday = ?, thursday = ?, friday = ?, " +
                "saturday = ?, sunday = ?, fromDayTime = ?, duration = ?, enabled = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            int index = 1;
            for(DayOfWeek day : DayOfWeek.values()) {
                stmt.setBoolean(index++, intermittentSale.getDaysOfSale().contains(day));
            }
            stmt.setTime(8, Time.valueOf(intermittentSale.getFromDayTime()));
            stmt.setLong(9, intermittentSale.getDuration().toMillis());
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

        super.delete(intermittentSale);

        generateHistory(intermittentSale);
    }

    @Override
    public List<IntermittentSale> find(IntermittentSale intermittentSale) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + intermittentSale);

        if (intermittentSale == null) {
            return new ArrayList<>();
        }

        final List<IntermittentSale> intermittentSales = new ArrayList<>();

        if (intermittentSale.getEntries() == null) {  // query without entries - no SaleAssoc join needed :)
            final String query = "SELECT * FROM IntermittentSale NATURAL JOIN Sale" +
                    " WHERE Sale.ID = ISNULL(?, Sale.ID)" +
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
                    " AND name = ISNULL(?, name) " +
                    " AND deleted = false";

            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
                stmt.setObject(1, intermittentSale.getIdentity());

                int index = 2;
                for(DayOfWeek day : DayOfWeek.values()) {
                    stmt.setObject(index++, intermittentSale.getDaysOfSale() == null ? null : intermittentSale.getDaysOfSale().contains(day));
                }

                stmt.setObject(9, intermittentSale.getFromDayTime() == null ? null : Time.valueOf(intermittentSale.getFromDayTime()));
                stmt.setObject(10, intermittentSale.getDuration() != null ? intermittentSale.getDuration().toMillis() : null);
                stmt.setObject(11, intermittentSale.getEnabled());
                stmt.setObject(12, intermittentSale.getName());

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    IntermittentSale sale = intermittentSaleFromResultSet(result);
                    try {
                        saleFromResultSet(sale, result);
                    } catch (ValidationException e) {
                        LOGGER.warn("parsing the result '" + result + "' failed", e);
                    }
                    intermittentSales.add(sale);
                }
            } catch (SQLException e) {
                LOGGER.error("Searching for intermittentSales failed", e);
                throw new DAOException("Searching for intermittentSales failed", e);
            }

        } else { // more complex query with table - SaleAssoc join required

            // sadly we have to provide our own list of pairs - fake it with a list of question marks in the prepared stmt
            final String entries = intermittentSale.getEntries().stream().map(t -> "?").collect(Collectors.joining(", "));

            final String query = "SELECT * FROM IntermittentSale NATURAL JOIN Sale" +
                    " WHERE Sale.ID = ISNULL(?, Sale.ID)" +
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
                    " AND name = ISNULL(?, name) " +
                    " AND deleted = false" +
                    " AND EXISTS (SELECT 1 FROM SaleAssoc " +
                    " WHERE sale_ID = Sale.ID AND menuEntry_ID IN (" + entries + ") AND disabled = false)";

            try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {

                int index = 1;
                stmt.setObject(index++, intermittentSale.getIdentity());
                for(DayOfWeek day : DayOfWeek.values()) {
                    stmt.setObject(index++, intermittentSale.getDaysOfSale() == null ? null : intermittentSale.getDaysOfSale().contains(day));
                }

                stmt.setObject(index++, intermittentSale.getFromDayTime() == null ? null : Time.valueOf(intermittentSale.getFromDayTime()));
                stmt.setObject(index++, intermittentSale.getDuration() != null ? intermittentSale.getDuration().toMillis() : null);
                stmt.setObject(index++, intermittentSale.getEnabled());
                stmt.setObject(index++, intermittentSale.getName());

                // fill entries
                for (MenuEntry entry : intermittentSale.getEntries()) {
                    stmt.setLong(index++, entry.getIdentity());
                }

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    IntermittentSale sale = intermittentSaleFromResultSet(result);
                    try {
                        saleFromResultSet(sale, result);
                    } catch (ValidationException e) {
                        LOGGER.warn("parsing the result '" + result + "' failed", e);
                    }
                    intermittentSales.add(sale);
                }
            } catch (SQLException e) {
                LOGGER.error("Searching for intermittentSales failed", e);
                throw new DAOException("Searching for intermittentSales failed", e);
            }
        }

        return intermittentSales;
    }

    @Override
    public List<IntermittentSale> getAll() throws DAOException {
        LOGGER.debug("Entering getAll");

        final String query = "SELECT * FROM IntermittentSale NATURAL JOIN Sale WHERE deleted = false";

        final List<IntermittentSale> intermittentSales = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                IntermittentSale sale = intermittentSaleFromResultSet(result);
                try {
                    saleFromResultSet(sale, result);
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + result + "' failed", e);
                }
                intermittentSales.add(sale);
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

        final String query = "SELECT * FROM IntermittentSaleHistory JOIN SaleHistory" +
                " ON IntermittentSaleHistory.ID = SaleHistory.ID AND" +
                " IntermittentSaleHistory.changeNr = SaleHistory.changeNr" +
                " WHERE SaleHistory.ID = ? ORDER BY changeNr";

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

        final String query = "SELECT * FROM IntermittentSale NATURAL JOIN Sale" +
                " WHERE Sale.ID IN (" +
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
                IntermittentSale intermittentSale = intermittentSaleFromResultSet(result);
                try {
                    saleFromResultSet(intermittentSale, result);
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + result + "' failed", e);
                }
                populatedIntermittentSales.add(intermittentSale);
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
                "(SELECT *, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM IntermittentSaleHistory WHERE ID = ?) " +
                "FROM IntermittentSale WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, intermittentSale.getIdentity());          // dataset id
            stmt.setLong(2, intermittentSale.getIdentity());          // dataset id

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
        intermittentSale.setIdentity(result.getLong("ID"));

        HashSet<DayOfWeek> saleDays = new HashSet<>();
        for(DayOfWeek day : DayOfWeek.values()) {
            if(result.getBoolean(day.toString().toLowerCase())) {
                saleDays.add(day);
            }
        }

        intermittentSale.setDaysOfSale(saleDays);
        intermittentSale.setFromDayTime(result.getTime("fromDayTime").toLocalTime());
        intermittentSale.setDuration(Duration.ofMillis(result.getLong("duration")));
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
    private History<IntermittentSale> historyFromResultSet(ResultSet result) throws DAOException, SQLException, ValidationException {
        // create history entry
        History<IntermittentSale> historyEntry = new History<>();
        historyEntry.setData(intermittentSaleFromResultSet(result));

        saleHistoryFromResultSet(historyEntry, result);

        return historyEntry;
    }
}