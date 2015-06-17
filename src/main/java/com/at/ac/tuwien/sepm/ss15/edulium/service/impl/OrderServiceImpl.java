package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import sun.security.validator.ValidatorException;

import java.util.LinkedList;
import java.util.List;

/**
 * implementation of the Order Service
 */
class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);
    @Autowired
    private DAO<Order> orderDAO;
    @Autowired
    private Validator<Order> orderValidator;


    @Override
    public void addOrder(Order order) throws ServiceException, ValidationException {
        LOGGER.debug("Entering addOrder with parameter: " + order);

        orderValidator.validateForCreate(order);

        try {
            orderDAO.create(order);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public void updateOrder(Order order) throws ServiceException, ValidationException {
        LOGGER.debug("Entering addOrder with parameter: " + order);

        orderValidator.validateForUpdate(order);

        List<Order> preOrders = this.findOrder(Order.withIdentity(order.getIdentity()));

        if(preOrders.size()==0){
            LOGGER.error("The order, you would like to update does not exist");
            throw new ServiceException("The order, you would like to update does not exist");
        }else {
            Order preOrder = preOrders.get(0);
            //if the order already had an invoice it is not allowed to be changed
            if(preOrder.getInvoice() != null){
                LOGGER.error("It is not allowed to change an order with invoice");
                throw new ServiceException("It is not allowed to change an order with invoice");
            }
            //Check which parameter have changed
            //Menu Entry and Time are not allowed to be changed
            if (!preOrder.getMenuEntry().equals(order.getMenuEntry())) {
                LOGGER.error("It is not allowed to change the MenuEntry of the order");
                throw new ServiceException("It is not allowed to change the MenuEntry of the order");
            }
            if (!preOrder.getTime().equals(order.getTime())) {
                LOGGER.error("It is not allowed to change the time of the order");
                throw new ServiceException("It is not allowed to change the time of the order");
            }
            //It is not allowed to change the additional information, if the order is not in the state QUEUED
            if (order.getState() != Order.State.QUEUED) {
                if (!preOrder.getAdditionalInformation().equals(order.getAdditionalInformation())) {
                    LOGGER.error("It is not allowed to change the additional Information of the order");
                    throw new ServiceException("It is not allowed to change the time of the order");
                }
            }

            try {
                orderDAO.update(order);
            } catch (DAOException e) {
                LOGGER.error("An Error has occurred in the data access object", e);
                throw new ServiceException("An Error has occurred in the data access object");
            }
        }
    }

    @Override
    public void cancelOrder(Order order) throws ServiceException, ValidationException {
        LOGGER.debug("Entering addOrder with parameter: " + order);

        orderValidator.validateForDelete(order);

        if(order.getState() != Order.State.QUEUED){
            LOGGER.error("The precondition of the function is not True, the order is not in State QUEUED");
            throw new
                    ServiceException("The precondition of the function is not True, the order is not in State QUEUED");
        }else {
            try {
                orderDAO.delete(order);
            } catch (DAOException e) {
                LOGGER.error("An Error has occurred in the data access object", e);
                throw new ServiceException("An Error has occurred in the data access object");
            }
        }
    }

    @Override
    public List<Order> findOrder(Order template) throws ServiceException {
        LOGGER.debug("Entering findOrder with parameter: " + template);

        try {
            return orderDAO.find(template);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public List<Order> getAllOrders() throws ServiceException {
        LOGGER.debug("Entering getAllOrders");

        try {
            return orderDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public List<History<Order>> getOrderHistory(Order template) throws ServiceException, ValidationException {
        LOGGER.debug("Entering getOrderHistory with parameter: " + template);

        orderValidator.validateIdentity(template);

        try {
            return orderDAO.getHistory(template);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public List<Order> getAllOrdersToPrepare(List<MenuCategory> menuCategories, Order.State state) throws
            ServiceException, ValidationException {
        LOGGER.debug("Entering getAllOrdersToPrepare with parameter " + menuCategories);

        List<Order> retVal = new LinkedList<>();

        if(menuCategories.isEmpty()){
            return new LinkedList<>();
        }

        //Prepare dummy MenuEntries with the given Categories for finding purpose
        for(MenuCategory category : menuCategories){
            MenuEntry dummyEntry = new MenuEntry();
            dummyEntry.setCategory(category);
            Order dummyOrder = new Order();
            dummyOrder.setMenuEntry(dummyEntry);
            dummyOrder.setState(state);
            for(Order order : this.findOrder(dummyOrder)){
               if(!retVal.contains(order)) retVal.add(order);
            }
        }
        return retVal;
    }

    @Override
    public void markAsInProgress(Order order) throws ServiceException, ValidationException {
        LOGGER.debug("Entering markAsInProgress with parameter " + order);

        if(order.getState() != Order.State.QUEUED){
            LOGGER.error("The precondition of the function is not True, the order is not in State QUEUED");
            throw new
                    ServiceException("The precondition of the function is not True, the order is not in State QUEUED");
        }else{
            order.setState(Order.State.IN_PROGRESS);
            try {
                orderDAO.update(order);
            }catch (DAOException e) {
                LOGGER.error("An Error has occurred in the data access object", e);
                throw new ServiceException("An Error has occurred in the data access object");
            }
        }
    }

    @Override
    public void markAsReadyForDelivery(Order order) throws ServiceException, ValidationException {
        LOGGER.debug("Entering markAsReadyForDelivery with parameter " + order);

        if(order.getState() != Order.State.IN_PROGRESS){
            LOGGER.error("The precondition of the function is not True, the order is not in State QUEUED");
            throw new
                    ServiceException("The precondition of the function is not True, the order is not in State QUEUED");
        }else{
            order.setState(Order.State.READY_FOR_DELIVERY);
            try {
                orderDAO.update(order);
            }catch (DAOException e) {
                LOGGER.error("An Error has occurred in the data access object", e);
                throw new ServiceException("An Error has occurred in the data access object");
            }
        }
    }

    @Override
    public void markAsDelivered(Order order) throws ServiceException, ValidationException {
        LOGGER.debug("Entering markAsDelivered with parameter " + order);

        if(order.getState() != Order.State.READY_FOR_DELIVERY &&
            order.getState() != Order.State.QUEUED){
            LOGGER.error("The precondition of the function is not True, the order is not in State QUEUED");
            throw new
                    ServiceException("The precondition of the function is not True, the order is not in State QUEUED");
        }else{
            order.setState(Order.State.DELIVERED);
            try {
                orderDAO.update(order);
            }catch (DAOException e) {
                LOGGER.error("An Error has occurred in the data access object", e);
                throw new ServiceException("An Error has occurred in the data access object");
            }
        }
    }


}
