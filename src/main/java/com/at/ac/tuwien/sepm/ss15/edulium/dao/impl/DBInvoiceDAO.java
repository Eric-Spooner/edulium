package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
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
                "WHERE id = ?";
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
        // TODO: Implement after writing the tests
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
        // TODO: Implement after writing the tests
        return null;
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
