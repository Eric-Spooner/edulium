package com.at.ac.tuwien.sepm.ss15.edulium.business.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.business.TableBusinessLogic;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by - on 23.06.2015.
 */
class TableBusinessLogicImpl implements TableBusinessLogic {
    @Resource(name = "orderService")
    private OrderService orderService;
    @Resource(name = "interiorService")
    private InteriorService interiorService;
    @Resource(name = "invoiceService")
    private InvoiceService invoiceService;

    @Override
    public void addedOrderToTable(Table table, Order order) throws ServiceException, ValidationException{
        if(table.getUser() == null || checkIfNoOpenOrdersOnTable(table)) {
            table.setUser(orderService.getOrderSubmitter(order));
            interiorService.updateTable(table);
        }
    }

    @Override
    public void removedOrderFromTable(Table table) throws ServiceException, ValidationException{
        if(checkIfNoOpenOrdersOnTable(table)){
            table.setUser(null);
            interiorService.updateTable(table);
        }
    }

    @Override
    public void movedOrders(Table tableOld, Table tableNew, List<Order> ordersToMove) throws ServiceException, ValidationException {
        if (!ordersToMove.isEmpty()) {
            addedOrderToTable(tableNew, ordersToMove.get(0));
            removedOrderFromTable(tableOld);
        }
    }

    /**
     * This function is used to check the user assignment of the table
     * @param table which shell be checked
     * @return true... if every order has been paid, or there are no orders for the table
     *         false... if there are not paid orders on the table
     * @throws ServiceException
     */
    private boolean checkIfNoOpenOrdersOnTable(Table table) throws ServiceException{
        Order template = new Order();
        template.setTable(table);
        List<Order> orderList = orderService.findOrder(template);
        return checkIfEveryOrderIsPaid(orderList);
    }

    /**
     * This function is used, to check if the given orders have been paid already
     * @param orders
     * @return false, if there is one ore more open orders
     *         true, if every order has an invoice
     */
    private boolean checkIfEveryOrderIsPaid(List<Order> orders) throws ServiceException{
        for(Order order:orders){
            Invoice invTemplate = new Invoice();
            invTemplate.setOrders(Arrays.asList(order));
            if(invoiceService.findInvoices(invTemplate).isEmpty()){
                return false;
            }
        }
        return true;
    }


}
