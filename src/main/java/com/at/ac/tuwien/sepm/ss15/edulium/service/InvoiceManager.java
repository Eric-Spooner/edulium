package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;

public interface InvoiceManager {

    /**
     * Generates an invoice for the customer and manages it
     * @param invoice The invoice object we want to generate
     * @throws ServiceException In case something goes wrong
     */
    void manageInvoice(Invoice invoice) throws ServiceException;
}
