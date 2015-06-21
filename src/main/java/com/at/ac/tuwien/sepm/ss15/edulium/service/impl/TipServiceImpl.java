package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TipService;
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

    @Resource(name = "orderService")
    private OrderService orderService;

    @Override
    public void calculateTheTipPerUserAndMatchItToUser(Invoice invoice, BigDecimal tip) throws ServiceException{
        List<User> userList = new LinkedList<>();
        try {
            for(Order order: invoice.getOrders()){
                if(!userList.contains(orderService.getOrderSubmitter(order))){
                    userList.add(orderService.getOrderSubmitter(order));
                }
            }
            BigDecimal tipPerUser = tip.divide(BigDecimal.valueOf(userList.size()));
            for(User user: userList){
               user.setTip(user.getTip().add(tipPerUser));
            }
        }catch (Exception e){
            LOGGER.error("The Service was not able to calculate and Match the tip /n" + e.toString());
        }
    }
}
