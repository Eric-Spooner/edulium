package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import java.util.List;

class InvoiceServiceImpl implements InvoiceService {

    @Override
    public void addInvoice(Invoice invoice) throws ServiceException, ValidationException {

    }

    @Override
    public void updateInvoice(Invoice invoice) throws ServiceException, ValidationException {

    }

    @Override
    public void deleteInvoice(Invoice invoice) throws ServiceException, ValidationException {

    }

    @Override
    public List<Invoice> findInvoices(Invoice invoice) throws ServiceException {
        return null;
    }

    @Override
    public List<Invoice> getAllInvoices() throws ServiceException {
        return null;
    }

    @Override
    public List<History<Invoice>> getInvoiceHistory(Invoice invoice) throws ServiceException, ValidationException {
        return null;
    }
}
