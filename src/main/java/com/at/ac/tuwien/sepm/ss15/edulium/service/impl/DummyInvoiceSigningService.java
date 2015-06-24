package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceSigningService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;

/**
 * ONLY FOR DEMONSTRATION PURPOSE
 * A dummy implementation of the invoice signing service.
 * Doesn't really sign the invoice, only sets the hash of an invoice as "pseudo" signature for demonstration.
 */
class DummyInvoiceSigningService implements InvoiceSigningService {
    private static final Logger LOGGER = LogManager.getLogger(DummyInvoiceSigningService.class);

    @Resource(name = "invoiceValidator")
    private Validator<Invoice> invoiceValidator;

    @Override
    public void signInvoice(Invoice invoice) throws ServiceException, ValidationException {
        LOGGER.debug("Enter signInvoice with parameter: " + invoice);

        invoiceValidator.validateForCreate(invoice);

        // unset identity and signature, so that we can hash the invoice without them
        Long identity = invoice.getIdentity();
        invoice.setIdentity(null);
        invoice.setSignature(null);

        String signature = Integer.toString(invoice.hashCode());
        invoice.setSignature(signature);

        // restore the identity
        invoice.setIdentity(identity);
    }
}
