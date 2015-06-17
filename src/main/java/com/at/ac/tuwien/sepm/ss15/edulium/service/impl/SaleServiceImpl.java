package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.SaleService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Implementation of the SaleService
 */
public class SaleServiceImpl implements SaleService {
    private static final Logger LOGGER = LogManager.getLogger(SaleServiceImpl.class);

    @Autowired
    private DAO<IntermittentSale> intermittentSaleDAO;
    @Autowired
    private DAO<OnetimeSale> onetimeSaleDAO;
    @Autowired
    private Validator<IntermittentSale> intermittentSaleValidator;
    @Autowired
    private Validator<OnetimeSale> onetimeSaleValidator;

    @Override
    public void addIntermittentSale(IntermittentSale intermittentSale) throws ValidationException, ServiceException {
        LOGGER.debug("Entering addIntermittentSaleEntry with parameter: " + intermittentSale);

        intermittentSaleValidator.validateForCreate(intermittentSale);

        try {
            intermittentSaleDAO.create(intermittentSale);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void updateIntermittentSale(IntermittentSale intermittentSale) throws ValidationException, ServiceException {
        LOGGER.debug("Entering updateIntermittentSaleEntry with parameter: " + intermittentSale);

        intermittentSaleValidator.validateForUpdate(intermittentSale);

        try {
            intermittentSaleDAO.update(intermittentSale);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void removeIntermittentSale(IntermittentSale intermittentSale) throws ValidationException, ServiceException {
        LOGGER.debug("Entering removeIntermittentSaleEntry with parameter: " + intermittentSale);

        intermittentSaleValidator.validateForDelete(intermittentSale);

        try {
            intermittentSaleDAO.delete(intermittentSale);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<IntermittentSale> findIntermittentSale(IntermittentSale matcher) throws ServiceException {
        LOGGER.debug("Entering findIntermittentSaleEntry with parameter: " + matcher);

        try {
            return intermittentSaleDAO.find(matcher);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<IntermittentSale> getAllIntermittentSales() throws ServiceException {
        LOGGER.debug("Entering getAllIntermittentSaleEntries");

        try {
            return intermittentSaleDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object",e);
        }
    }

    @Override
    public List<History<IntermittentSale>> getIntermittentSaleHistory(IntermittentSale intermittentSale) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getIntermittentSaleEntryHistory with parameter: " + intermittentSale);

        intermittentSaleValidator.validateIdentity(intermittentSale);

        try {
            return intermittentSaleDAO.getHistory(intermittentSale);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void addOnetimeSale(OnetimeSale onetimeSale) throws ValidationException, ServiceException {
        LOGGER.debug("Entering addOnetimeSale with parameter: " + onetimeSale);

        onetimeSaleValidator.validateForCreate(onetimeSale);

        try {
            onetimeSaleDAO.create(onetimeSale);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void updateOnetimeSale(OnetimeSale onetimeSale) throws ValidationException, ServiceException {
        LOGGER.debug("Entering updateOnetimeSale with parameter: " + onetimeSale);

        onetimeSaleValidator.validateForUpdate(onetimeSale);

        try {
            onetimeSaleDAO.update(onetimeSale);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public void removeOnetimeSale(OnetimeSale onetimeSale) throws ValidationException, ServiceException {
        LOGGER.debug("Entering removeOnetimeSale with parameter: " + onetimeSale);

        onetimeSaleValidator.validateForDelete(onetimeSale);

        try {
            onetimeSaleDAO.delete(onetimeSale);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<OnetimeSale> findOnetimeSale(OnetimeSale matcher) throws ServiceException {
        LOGGER.debug("Entering removeOnetimeSale with parameter: " + matcher);

        try {
            return onetimeSaleDAO.find(matcher);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<OnetimeSale> getAllOnetimeSales() throws ServiceException {
        LOGGER.debug("Entering getAllOnetimeSales");

        try {
            return onetimeSaleDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }

    @Override
    public List<History<OnetimeSale>> getOnetimeSaleHistory(OnetimeSale onetimeSale) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getOnetimeSaleHistory with parameter: " + onetimeSale);

        onetimeSaleValidator.validateIdentity(onetimeSale);

        try {
            return onetimeSaleDAO.getHistory(onetimeSale);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object", e);
        }
    }
}