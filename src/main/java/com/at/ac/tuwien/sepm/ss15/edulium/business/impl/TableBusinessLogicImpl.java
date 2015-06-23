package com.at.ac.tuwien.sepm.ss15.edulium.business.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.business.TableBusinessLogic;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by - on 23.06.2015.
 */
class TableBusinessLogicImpl implements TableBusinessLogic {
    @Resource(name = "orderService")
    private OrderService orderService;
    @Resource(name = "interiorService")
    private InteriorService interiorService;

    @Override
    public void addedOrderToTable(Table table, Order order) throws ServiceException, ValidationException{
        if(table.getUser() == null || checkIfEveryOrderIsPaid(table)) {
            table.setUser(orderService.getOrderSubmitter(order));
            interiorService.updateTable(table);
        }
    }

    @Override
    public void paidOrderFromTable(Table table) throws ServiceException, ValidationException{
        if(checkIfEveryOrderIsPaid(table)){
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
    private boolean checkIfEveryOrderIsPaid(Table table) throws ServiceException{
        Order template = new Order();
        template.setTable(table);
        List<Order> orderList = orderService.findOrder(template);
        if(orderList.isEmpty()){
            return true;
        }else {
            for(Order order:orderList){
                if(!order.getState().equals(Order.State.PAID)){
                    return false;
                }
            }
            return true;
        }
    }
}
