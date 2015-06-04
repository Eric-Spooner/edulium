package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import java.util.List;

/**
 * implementation of the Order Service
 */
public class OrderServiceImpl implements OrderService {
    @Override
    public void addOrder(Order order) throws ServiceException, ValidationException {

    }

    @Override
    public void updateOrder(Order order) throws ServiceException, ValidationException {

    }

    @Override
    public void cancelOrder(Order order) throws ServiceException, ValidationException {

    }

    @Override
    public List<Order> findOrder(Order template) throws ServiceException {
        return null;
    }

    @Override
    public List<Order> getAllOrders() throws ServiceException {
        return null;
    }

    @Override
    public List<History<Order>> getOrderHistory(Order template) throws ServiceException {
        return null;
    }

    @Override
    public List<Order> getAllOrdersToCook() throws ServiceException {
        return null;
    }

    @Override
    public void setStateOfOrder(Order order) throws ServiceException {

    }

}
