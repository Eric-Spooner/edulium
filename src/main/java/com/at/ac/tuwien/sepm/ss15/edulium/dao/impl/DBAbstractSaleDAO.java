package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Sale;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is a helper class for the H2 Database implementations DBIntermittentSaleDAO and DBOnetimeSaleDAO
 */
public class DBAbstractSaleDAO {
    /**
     * This function is used, to update/create Sale Assoc Entries
     * All SaleAssoc with the SaleID are disabled, the one, which are used are enabled again and the rest is added
     * the parameters are used, to identify the Sale Assoc and to set the values
     *
     * @param sale Sale Is used to identify the SaleAssoc
     */
    public static void updateSaleAssoc(Sale sale, DataSource dataSource, Logger LOGGER) throws DAOException {
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
            for (MenuEntry entry : sale.getEntries().keySet()) {
                stmt.setLong(1, sale.getIdentity());
                stmt.setLong(2, entry.getIdentity());
                stmt.setBigDecimal(3, sale.getEntries().get(entry));
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
     */
    public static void generateSaleHistory(Sale sale, DataSource dataSource, Logger LOGGER) throws DAOException {
        LOGGER.debug("Entering generateHistory with parameters: " + sale);

        final String query = "INSERT INTO SaleHistory " +
                "(SELECT *, CURRENT_TIMESTAMP(), ?, " +
                "(SELECT ISNULL(MAX(changeNr) + 1, 1) FROM SaleHistory WHERE ID = ?) " +
                "FROM Sale WHERE ID = ?)";

        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(query)) {
            stmt.setString(1, SecurityContextHolder.getContext().getAuthentication().getName()); // user
            stmt.setLong(2, sale.getIdentity());          // dataset id
            stmt.setLong(3, sale.getIdentity());          // dataset id

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("generating history failed", e);
            throw new DAOException("generating history failed", e);
        }
    }

    /**
     * Sets the name of the sale object according to the value in the database.
     * @param sale sale object resulting from a query, not having a name value yet
     * @throws SQLException if an error accessing the database occurred
     */
    public static void addNameToSale(Sale sale, DataSource dataSource) throws SQLException {
        final String saleQuery = "SELECT name FROM Sale WHERE ID = ?";
        PreparedStatement stmt = dataSource.getConnection().prepareStatement(saleQuery);
        stmt.setObject(1, sale.getIdentity());
        ResultSet result = stmt.executeQuery();
        result.next();
        String name = result.getString("name");
        sale.setName(name);
    }
}
