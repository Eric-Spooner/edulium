package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.ImmutableDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.InvoiceDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Instalment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ImmutableValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InvoiceService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class InvoiceServiceImpl implements InvoiceService {
    private static final Logger LOGGER = LogManager.getLogger(InteriorServiceImpl.class);

    @Resource(name = "invoiceDAO")
    DAO<Invoice> invoiceDAO;

    @Resource(name = "invoiceDAO")
    InvoiceDAO findBetweenInvoiceDAO;

    @Resource(name = "instalmentDAO")
    ImmutableDAO<Instalment> instalmentDAO;

    @Resource(name = "invoiceValidator")
    Validator<Invoice> invoiceValidator;

    @Resource(name = "instalmentValidator")
    ImmutableValidator<Instalment> instalmentValidator;

    @Override
    public void addInvoice(Invoice invoice) throws ServiceException, ValidationException {
        LOGGER.debug("Entering addInvoice with parameters: " + invoice);
        invoiceValidator.validateForCreate(invoice);
        updateGross(invoice);

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
        updateGross(invoice);

        try {
            invoiceDAO.update(invoice);
        } catch (DAOException e) {
            throw new ServiceException("Could not update invoice", e);
        }
    }

    private void updateGross(Invoice invoice) {
        if (invoice.getOrders() != null) {
            BigDecimal sum = BigDecimal.ZERO;
            for (Order order : invoice.getOrders()) {
                sum = sum.add(order.getBrutto());
            }
            invoice.setGross(sum);
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
    public List<Invoice> findInvoiceBetween(LocalDateTime from, LocalDateTime to) throws ServiceException, ValidationException {
        LOGGER.debug("Entering findInvoicesBetween with parameters: " + from + ", "+ to);

        try {
            return findBetweenInvoiceDAO.findBetween(from, to);
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
        LOGGER.debug("Entering addInstalment with parameters: " + instalment);
        instalmentValidator.validateForCreate(instalment);

        try {
            instalmentDAO.create(instalment);
        } catch (DAOException e) {
            throw new ServiceException("Could not add instalment", e);
        }
    }

    @Override
    public List<Instalment> findInstalments(Instalment instalment) throws ServiceException {
        LOGGER.debug("Entering findInstalment with parameters: " + instalment);

        try {
            return instalmentDAO.find(instalment);
        } catch (DAOException e) {
            throw new ServiceException("Could not find instalments", e);
        }
    }

    @Override
    public List<Instalment> getAllInstalments() throws ServiceException {
        LOGGER.debug("Entering getAllInstalments");

        try {
            return instalmentDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Could not get all instalments", e);
        }
    }
}
