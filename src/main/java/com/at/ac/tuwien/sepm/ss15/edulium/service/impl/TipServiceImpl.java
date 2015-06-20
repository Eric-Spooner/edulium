package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.service.OrderService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TipService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
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
    public void calculateTheTipAndMatchItToUser(Invoice invoice) {


        List<User> userList = new LinkedList<>();
        try {
            for(Order order: invoice.getOrders()){
                if(!userList.contains(orderService.getOrderSubmitter(order))){
                    userList.add(orderService.getOrderSubmitter(order));
                }
            }
            float tip;
            for(User user: userList){
                //TODO For each user add the tip
            }
        }catch (Exception e){
            LOGGER.error("The Service was not able to calculate and Match the tip /n" + e.toString());
        }
    }
}
