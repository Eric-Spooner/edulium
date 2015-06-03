package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import java.util.List;

/**
 * Created by - on 03.06.2015.
 */
public class OrderServiceImpl implements OrderService {
    @Override
    public void addOrder(Order order) throws ServiceException, ValidationException {

    }

    @Override
    public void updateOrder(Order order) throws ServiceException, ValidationException {

    }

    @Override
    public void removeOrder(Order order) throws ServiceException, ValidationException {

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
    public List<Order> getOrderHistory(Order template) throws ServiceException {
        return null;
    }
}
