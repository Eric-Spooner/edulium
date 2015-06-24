package com.at.ac.tuwien.sepm.ss15.edulium.business;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import javafx.scene.control.Tab;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Interface for Automatic Table assignment
 */
@PreAuthorize("isAuthenticated()")
public interface TableBusinessLogic extends BusinessLogic {

    /**
     * If the table has no assignment, it will be assigned to the user, which
     * took the order
     * If the table is assigned at the moment, but has no orders, or every order, which is on the table
     * is already paid, the table will again be assigned to the user, which took the new order
     * @param table the table, the order has been placed
     */
    @PreAuthorize("hasRole('SERVICE')")
    public void addedOrderToTable(Table table, Order order) throws ServiceException, ValidationException;

    /**
     * Checks if every order, which has been placed at the table has been paid, and resets the assignment of the
     * table to null.
     * @param table the table, the orders have been paid
     */
    @PreAuthorize("hasRole('SERVICE')")
    public void removedOrderFromTable(Table table) throws ServiceException, ValidationException;

    /**
     *  Function is used to set the Table User new, if orders are beeing moved
     *  if there are open orders left on the tableOld, the user will not be changed
     *  If there are no open orders left on the tableOld, the user will be set to null
     *  If there are open orders on the new table the user of the new table will not be changed
     *  if there are no open orders on the new table the user of the new table will be set to the
     *  user on the old table
     * @param tableOld the table, the orders are now
     * @param tableNew the table, the orders should move to
     * @param ordersToMove the orders, which should be moved from old to new
     */
    @PreAuthorize("hasRole('SERVICE')")
    public void movedOrders(Table tableOld, Table tableNew, List<Order> ordersToMove) throws ServiceException, ValidationException;
}
