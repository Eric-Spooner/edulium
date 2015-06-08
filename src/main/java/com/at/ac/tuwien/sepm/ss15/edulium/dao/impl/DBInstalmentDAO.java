package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.ImmutableDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Instalment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ImmutableValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database implementation of the DAO interface for Instalment objects
 */
class DBInstalmentDAO implements ImmutableDAO<Instalment> {

    private static final Logger LOGGER = LogManager.getLogger(DBInstalmentDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ImmutableValidator<Instalment> instalmentValidator;

    /**
     * Writes the instalment object into the database and sets the identity
     * parameter that was generated there
     */
    @Override
    public void create(Instalment object) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + object);

        instalmentValidator.validateForCreate(object);

        final String query = "INSERT INTO Instalment (instalmentTime, paymentInfo, type, " +
                "amount, invoice_ID) VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(object.getTime()));
            stmt.setString(2, object.getPaymentInfo());
            stmt.setString(3, object.getType());
            stmt.setBigDecimal(4, object.getAmount());
            stmt.setLong(5, object.getInvoice().getIdentity());
            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                object.setIdentity(key.getLong(1));
            }
            key.close();
        } catch (SQLException e) {
            LOGGER.error("Failed to insert instalment entry into database", e);
            throw new DAOException("Failed to insert instalment entry into database", e);
        }
    }

    @Override
    public List<Instalment> find(Instalment object) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + object);

        if (object == null) {
            return new ArrayList<>();
        }

        final List<Instalment> results = new ArrayList<>();

        final String query = "SELECT * FROM Invoice WHERE " +
                "ID = ISNULL(?, ID) AND " +
                "instalmentTime = ISNULL(?, instalmentTime) AND " +
                "paymentInfo = ISNULL(?, paymentInfo) AND " +
                "type = ISNULL(?, type) AND " +
                "amount = ISNULL(?, amount) AND " +
                "invoice_ID = ISNULL(?, invoice_ID);";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, object.getIdentity());
            stmt.setTimestamp(2, Timestamp.valueOf(object.getTime()));
            stmt.setString(3, object.getPaymentInfo());
            stmt.setString(4, object.getType());
            stmt.setBigDecimal(5, object.getAmount());
            stmt.setLong(6, object.getInvoice() == null ? null : object.getInvoice().getIdentity());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                try {
                    results.add(parseResult(rs));
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + rs + "' failed", e);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to retrieve data from the database", e);
            throw new DAOException("Failed to retrieve data from the database", e);
        }

        return results;
    }

    private Instalment parseResult(ResultSet rs) throws DAOException, ValidationException, SQLException {
        // TODO
        return null;
    }

    @Override
    public List<Instalment> getAll() throws DAOException {
        return null;
    }

    @Override
    public List<Instalment> populate(List<Instalment> objects) throws DAOException, ValidationException {
        return null;
    }
}
