package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.business.TableBusinessLogic;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.OrderDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.SaleService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * implementation of the Order Service
 */
class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LogManager.getLogger(TaxRateServiceImpl.class);
    @Resource(name = "saleService")
    private SaleService saleService;
    @Resource(name = "orderDAO")
    private DAO<Order> orderDAO;
    @Resource(name = "orderDAO")
    private OrderDAO findBetweenOrderDAO;
    @Resource(name = "orderValidator")
    private Validator<Order> orderValidator;
    @Resource(name = "tableBusinessLogic")
    private TableBusinessLogic tableBusinessLogic;

    @Override
    public void addOrder(Order order) throws ServiceException, ValidationException {
        LOGGER.debug("Entering addOrder with parameter: " + order);

        orderValidator.validateForCreate(order);

        try {
            //Check if a sale is active and let the price be updated
            saleService.applySales(order);
            //Create the order with the updated price
            orderDAO.create(order);
            tableBusinessLogic.addedOrder(order);
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
            if(preOrder.isPaid()){
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

                if (!order.getTable().equals(preOrder.getTable())) {
                    // order has been moved to another table
                    tableBusinessLogic.removedOrder(preOrder);
                    tableBusinessLogic.addedOrder(order);
                }
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

        List<Order> preOrders = this.findOrder(Order.withIdentity(order.getIdentity()));

        if (preOrders.isEmpty()){
            LOGGER.error("The order, you would like to delete does not exist");
            throw new ServiceException("The order, you would like to delete does not exist");
        } else {
            Order preOrder = preOrders.get(0);

            if (preOrder.getState() != Order.State.QUEUED) {
                LOGGER.error("The precondition of the function is not True, the order is not in State QUEUED");
                throw new ServiceException("The precondition of the function is not True, the order is not in State QUEUED");
            }

            // check if invoice has been paid already
            if (preOrder.isPaid()) {
                LOGGER.error("The order has already been paid, can't cancel order");
                throw new ServiceException("The order '" + order.getMenuEntry().getName() + "' has already been paid, can't cancel order");
            }

            try {
                orderDAO.delete(order);

                tableBusinessLogic.removedOrder(preOrder);
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
    public List<Order> findOrderBetween(LocalDateTime from, LocalDateTime to) throws ServiceException {
        LOGGER.debug("Entering findOrderBetween with parameters: " + from +", "+ to);

        try {
            return findBetweenOrderDAO.findBetween(from, to);
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
    public User getOrderSubmitter(Order order) throws ServiceException, ValidationException {
        orderValidator.validateIdentity(order);
        if(this.getOrderHistory(order).size()>0) {
            History<Order> history = this.getOrderHistory(order).get(0);
            return history.getUser();
        }else{
            throw new ServiceException("THere should be at least one History entry for the order");
        }
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
