package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TipService;

import java.util.List;

/**
 * The Implementation of the Tip-Service
 */
class TipServiceImpl implements TipService {
    @Override
    public void calculateTheTipAndMatchItToUser(Invoice invoice) {
        for(Order order: invoice.getOrders()){

        }
    }
}
