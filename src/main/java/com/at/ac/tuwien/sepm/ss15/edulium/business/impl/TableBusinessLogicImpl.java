package com.at.ac.tuwien.sepm.ss15.edulium.business.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.business.TableBusinessLogic;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
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
    public void addedOrder(Order order) throws ServiceException, ValidationException {
        assert order != null;
        assert order.getTable() != null;

        List<Table> tables = interiorService.findTables(order.getTable());
        if (tables.isEmpty()) {
            throw new ServiceException("Table not found");
        }
        Table table = tables.get(0);

        if (table.getUser() == null || checkIfNoOpenOrdersOnTable(table)) {
            table.setUser(orderService.getOrderSubmitter(order));
            interiorService.updateTable(table);
        }
    }

    @Override
    public void paidOrder(Order order) throws ServiceException, ValidationException {
        removedOrder(order); // same logic as removedOrder
    }

    @Override
    public void removedOrder(Order order) throws ServiceException, ValidationException {
        assert order != null;
        assert order.getTable() != null;

        List<Table> tables = interiorService.findTables(order.getTable());
        if (tables.isEmpty()) {
            throw new ServiceException("Table not found");
        }
        Table table = tables.get(0);

        if (checkIfNoOpenOrdersOnTable(table)){
            table.setUser(null);
            interiorService.updateTable(table);
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
        template.setPaid(false);
        return orderService.findOrder(template).isEmpty();
    }
}
