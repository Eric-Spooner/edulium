package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;

/**
 * implementation of the taxRateService
 */
class TaxRateServiceImpl implements TaxRateService {
    private static final Logger LOGGER = LogManager.getLogger(TaxRateServiceImpl.class);

    @Resource(name = "taxRateDAO")
    private DAO<TaxRate> taxRateDAO;
    @Resource(name = "taxRateValidator")
    private Validator<TaxRate> taxRateValidator;

    @Override
    public void addTaxRate(TaxRate taxRate) throws ServiceException, ValidationException {
        LOGGER.debug("Entering addTaxRate with parameter: " + taxRate);

        taxRateValidator.validateForCreate(taxRate);

        try {
            taxRateDAO.create(taxRate);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public void updateTaxRate(TaxRate taxRate) throws ServiceException, ValidationException {
        LOGGER.debug("Entering updateTaxRate with parameter: " + taxRate);

        taxRateValidator.validateForUpdate(taxRate);

        try {
            taxRateDAO.update(taxRate);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public void removeTaxRate(TaxRate taxRate) throws ServiceException, ValidationException {
        LOGGER.debug("Entering removeTaxRate with parameter: " + taxRate);

        taxRateValidator.validateForDelete(taxRate);

        try {
            taxRateDAO.delete(taxRate);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public List<TaxRate> findTaxRate(TaxRate matcher) throws ServiceException {
        LOGGER.debug("Entering findTaxRate with parameter: " + matcher);

        try {
            return taxRateDAO.find(matcher);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public List<TaxRate> getAllTaxRates() throws ServiceException {
        LOGGER.debug("Entering getAllTaxRates");

        try {
            return taxRateDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public List<History<TaxRate>> getTaxRateHistory(TaxRate taxRate) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getTaxRateHistory with parameter: " + taxRate);

        taxRateValidator.validateIdentity(taxRate);

        try {
            return taxRateDAO.getHistory(taxRate);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }
}
