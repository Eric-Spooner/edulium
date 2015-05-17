package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * H2 Database Implementation of the Order DAO interface
 */
class DBOrderDAO implements DAO<Order> {
    private static final Logger LOGGER = LogManager.getLogger(DBOrderDAO.class);


    @Autowired
    private DataSource dataSource;
    @Autowired
    private Validator<Order> validator;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private DAO<Invoice> invoiceDAO;
    @Autowired
    private DAO<Table> tableDAO;
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;




    @Override
    public void create(Order order) throws DAOException, ValidationException {
        LOGGER.debug("entering create with parameters " + order);
        validator.validateForCreate(order);
        final String query = "INSERT INTO MenuEntry (invoice_ID, table_section, table_number, menuEntry_ID, " +
                "orderTime, brutto, tax, info) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, order.getInvoice().getIdentity());
            stmt.setLong(2, order.getTable().getSection().getIdentity());
            stmt.setLong(3, order.getTable().getNumber());
            stmt.setLong(4, order.getMenuEntry().getIdentity());
            stmt.setTimestamp(5, Timestamp.valueOf(order.getTime()));
            stmt.setBigDecimal(6, order.getBrutto());
            stmt.setBigDecimal(7, order.getTax());
            stmt.setString(8, order.getAdditionalInformation());
            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                order.setIdentity(key.getLong(1));
            }
            key.close();
        } catch (SQLException e) {
            LOGGER.error("inserting menuEntry into database failed", e);
            throw new DAOException("inserting menuEntry into database failed", e);
        }
        generateHistory(order);
    }

    @Override
    public void update(Order object) throws DAOException, ValidationException {

    }

    @Override
    public void delete(Order object) throws DAOException, ValidationException {

    }

    @Override
    public List<Order> find(Order object) throws DAOException {
        return null;
    }

    @Override
    public List<Order> getAll() throws DAOException {
        return null;
    }

    @Override
    public List<History<Order>> getHistory(Order object) throws DAOException, ValidationException {
        return null;
    }


    /**
     * Generates an Order History enter for the given order
     * @param order
     * @throws DAOException
     */
    private void generateHistory(Order order) throws DAOException {
        LOGGER.debug("entering generateHistory with parameters " + order);

        final String query = "INSERT INTO RestaurantOrderHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM RestaurantOrderHistory WHERE ID = ?) " +
                "FROM RestaurantOrder WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, order.getIdentity());          // dataset id
            stmt.setLong(3, order.getIdentity());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }

    /**
     * converts the database query output into a object
     * @param result database output
     * @return MenuEntry object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the taxRate/category occurred
     */
    private Order parseResult(ResultSet result) throws DAOException, SQLException {
        List<MenuEntry> menuEntries = menuEntryDAO.find(MenuEntry.withIdentity(result.getLong("menuEntry_ID")));
        if (menuEntries.size() != 1) {
            LOGGER.error("retrieving MenuEntry failed");
            throw new DAOException("retrieving MenuEntry failed");
        }
        List<Table> tables = tableDAO.find(Table.withIdentity(Section.withIdentity(result.getLong("table_section")),result.getLong("table_number")));
        if (tables.size() != 1) {
            LOGGER.error("retrieving MenuEntry failed");
            throw new DAOException("retrieving MenuEntry failed");
        }
        List<Invoice> invoices = invoiceDAO.find(Invoice.withIdentity(result.getLong("invoice_ID")));
        if (invoices.size() > 1) {
            LOGGER.error("retrieving Invoice failed");
            throw new DAOException("retrieving Invoice failed");
        }

        Order order = new Order();
        order.setIdentity(result.getLong("ID"));
        order.setAdditionalInformation(result.getString("info"));
        order.setBrutto(result.getBigDecimal("brutto"));
        order.setTax(result.getBigDecimal("tax"));
        order.setTime(result.getTimestamp("orderTime").toLocalDateTime());
        order.setTable(tables.get(0));
        order.setInvoice(invoices.get(0));
        order.setMenuEntry(menuEntries.get(0));
        return order;
    }

    /**
     * converts the database query output into a history entry object
     * @param result database output
     * @return History object with the data of the resultSet set
     * @throws SQLException if an error accessing the database occurred
     * @throws DAOException if an error retrieving the user ocurred
     */
    private History<Order> parseHistoryEntry(ResultSet result) throws DAOException, SQLException {
        // get user
        List<User> storedUsers = userDAO.find(User.withIdentity(result.getString("changeUser")));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        // create history entry
        History<Order> historyEntry = new History<>();
        historyEntry.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        historyEntry.setChangeNumber(result.getLong("changeNr"));
        historyEntry.setDeleted(result.getBoolean("deleted"));
        historyEntry.setUser(storedUsers.get(0));
        historyEntry.setData(parseResult(result));

        return historyEntry;
    }
}
