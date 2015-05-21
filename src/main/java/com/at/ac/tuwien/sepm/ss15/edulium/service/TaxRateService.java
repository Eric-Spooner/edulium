package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * service for the taxRate domain object
 */
public interface TaxRateService {

    /**
     * adds a taxRate object to the underlying datasource
     * @param taxRate taxRate to add
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    void addTaxRate(TaxRate taxRate) throws ServiceException, ValidationException;

    /**
     * updates a taxRate object in the underlying datasource
     * @param taxRate taxRate to update
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    void updateTaxRate(TaxRate taxRate) throws ServiceException, ValidationException;

    /**
     * removes a taxRate object from the underlying datasource
     * @param taxRate taxRate to remove
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    void removeTaxRate(TaxRate taxRate) throws ServiceException, ValidationException;

    /**
     * returns all taxRates from the underlying datasource which paramters
     * match the paramter of the matcher object
     * @param matcher matcher
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    List<TaxRate> findTaxRate(TaxRate matcher) throws ServiceException, ValidationException;

    /**
     * returns all taxRates from the underlying datasource
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    List<TaxRate> getAllTaxRates() throws ServiceException, ValidationException;

}
