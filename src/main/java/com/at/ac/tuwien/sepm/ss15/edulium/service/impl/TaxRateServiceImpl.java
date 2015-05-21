package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TaxRateService;

import java.util.List;

/**
 * implementation of the taxRateService
 */
public class TaxRateServiceImpl implements TaxRateService {
    @Override
    public void addTaxRate(TaxRate taxRate) throws ServiceException, ValidationException {

    }

    @Override
    public void updateTaxRate(TaxRate taxRate) throws ServiceException, ValidationException {

    }

    @Override
    public void removeTaxRate(TaxRate taxRate) throws ServiceException, ValidationException {

    }

    @Override
    public List<TaxRate> findTaxRate(TaxRate matcher) throws ServiceException, ValidationException {
        return null;
    }

    @Override
    public List<TaxRate> getAllTaxRates() throws ServiceException, ValidationException {
        return null;
    }
}
