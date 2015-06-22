package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * The Implementation of the Tip-Service
 */
class TipServiceImpl implements TipService {
    private static final Logger LOGGER = LogManager.getLogger(TipServiceImpl.class);

    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "orderService")
    private OrderService orderService;

    @Override
    public void divideAndMatchTip(Invoice invoice, BigDecimal tip) throws ServiceException, ValidationException{
        List<User> userList = new LinkedList<>();
        if(invoice.getOrders().size()==0){
            throw new ServiceException("Given Invoice has no orders");
        }
        for(Order order: invoice.getOrders()){
            if(!userList.contains(orderService.getOrderSubmitter(order))){
                userList.add(orderService.getOrderSubmitter(order));
            }
        }
        BigDecimal tipPerUser = tip.divide(BigDecimal.valueOf(userList.size()));
        for(User user: userList){
           user.setTip(user.getTip().add(tipPerUser));
           userService.updateUser(user);
        }
    }
}
