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
     */
    @PreAuthorize("hasRole('SERVICE')")
    void addedOrder(Order order) throws ServiceException, ValidationException;

    /**
     * Checks if every order, which has been placed at the table has been paid, and resets the assignment of the
     * table to null.
     */
    @PreAuthorize("hasRole('SERVICE')")
    void paidOrder(Order order) throws ServiceException, ValidationException;

    /**
     * Checks if every order, which has been removed from the table, and resets the assignment of the
     * table to null.
     */
    @PreAuthorize("hasRole('SERVICE')")
    void removedOrder(Order order) throws ServiceException, ValidationException;
}
