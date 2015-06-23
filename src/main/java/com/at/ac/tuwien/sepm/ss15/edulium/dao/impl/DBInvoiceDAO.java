package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Database implementation of the DAO interface for Invoice objects
 */
@PreAuthorize("isAuthenticated()")
class DBInvoiceDAO implements DAO<Invoice> {
    private static final Logger LOGGER = LogManager.getLogger(DBInvoiceDAO.class);

    @Resource(name = "dataSource")
    private DataSource dataSource;
    @Resource(name = "invoiceValidator")
    private Validator<Invoice> invoiceValidator;
    @Resource(name = "orderValidator")
    private Validator<Order> orderValidator;
    @Resource(name = "userDAO")
    private DAO<User> userDAO;
    @Resource(name = "orderDAO")
    private DAO<Order> orderDAO;

    /**
     * Writes the invoice object into the database and sets the identity
     * parameter that was generated there
     */
    @Override
    public void create(Invoice invoice) throws DAOException, ValidationException {
        LOGGER.debug("Entering create with parameters: " + invoice);

        invoiceValidator.validateForCreate(invoice);

        final String query = "INSERT INTO Invoice (invoiceTime, brutto, user_ID) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = dataSource.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(invoice.getTime()));
            stmt.setBigDecimal(2, invoice.getGross());
            stmt.setString(3, invoice.getCreator().getIdentity());
            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                invoice.setIdentity(key.getLong(1));
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to insert invoice entry into database", e);
            throw new DAOException("Failed to insert invoice entry into database", e);
        }

        updateOrders(invoice);
        generateHistory(invoice);
    }

    /**
     * Updates the invoice object in the database
     */
    @Override
    public void update(Invoice invoice) throws DAOException, ValidationException {
        LOGGER.debug("Entering update with parameters: " + invoice);

        invoiceValidator.validateForUpdate(invoice);

        final String query = "UPDATE Invoice SET invoiceTime = ?, brutto = ?, user_ID = ? WHERE id = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(invoice.getTime()));
            stmt.setBigDecimal(2, invoice.getGross());
            stmt.setString(3, invoice.getCreator().getIdentity());
            stmt.setLong(4, invoice.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Failed to update invoice entry in database, dataset not found");
                throw new DAOException("Failed to update invoice entry in database, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update invoice entry in database", e);
            throw new DAOException("Failed to update invoice entry in database", e);
        }

        updateOrders(invoice);
        generateHistory(invoice);
    }

    private void updateOrders(Invoice invoice) throws DAOException, ValidationException {
        LOGGER.debug("Entering updateOrders with parameters: " + invoice);

        final String orderQuery = "UPDATE RestaurantOrder SET invoice_ID = ? WHERE ID IN (" +
                invoice.getOrders().stream().map(u -> "?").collect(Collectors.joining(", ")) + ")";

        try (PreparedStatement orderStmt = dataSource.getConnection().prepareStatement(orderQuery)) {
            orderStmt.setLong(1, invoice.getIdentity());

            int index = 2;
            for (Order o : invoice.getOrders()) {
                orderStmt.setLong(index++, o.getIdentity());
            }

            if (orderStmt.executeUpdate() != invoice.getOrders().size()) {
                LOGGER.error("Failed to update order entry in database, dataset not found");
                throw new DAOException("Failed to update order entry in database, dataset not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update order entries in database", e);
            throw new DAOException("Failed to update order entries in database", e);
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

        generateHistory(invoice);
    }

    /**
     * Finds all invoice objects in the database which match the
     * parameters of the provided invoice object
     */
    @Override
    public List<Invoice> find(Invoice invoice) throws DAOException {
        LOGGER.debug("Entering find with parameters: " + invoice);

        if (invoice == null) {
            return new ArrayList<>();
        }

        final List<Invoice> results = new ArrayList<>();

        final String query;

        if (invoice.getOrders() != null && invoice.getOrders().size() > 0) {
            query = "SELECT * FROM Invoice i WHERE " +
                    "i.ID = ISNULL(?, i.ID) AND " +
                    "i.invoiceTime = ISNULL(?, i.invoiceTime) AND " +
                    "i.brutto = ISNULL(?, i.brutto) AND " +
                    "i.user_ID = ISNULL(?, i.user_ID) AND " +
                    "canceled = FALSE AND " +
                    "EXISTS (SELECT 1 FROM RestaurantOrder o " +
                    "WHERE o.invoice_ID = i.ID AND o.ID IN (" +
                    invoice.getOrders().stream().map(o -> "?").collect(Collectors.joining(", ")) +
                    ") AND o.canceled = FALSE);";
        } else {
            query = "SELECT * FROM Invoice i WHERE " +
                    "i.ID = ISNULL(?, i.ID) AND " +
                    "i.invoiceTime = ISNULL(?, i.invoiceTime) AND " +
                    "i.brutto = ISNULL(?, i.brutto) AND " +
                    "i.user_ID = ISNULL(?, i.user_ID) AND " +
                    "canceled = FALSE;";
        }

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setObject(1, invoice.getIdentity());
            stmt.setObject(2, invoice.getTime() == null ? null : Timestamp.valueOf(invoice.getTime()));
            stmt.setObject(3, invoice.getGross());
            stmt.setObject(4, invoice.getCreator() == null ? null : invoice.getCreator().getIdentity());

            if (invoice.getOrders() != null && invoice.getOrders().size() > 0) {
                int index = 5;
                for (Order order : invoice.getOrders()) {
                    stmt.setLong(index++, order.getIdentity());
                }
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
                try {
                    results.add(parseResult(rs));
                } catch (ValidationException e) {
                    LOGGER.warn("parsing the result '" + rs + "' failed", e);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to retreive all invoice entries from the database", e);
            throw new DAOException("Failed to retreive all invoice entries from the database", e);
        }

        return results;
    }

    /**
     * Gets the history of changes for the invoice object from
     * the database
     */
    @Override
    public List<History<Invoice>> getHistory(Invoice invoice) throws DAOException, ValidationException {
        LOGGER.debug("entering getHistory with parameters " + invoice);

        invoiceValidator.validateIdentity(invoice);

        List<History<Invoice>> history = new ArrayList<>();

        final String query = "SELECT * FROM InvoiceHistory WHERE ID = ? ORDER BY changeNr";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, invoice.getIdentity());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                history.add(parseHistoryResult(rs));
            }
            rs.close();
        } catch (SQLException e) {
            LOGGER.error("Failed to retrieve history", e);
            throw new DAOException("Failed to retrieve history", e);
        }

        return history;
    }

    @Override
    public List<Invoice> populate(List<Invoice> invoices) throws DAOException, ValidationException {
        LOGGER.debug("Entering populate with parameters: " + invoices);

        if (invoices == null || invoices.isEmpty()) {
            return new ArrayList<>();
        }

        for (Invoice invoice : invoices) {
            invoiceValidator.validateIdentity(invoice);
        }

        final String query = "SELECT * FROM Invoice WHERE ID IN (" +
                invoices.stream().map(u -> "?").collect(Collectors.joining(", ")) + ")"; // fake a list of identities

        final List<Invoice> populatedInvoices = new ArrayList<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            int index = 1;

            // fill identity list
            for (Invoice invoice : invoices) {
                stmt.setLong(index++, invoice.getIdentity());
            }

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                populatedInvoices.add(parseResult(result));
            }
        } catch (SQLException e) {
            LOGGER.error("Populating invoices failed", e);
            throw new DAOException("Populating invoices failed", e);
        }

        return populatedInvoices;
    }

    /**
     * Generates a historic event in the InvoiceHistory table
     * @param invoice The updated dataset
     * @throws DAOException Thrown if an error occurs when accessing the database
     */
    private void generateHistory(Invoice invoice) throws DAOException {
        LOGGER.debug("Entering generateHistory with parameters: " + invoice);

        final String query = "INSERT INTO InvoiceHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM InvoiceHistory WHERE ID = ?) " +
                "FROM Invoice WHERE ID = ?);";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName());
            stmt.setLong(2, invoice.getIdentity());
            stmt.setLong(3, invoice.getIdentity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Generating history failed", e);
            throw new DAOException("Generating history failed", e);
        }
    }

    /**
     * Parses a result set in an object of type invoice
     * @param rs The database output
     * @return The parsed object of type invoice
     * @throws DAOException If an error, while retrieving the invoice data, occurs
     * @throws SQLException If an error, while accessing database results, occurs
     */
    private Invoice parseResult(ResultSet rs) throws DAOException, ValidationException, SQLException {
        List<User> creator = userDAO.populate(Collections.singletonList(User.withIdentity(rs.getString("user_ID"))));
        if (creator.size() != 1) {
            LOGGER.error("Retrieving creator failed");
            throw new DAOException("Retrieving creator failed");
        }

        Invoice invoice = new Invoice();
        invoice.setCreator(creator.get(0));
        invoice.setIdentity(rs.getLong("ID"));
        invoice.setTime(rs.getTimestamp("invoiceTime").toLocalDateTime());
        invoice.setGross(rs.getBigDecimal("brutto"));

        List<Order> orders = new LinkedList<>();
        final String query = "SELECT ID FROM RestaurantOrder WHERE invoice_ID = ?";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, invoice.getIdentity());

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                orders.add(Order.withIdentity(resultSet.getLong("ID")));
            }
        } catch (SQLException e) {
            LOGGER.error("Getting orders failed", e);
            throw new DAOException("Getting orders failed", e);
        }
        invoice.setOrders(orderDAO.populate(orders));

        return invoice;
    }

    /**
     * Parses a result set in an object of type History
     * @param rs The result set
     * @return History object containing the result set information
     * @throws DAOException Thrown in case an error occurs when accessing the database
     * @throws SQLException Thrown in case an error occurs when accessing the database result
     */
    private History<Invoice> parseHistoryResult(ResultSet rs) throws DAOException, ValidationException, SQLException {
        List<User> user = userDAO.populate(Collections.singletonList(User.withIdentity(rs.getString("changeUser"))));
        if (user.size() != 1) {
            LOGGER.error("User not found");
            throw new DAOException("User not found");
        }

        History<Invoice> event = new History<>();
        event.setTimeOfChange(rs.getTimestamp("changeTime").toLocalDateTime());
        event.setChangeNumber(rs.getLong("changeNr"));
        event.setDeleted(rs.getBoolean("canceled"));
        event.setUser(user.get(0));
        event.setData(parseResult(rs));

        return event;
    }
}
