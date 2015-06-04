package com.at.ac.tuwien.sepm.ss15.edulium.service;


import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * service for the Order domain object
 */
public interface OrderService extends Service {

    /**
     * adds a Order object to the underlying datasource
     * @param order order to add
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    void addOrder(Order order) throws ServiceException, ValidationException;

    /**
     * updates a order object in the underlying datasource
     * @condition: Only in state QUEUED it is allowed to update the additional info
     *             it is only allowed to set the state in the given "direction"
     *             QUEUED -> IN_PROGRESS -> READY_FOR_DELIVERY -> DELIVERED
     *             it is allowed to skip steps
     *             it is not allowed to change the time of an order
     *             it is not allowed to change the MenuEntry of the order
     * @param order order to update
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    void updateOrder(Order order) throws ServiceException, ValidationException;

    /**
     * deletes an order object from the underlying datasource
     * @pre: the order has to be in the state QUEUED
     * @param order order to remove
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    void cancelOrder(Order order) throws ServiceException, ValidationException;

    /**
     * returns all taxRates from the underlying datasource which paramters
     * match the paramter of the matcher object
     * @param template template used for finding Orders
     * @throws ServiceException if an error processing the request ocurred
     */
    List<Order> findOrder(Order template) throws ServiceException;

    /**
     * returns all taxRates from the underlying datasource
     * @throws ServiceException if an error processing the request ocurred
     */
    List<Order> getAllOrders() throws ServiceException;

    /**
     * returns all History Orders from the underlying datasource which paramters
     * match the paramter of the matcher object
     * @param template template used for finding Orders
     * @throws ServiceException if an error processing the request ocurred
     */
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    List<History<Order>> getOrderHistory(Order template) throws ServiceException;

    /**
     * returns all Orders, which have to be cooked
     * @return List of MenuEntries, which should be cooked
     * @throws ServiceException
     */
    @PreAuthorize("hasRole('ROLE_COOK')")
    List<Order> getAllOrdersToCook() throws ServiceException;

    /**
     * Service or Cook uses this function, to set the state of the order
     * @post: it is only allowed to set the state in the given "direction"
     *        QUEUED -> IN_PROGRESS -> READY_FOR_DELIVERY -> DELIVERED
     *        it is allowed to skip steps
     * @param order order
     * @throws ServiceException
     */
    @PreAuthorize("hasAnyRole('ROLE_SERVICE','ROLE_COOK')")
    void setStateOfOrder(Order order) throws ServiceException;
}
