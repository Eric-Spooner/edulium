package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Database implementation of the DAO interface for Invoice objects
 */
class DBInvoiceDAO implements DAO<Invoice> {

    private static final Logger LOGGER = LogManager.getLogger(DBInvoiceDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Validator<Invoice> invoiceValidator;
    @Autowired
    private DAO<User> userDAO;

    /**
     * Writes the invoice object into the database and sets the identity
     * parameter that was generated there
     */
    @Override
    public void create(Invoice invoice) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + invoice);

        invoiceValidator.validateForCreate(invoice);

        final String query = "INSERT INTO Invoice (invoiceTime, brutto, user_ID) " +
                "VALUES (?, ?, ?);";
        try (PreparedStatement stmt = dataSource.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setObject(1, invoice.getTime());
            stmt.setBigDecimal(2, invoice.getGross());
            stmt.setString(3, invoice.getCreator().getIdentity());
            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                invoice.setIdentity(key.getLong(1));
            }
            key.close();
        } catch (SQLException e) {
            LOGGER.error("Failed to insert invoice entry into database", e);
            throw new DAOException("Failed to insert invoice entry into database", e);
        }
    }

    /**
     * Updates the invoice object in the database
     */
    @Override
    public void update(Invoice invoice) throws DAOException, ValidationException {
        LOGGER.debug("Entering update with parameters: " + invoice);

        invoiceValidator.validateForUpdate(invoice);

        final String query = "UPDATE Invoice SET invoiceTime = ?, brutto = ?, user_ID = ? " +
                "WHERE id = ?;";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, invoice.getTime());
            stmt.setBigDecimal(2, invoice.getGross());
            stmt.setString(3, invoice.getCreator().getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Failed to update invoice entry in database, dataset not found");
                throw new DAOException("Failed to update invoice entry in database, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update invoice entry in database", e);
            throw new DAOException("Failed to update invoice entry in database", e);
        }
    }

    /**
     * Removes the invoice object from the database
     */
    @Override
    public void delete(Invoice invoice) throws DAOException, ValidationException {
        LOGGER.debug("Entering delete with parameters: " + invoice);

        invoiceValidator.validateForDelete(invoice);

        final String query = "UPDATE Invoice SET canceled = TRUE WHERE ID = ?;";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, invoice.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Failed to delete invoice entry from database, dataset not found");
                throw new DAOException("Failed to delete invoice entry from database, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to delete invoice entry from database", e);
            throw new DAOException("Failed to delete invoice entry from database", e);
        }
    }

    /**
     * Finds all invoice objects in the database which match the
     * parameters of the provided invoice object
     */
    @Override
    public List<Invoice> find(Invoice invoice) throws DAOException {
        // TODO: Implement after writing the tests
        return null;
    }

    /**
     * Retrieves all invoice entries from database
     */
    @Override
    public List<Invoice> getAll() throws DAOException {
        LOGGER.debug("Entering getAll");

        final List<Invoice> results = new ArrayList<>();

        final String query = "SELECT * FROM Invoice WHERE canceled = FALSE";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(parseResult(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to retreive all invoice entries from the database", e);
            throw new DAOException("Failed to retreive all invoice entries from the database", e);
        }

        return results;
    }

    /**
     * Parses a result set in an object of type invoice
     * @param rs The database output
     * @return The parsed object of type invoice
     * @throws DAOException If an error, while retrieving the invoice data, occurs
     * @throws SQLException If an error, while accessing database results, occurs
     */
    private Invoice parseResult(ResultSet rs) throws DAOException, SQLException {
        List<User> creator = userDAO.find(User.withIdentity(rs.getString("user_ID")));
        if (creator.size() != 1) {
            LOGGER.error("Retrieving creator failed");
            throw new DAOException("Retrieving creator failed");
        }

        Invoice invoice = new Invoice();
        invoice.setCreator(creator.get(0));
        invoice.setIdentity(rs.getLong("ID"));
        invoice.setTime((LocalDateTime) rs.getObject("invoiceTime"));
        invoice.setGross(rs.getBigDecimal("brutto"));

        return invoice;
    }


    /**
     * Gets the history of changes for the invoice object from
     * the database
     */
    @Override
    public List<History<Invoice>> getHistory(Invoice object) throws DAOException, ValidationException {
        // TODO: Implement after writing the tests
        return null;
    }
}
