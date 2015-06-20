package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.service.Service;

/**
 * TipService is used to calculate the tip and save it to the users
 */
public interface TipService extends Service {

    /**
     * The tip of the invoice will be separated in same quantity
     * to all users, which created the orders, which are on the given invoice
     * @param invoice the invoice, where the tip is included
     */
    public void calculateTheTipAndMatchItToUser(Invoice invoice);
}
