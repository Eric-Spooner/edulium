package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * H2 Database Implementation of the TaxRate DAO interface
 */
@PreAuthorize("isAuthenticated()")
class DBTaxRateDAO implements DAO<TaxRate> {
    private static final Logger LOGGER = LogManager.getLogger(DBTaxRateDAO.class);

    @Resource(name = "dataSource")
    private DataSource dataSource;
    @Resource(name = "userDAO")
    private DAO<User> userDAO;
    @Resource(name = "taxRateValidator")
    private Validator<TaxRate> validator;

    @Override
    public void create(TaxRate taxRate) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + taxRate);

        validator.validateForCreate(taxRate);

        final String query = "INSERT INTO TaxRate (taxRateValue) VALUES (?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setBigDecimal(1, taxRate.getValue());

            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                taxRate.setIdentity(key.getLong(1));
            }
        } catch (SQLException e) {
            LOGGER.error("Inserting tax rate into database failed", e);
            throw new DAOException("Inserting tax rate into database failed", e);
        }

        generateHistory(taxRate);
    }

    @Override
    public void update(TaxRate taxRate) throws DAOException, ValidationException {
        LOGGER.debug("Entering update with parameters: " + taxRate);

        validator.validateForUpdate(taxRate);

        final String query = "UPDATE TaxRate SET taxRateValue = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setBigDecimal(1, taxRate.getValue());
            stmt.setLong(2, taxRate.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Updating tax rate in database failed, tax rate not found");
                throw new DAOException("Updating tax rate in database failed, tax rate not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Updating tax rate in database failed", e);
            throw new DAOException("Updating tax rate in database failed", e);
        }

        generateHistory(taxRate);
    }

    @Override
    public void delete(TaxRate taxRate) throws DAOException, ValidationException {
        LOGGER.debug("Entering delete with parameters: " + taxRate);

        validator.validateForDelete(taxRate);

        final String query = "UPDATE TaxRate SET deleted = true WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, taxRate.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Deleting tax rate from database failed, tax rate not found");
                throw new DAOException("Deleting tax rate from database failed, tax rate not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Deleting tax rate from database failed", e);
            throw new DAOException("Deleting tax rate from database failed", e);
        }

        generateHistory(taxRate);
    }

    @Override
    public List<TaxRate> find(TaxRate taxRate) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + taxRate);

        if (taxRate == null) {
            return new ArrayList<>();
        }

        final String query = "SELECT * FROM TaxRate WHERE ID = ISNULL(?, ID) AND taxRateValue = ISNULL(?, taxRateValue) " +
                             "AND deleted = false";

        final List<TaxRate> taxRates = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, taxRate.getIdentity());
            stmt.setObject(2, taxRate.getValue());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                taxRates.add(taxRateFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for tax rates failed", e);
            throw new DAOException("Searching for tax rates failed", e);
        }

        return taxRates;
    }

    @Override
    public List<TaxRate> getAll() throws DAOException {
        LOGGER.debug("Entering getAll");

        final String query = "SELECT * FROM TaxRate WHERE deleted = false";

        final List<TaxRate> taxRates = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                taxRates.add(taxRateFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Searching for all tax rates failed", e);
            throw new DAOException("Searching for all tax rates failed", e);
        }

        return taxRates;
    }

    @Override
    public List<History<TaxRate>> getHistory(TaxRate taxRate) throws DAOException, ValidationException {
        LOGGER.debug("Entering getHistory with parameters: " + taxRate);

        validator.validateIdentity(taxRate);

        final String query = "SELECT * FROM TaxRateHistory WHERE ID = ? ORDER BY changeNr";

        List<History<TaxRate>> taxRates = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, taxRate.getIdentity());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                taxRates.add(historyFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("retrieving history for tax rate failed", e);
            throw new DAOException("retrieving history for tax rate failed", e);
        }

        return taxRates;
    }

    @Override
    public List<TaxRate> populate(List<TaxRate> taxRates) throws DAOException, ValidationException {
        LOGGER.debug("Entering populate with parameters: " + taxRates);

        if (taxRates == null || taxRates.isEmpty()) {
            return new ArrayList<>();
        }

        for (TaxRate taxRate : taxRates) {
            validator.validateIdentity(taxRate);
        }

        final String query = "SELECT * FROM TaxRate WHERE ID IN (" +
                taxRates.stream().map(u -> "?").collect(Collectors.joining(", ")) + ")"; // fake a list of identities

        final List<TaxRate> populatedTaxRates = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            int index = 1;

            // fill identity list
            for (TaxRate taxRate : taxRates) {
                stmt.setLong(index++, taxRate.getIdentity());
            }

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                populatedTaxRates.add(taxRateFromResultSet(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Populating tax rates failed", e);
            throw new DAOException("Populating tax rates failed", e);
        }

        return populatedTaxRates;
    }

    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed the changes
     * @param taxRate updated dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateHistory(TaxRate taxRate) throws DAOException {
        LOGGER.debug("Entering generateHistory with parameters: " + taxRate);

        final String query = "INSERT INTO TaxRateHistory " +
                             "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                             "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM TaxRateHistory WHERE ID = ?) " +
                             "FROM TaxRate WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, taxRate.getIdentity()); // dataset id
            stmt.setLong(3, taxRate.getIdentity()); // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history for tax rate failed", e);
            throw new DAOException("generating history for tax rate failed", e);
        }
    }

    /**
     * converts the database query output into a TaxRate object
     * @param result database output
     * @return TaxRate object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     */
    private TaxRate taxRateFromResultSet(ResultSet result) throws SQLException {
        TaxRate taxRate = new TaxRate();
        taxRate.setIdentity(result.getLong("ID"));
        taxRate.setValue(result.getBigDecimal("taxRateValue"));
        return taxRate;
    }

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the user ocurred
     */
    private History<TaxRate> historyFromResultSet(ResultSet result) throws DAOException, ValidationException, SQLException {
        // get user
        List<User> storedUsers = userDAO.populate(Arrays.asList(User.withIdentity(result.getString("changeUser"))));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        // create history entry
        History<TaxRate> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(taxRateFromResultSet(result));

        return historyEntry;
    }
}
