package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

public interface InvoiceSigningService extends Service {
    /**
     * Signs the given invoice and adds the signature to the invoice object (signature attribute)

     * @param invoice The invoice which should be signed
     * @throws ServiceException If the invoice couldn't be signed
     * @throws ValidationException If the invoice doesn't pass the validation
     */
    void signInvoice(Invoice invoice) throws ServiceException, ValidationException;
}
