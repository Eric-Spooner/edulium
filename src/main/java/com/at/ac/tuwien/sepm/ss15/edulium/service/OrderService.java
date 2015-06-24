package com.at.ac.tuwien.sepm.ss15.edulium.service;


import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;
import java.util.List;

/**
 * service for the Order domain object
 */
@PreAuthorize("isAuthenticated()")
public interface OrderService extends Service {

    /**
     * adds a Order object to the underlying datasource
     * checks the price of the menuEntry and updates it, if there is an active sale
     * @param order order to add
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('SERVICE')")
    void addOrder(Order order) throws ServiceException, ValidationException;

    /**
     * updates a order object in the underlying datasource
     * @pre: The order must not have an Invoice
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
    @PreAuthorize("hasRole('SERVICE')")
    void updateOrder(Order order) throws ServiceException, ValidationException;

    /**
     * deletes an order object from the underlying datasource
     * @pre: the order has to be in the state QUEUED
     * @param order order to remove
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('SERVICE')")
    void cancelOrder(Order order) throws ServiceException, ValidationException;

    /**
     * returns all orders from the underlying datasource which paramters
     * match the paramter of the matcher object
     * @param template template used for finding Orders
     * @throws ServiceException if an error processing the request ocurred
     */
    List<Order> findOrder(Order template) throws ServiceException;

    /**
     * returns all orders in the given interval
     * @param from start of the interval
     * @param to end of the interval
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the parameters are invalid
     */
    List<Order> findOrderBetween(LocalDateTime from, LocalDateTime to) throws ServiceException;

    /**
     * returns all orders from the underlying datasource
     * @throws ServiceException if an error processing the request ocurred
     */
    List<Order> getAllOrders() throws ServiceException;

    /**
     * returns all History Orders from the underlying datasource which paramters
     * match the paramter of the matcher object
     * @param template template used for finding Orders
     * @throws ServiceException if an error processing the request ocurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<History<Order>> getOrderHistory(Order template) throws ServiceException, ValidationException;


    /**
     * The function is used to get the User, who submitted the Order
     *
     * @param order, the submitter is asked for
     * @return the user, the order submitted
     * @throws ServiceException
     * @throws ValidationException
     */
    User getOrderSubmitter(Order order) throws ServiceException, ValidationException;

    /**
     * Cook uses this function, to set the state of the order to IN_PROGRESS
     * @pre: The state of the order has to be QUEUED
     * @post: The state of the order is IN_PROGRESS
     * @param order order
     * @throws ServiceException
     */
    @PreAuthorize("hasRole('COOK')")
    void markAsInProgress(Order order) throws ServiceException, ValidationException;

    /**
     * Cook uses this function, to set the state of the order to READY_FOR_DELIVERY
     * @pre: The state of the order has to be IN_PROGRESS
     * @post: The state of the order is READY_FOR_DELIVERY
     * @param order order
     * @throws ServiceException
     */
    @PreAuthorize("hasRole('COOK')")
    void markAsReadyForDelivery(Order order) throws ServiceException, ValidationException;


    /**
     * Service uses this function, to set the state of the order to READY_FOR_DELIVERY
     * @pre: The state of the order has to be READY_FOR_DELIVERY or QUEUED
     * @post: The state of the order is DELIVERED
     * @param order order
     * @throws ServiceException
     */
    @PreAuthorize("hasRole('SERVICE')")
    void markAsDelivered(Order order) throws ServiceException, ValidationException;

}
