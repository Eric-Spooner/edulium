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
import java.util.HashSet;
import java.util.Set;

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
    public void divideAndMatchTip(Invoice invoice, BigDecimal tip) throws ServiceException, ValidationException {
        LOGGER.debug("Enter divideAndMatchTip with parameters: " + invoice + ", " + tip);

        if (invoice.getOrders() == null || invoice.getOrders().isEmpty()) {
            throw new ServiceException("Given Invoice has no orders");
        }

        Set<User> users = new HashSet<>();
        for (Order order: invoice.getOrders()){
            users.add(orderService.getOrderSubmitter(order));
        }

        assert !users.isEmpty();

        BigDecimal tipPerUser = tip.divide(BigDecimal.valueOf(users.size()));
        for (User user: users) {
           user.setTip(user.getTip().add(tipPerUser));
           userService.updateUser(user);
        }
    }
}
