package com.at.ac.tuwien.sepm.ss15.edulium.service;


import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
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
    @PreAuthorize("hasRole('ROLE_WAITER')")
    void addOrder(Order order) throws ServiceException, ValidationException;

    /**
     * updates a order object in the underlying datasource
     * @param order order to update
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('ROLE_WAITER')")
    void updateOrder(Order order) throws ServiceException, ValidationException;

    /**
     * deletes an order object from the underlying datasource
     * @param order order to remove
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('ROLE_WAITER')")
    void removeOrder(Order order) throws ServiceException, ValidationException;

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
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    List<Order> getOrderHistory(Order template) throws ServiceException;
}
