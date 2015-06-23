package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.ImmutableDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Instalment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ImmutableValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Database implementation of the DAO interface for Instalment objects
 */
class DBInstalmentDAO implements ImmutableDAO<Instalment> {
    private static final Logger LOGGER = LogManager.getLogger(DBInstalmentDAO.class);

    @Resource(name = "dataSource")
    private DataSource dataSource;
    @Resource(name = "invoiceDAO")
    private DAO<Invoice> invoiceDAO;
    @Resource(name = "instalmentValidator")
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

    /**
     * Finds all instalment objects in the database which match the
     * parameters of the provided invoice object
     */
    @Override
    public List<Instalment> find(Instalment object) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + object);

        if (object == null) {
            return new ArrayList<>();
        }

        final List<Instalment> results = new ArrayList<>();

        final String query = "SELECT * FROM Instalment WHERE " +
                "ID = ISNULL(?, ID) AND " +
                "instalmentTime = ISNULL(?, instalmentTime) AND " +
                "paymentInfo = ISNULL(?, paymentInfo) AND " +
                "type = ISNULL(?, type) AND " +
                "amount = ISNULL(?, amount) AND " +
                "invoice_ID = ISNULL(?, invoice_ID);";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            if (object.getIdentity() == null) {
                stmt.setNull(1, Types.BIGINT);
            } else {
                stmt.setLong(1, object.getIdentity());
            }
            stmt.setTimestamp(2, object.getTime() == null ? null : Timestamp.valueOf(object.getTime()));
            stmt.setString(3, object.getPaymentInfo());
            stmt.setString(4, object.getType());
            stmt.setBigDecimal(5, object.getAmount());
            if (object.getInvoice() == null) {
                stmt.setNull(6, Types.BIGINT);
            } else {
                stmt.setLong(6, object.getInvoice().getIdentity());
            }

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

    /**
     * Retrieves all instalment entries from the database
     */
    @Override
    public List<Instalment> getAll() throws DAOException {
        LOGGER.debug("Entering getAll");

        final List<Instalment> results = new ArrayList<>();

        final String query = "SELECT * FROM Instalment;";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                try {
                    results.add(parseResult(rs));
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + rs + "' failed", e);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to retreive all instalment entries from the database", e);
            throw new DAOException("Failed to retreive all instalment entries from the database", e);
        }

        return results;
    }

    /**
     * Populates the given list of instalment objects
     */
    @Override
    public List<Instalment> populate(List<Instalment> objects) throws DAOException, ValidationException {
        LOGGER.debug("Entering populate with parameters: " + objects);

        if (objects == null || objects.isEmpty()) {
            return new ArrayList<>();
        }

        for (Instalment instalment: objects) {
            instalmentValidator.validateIdentity(instalment);
        }

        final String query = "SELECT * FROM Instalment WHERE ID IN (" +
                objects.stream().map(u -> "?").collect(Collectors.joining(", ")) + ")"; // fake a list of identities

        final List<Instalment> populatedInstalments = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            int index = 1;

            // fill identity list
            for (Instalment instalment : objects) {
                stmt.setLong(index++, instalment.getIdentity());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                populatedInstalments.add(parseResult(rs));
            }

        } catch (SQLException e) {
            LOGGER.error("Populating instalments failed", e);
            throw new DAOException("Populating instalments failed", e);
        }

        return populatedInstalments;
    }

    /**
     * Parses a result set in an object of type instalment
     * @param rs The database output
     * @return The parsed object of type instalment
     * @throws DAOException If an error, while retrieving the instalment data, occurs
     * @throws SQLException If an error, while accessing database results, occurs
     */
    private Instalment parseResult(ResultSet rs) throws DAOException, ValidationException, SQLException {
        List<Invoice> invoice = invoiceDAO.populate(Arrays.asList(Invoice.withIdentity(rs.getLong("invoice_ID"))));
        if (invoice.size() != 1) {
            LOGGER.error("Retrieving invoice failed");
            throw new DAOException("Retrieving creator failed");
        }

        Instalment instalment = new Instalment();
        instalment.setIdentity(rs.getLong("ID"));
        instalment.setPaymentInfo(rs.getString("paymentInfo"));
        instalment.setType(rs.getString("type"));
        instalment.setAmount(rs.getBigDecimal("amount"));
        instalment.setTime(rs.getTimestamp("instalmentTime").toLocalDateTime());
        instalment.setInvoice(invoice.get(0));

        return instalment;
    }
}
