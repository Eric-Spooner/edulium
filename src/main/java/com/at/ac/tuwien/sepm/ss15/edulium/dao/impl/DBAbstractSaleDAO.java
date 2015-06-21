package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.IntermittentSale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Sale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * This is a helper class for the H2 Database implementations DBIntermittentSaleDAO and DBOnetimeSaleDAO
 */
@PreAuthorize("isAuthenticated()")
abstract class DBAbstractSaleDAO<T extends Sale> implements DAO<T> {
    private static final Logger LOGGER = LogManager.getLogger(DBAbstractSaleDAO.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;

    @Override
    public void create(T sale) throws DAOException, ValidationException {
        //Save Sale
        final String saleQuery = "INSERT INTO Sale (name) VALUES (?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(saleQuery)) {
            stmt.setString(1, sale.getName());
            stmt.executeUpdate();

            ResultSet key = stmt.getGeneratedKeys();
            if (key.next()) {
                sale.setIdentity(key.getLong(1));
            }
            key.close();
        } catch (SQLException e) {
            LOGGER.error("Inserting sale into database failed", e);
            throw new DAOException("Inserting sale into database failed", e);
        }

        updateSaleAssoc(sale);
    }

    @Override
    public void update(T sale) throws DAOException, ValidationException {
        final String saleQuery = "UPDATE Sale SET name = ? WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(saleQuery)) {
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

        updateSaleAssoc(sale);
    }

    @Override
    public void delete(T sale) throws DAOException, ValidationException {
        LOGGER.debug("Entering delete with parameters: " + sale);

        final String saleQuery = "UPDATE Sale SET deleted = true WHERE ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(saleQuery)) {
            stmt.setLong(1, sale.getIdentity());

            if (stmt.executeUpdate() == 0) {
                LOGGER.error("Deleting sale from database failed, sale not found");
                throw new DAOException("Deleting sale from database failed, sale not found");
            }
        } catch (SQLException e) {
            LOGGER.error("Deleting sale from database failed", e);
            throw new DAOException("Deleting sale from database failed", e);
        }
    }

    /**
     * This function is used, to update/create Sale Assoc Entries
     * All SaleAssoc with the SaleID are disabled, the one, which are used are enabled again and the rest is added
     * the parameters are used, to identify the Sale Assoc and to set the values
     *
     * @param sale Sale Is used to identify the SaleAssoc
     */
    protected void updateSaleAssoc(Sale sale) throws DAOException {
        LOGGER.debug("Entering updateSaleAssoc with parameters: " + sale);
        // At first, look if the
        // disable all SaleAssoc for now, the update sql query will re-enable all valid
        // associations
        final String queryDisableSaleAssoc = "UPDATE SaleAssoc SET disabled = true WHERE sale_ID = ?";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryDisableSaleAssoc)) {
            stmt.setLong(1, sale.getIdentity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Disabling Sale-MenuEntry associations from database failed", e);
            throw new DAOException("Disabling Sale-MenuEntry associations from database failed", e);
        }

        final String queryUpdateSaleAssoc =
                "Merge INTO SaleAssoc (sale_ID, menuEntry_ID, salePrice, disabled) " +
                        "KEY (sale_ID, menuEntry_ID) VALUES (?, ?, ?, false)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(queryUpdateSaleAssoc)) {
            for (MenuEntry entry : sale.getEntries()) {
                stmt.setLong(1, sale.getIdentity());
                stmt.setLong(2, entry.getIdentity());
                stmt.setBigDecimal(3, entry.getPrice());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("updating saleAssoc in database failed", e);
            throw new DAOException("updating saleAssoc in database failed", e);
        }
    }

    /**
     * writes the changes of the dataset into the database
     * stores the time; number of the change and the user which executed the changes
     * @param sale updated dataset
     * @throws DAOException if an error accessing the database occurred
     * @return returns the changeNr
     */
    protected long generateSaleHistory(Sale sale) throws DAOException {
        LOGGER.debug("Entering generateHistory with parameters: " + sale);

        final long changeNr;

        final String changeNrQuery = "SELECT ISNULL(MAX(changeNr) + 1, 1) AS changeNr FROM SaleHistory WHERE ID = ?";
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(changeNrQuery)) {
            stmt.setLong(1, sale.getIdentity());

            ResultSet result = stmt.executeQuery();
            if(!result.next()) {
                LOGGER.error("retrieving changeNr failed");
                throw new DAOException("retrieving changeNr failed");
            }

            changeNr = result.getLong("changeNr");
        } catch (SQLException e) {
            LOGGER.error("retrieving changeNr failed");
            throw new DAOException("retrieving changeNr failed");
        }


        final String query = "INSERT INTO SaleHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, ? " +
                "FROM Sale WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, changeNr);          // change nr
            stmt.setLong(3, sale.getIdentity());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }

        generateSaleAssociationsHistory(sale, changeNr);
        return changeNr;
    }

    /**
     * writes the association changes of the dataset into the database
     * stores the time; number of the change (uses the given changeNumber) and the user which executed the changes
     * @param sale updated dataset
     * @param changeNumber sale history change number
     * @throws DAOException if an error accessing the database occurred
     */
    private void generateSaleAssociationsHistory(Sale sale, long changeNumber) throws DAOException {
        LOGGER.debug("Entering generateSaleAssociationsHistory with parameters: " + sale);

        final String query = "INSERT INTO SaleAssocHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, ? FROM SaleAssoc WHERE sale_ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, changeNumber);
            stmt.setLong(3, sale.getIdentity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history for sale-table associations failed", e);
            throw new DAOException("generating history for sale-table associations failed", e);
        }
    }

    /**
     * searches all entries from the sale-menuEntry associations for the given sale
     * @param sale sale dataset
     * @throws DAOException if an error accessing the database occurred
     */
    private List<MenuEntry> getEntriesForSale(T sale) throws DAOException, ValidationException {
        LOGGER.debug("Entering getEntriesForSale with parameters: " + sale);

        final String query = "SELECT menuEntry_ID, salePrice FROM SaleAssoc WHERE sale_ID = ? " +
                "AND disabled = false";

        List<MenuEntry> entries = new ArrayList<>();
        HashMap<Long, BigDecimal> salePrices = new HashMap<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, sale.getIdentity());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final Long menuEntryId = result.getLong("menuEntry_ID");

                salePrices.put(menuEntryId, result.getBigDecimal("salePrice"));
                entries.add(MenuEntry.withIdentity(menuEntryId));
            }

        } catch (SQLException e) {
            LOGGER.error("Searching for reservation-tables failed", e);
            throw new DAOException("Searching for reservation-tables failed", e);
        }

        // populate list with data
        entries = menuEntryDAO.populate(entries);
        // set sale prices
        entries.stream().forEach(entry -> entry.setPrice(salePrices.get(entry.getIdentity())));

        return entries;
    }

    /**
     * searches all entries from the sale-menuEntry associations history for the history
     * @param history history object with sale_id and changeNr set
     * @throws DAOException if an error accessing the database occurred
     */
    private List<MenuEntry> getEntriesForSaleHistory(History<T> history) throws DAOException, ValidationException {
        LOGGER.debug("Entering getEntriesForSaleHistory with parameters: " + history);

        final String query = "SELECT menuEntry_ID, salePrice FROM SaleAssocHistory " +
                "WHERE sale_ID = ? AND changeNr = ? AND disabled = false";

        List<MenuEntry> entries = new ArrayList<>();
        HashMap<Long, BigDecimal> salePrices = new HashMap<>();

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setLong(1, history.getData().getIdentity());
            stmt.setLong(2, history.getChangeNumber());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final Long menuEntryId = result.getLong("menuEntry_ID");

                salePrices.put(menuEntryId, result.getBigDecimal("salePrice"));
                entries.add(MenuEntry.withIdentity(menuEntryId));
            }

        } catch (SQLException e) {
            LOGGER.error("Searching for reservation-tables failed", e);
            throw new DAOException("Searching for reservation-tables failed", e);
        }

        // populate list with data
        entries = menuEntryDAO.populate(entries);
        // set sale prices
        entries.stream().forEach(entry -> entry.setPrice(salePrices.get(entry.getIdentity())));

        return entries;
    }

    /**
     * Sets the name and the entries of the sale object according to the values in the database.
     * @param sale sale object resulting from a query
     */
    protected void saleFromResultSet(T sale, ResultSet result) throws SQLException, DAOException, ValidationException {
        LOGGER.debug("Entering saleFromResultSet with parameters: " + sale);

        // set name
        sale.setName(result.getString("name"));
        sale.setEntries(getEntriesForSale(sale));
    }

    protected  void saleHistoryFromResultSet(History<T> history, ResultSet result) throws SQLException, DAOException, ValidationException  {
        LOGGER.debug("Entering saleHistoryFromResultSet " + history);

        // get user
        List<User> storedUsers = userDAO.find(User.withIdentity(result.getString("changeUser")));
        if (storedUsers.size() != 1) {
            LOGGER.error("user not found");
            throw new DAOException("user not found");
        }

        history.setTimeOfChange(result.getTimestamp("changeTime").toLocalDateTime());
        history.setChangeNumber(result.getLong("changeNr"));
        history.setUser(storedUsers.get(0));
        history.setDeleted(result.getBoolean("deleted"));

        history.getData().setName(result.getString("name"));
        history.getData().setEntries(getEntriesForSaleHistory(history));
    }

}