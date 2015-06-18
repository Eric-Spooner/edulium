package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Instalment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class InvoiceServiceImpl implements InvoiceService {
    private static final Logger LOGGER = LogManager.getLogger(InteriorServiceImpl.class);

    @Autowired
    DAO<Invoice> invoiceDAO;
    @Autowired
    Validator<Invoice> invoiceValidator;

    @Override
    public void addInvoice(Invoice invoice) throws ServiceException, ValidationException {
        LOGGER.debug("Entering addInvoice with parameters: " + invoice);
        invoiceValidator.validateForCreate(invoice);

        try {
            invoiceDAO.create(invoice);
        } catch (DAOException e) {
            throw new ServiceException("Could not add invoice", e);
        }
    }

    @Override
    public void updateInvoice(Invoice invoice) throws ServiceException, ValidationException {
        LOGGER.debug("Entering updateInvoice with parameters: " + invoice);
        invoiceValidator.validateForUpdate(invoice);

        try {
            invoiceDAO.update(invoice);
        } catch (DAOException e) {
            throw new ServiceException("Could not update invoice", e);
        }
    }

    @Override
    public void deleteInvoice(Invoice invoice) throws ServiceException, ValidationException {
        LOGGER.debug("Entering deleteInvoice with parameters: " + invoice);
        invoiceValidator.validateForDelete(invoice);

        try {
            invoiceDAO.delete(invoice);
        } catch (DAOException e) {
            throw new ServiceException("Could not delete invoice", e);
        }
    }

    @Override
    public List<Invoice> findInvoices(Invoice invoice) throws ServiceException {
        LOGGER.debug("Entering findInvoices with parameters: " + invoice);

        try {
            return invoiceDAO.find(invoice);
        } catch (DAOException e) {
            throw new ServiceException("Could not find invoices", e);
        }
    }

    @Override
    public List<Invoice> getAllInvoices() throws ServiceException {
        LOGGER.debug("Entering getAllInvoices");

        try {
            return invoiceDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Could not get all invoices", e);
        }
    }

    @Override
    public List<History<Invoice>> getInvoiceHistory(Invoice invoice) throws ServiceException, ValidationException {
        LOGGER.debug("Entering getInvoiceHistory with parameters: " + invoice);
        invoiceValidator.validateIdentity(invoice);

        try {
            return invoiceDAO.getHistory(invoice);
        } catch (DAOException e) {
            throw new ServiceException("Could not get invoice history", e);
        }
    }

    @Override
    public void addInstalment(Instalment instalment) throws ServiceException, ValidationException {
        // TODO
    }

    @Override
    public List<Instalment> findInstalments(Instalment instalment) throws ServiceException {
        // TODO
        return null;
    }

    @Override
    public List<Instalment> getAllInstalments() throws ServiceException {
        // TODO
        return null;
    }
}
