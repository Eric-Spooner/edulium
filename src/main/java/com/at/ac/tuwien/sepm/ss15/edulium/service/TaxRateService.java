package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * service for the taxRate domain object
 */
public interface TaxRateService extends Service {
    /**
     * adds a taxRate object to the underlying datasource
     * @param taxRate taxRate to add
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('MANAGER')")
    void addTaxRate(TaxRate taxRate) throws ServiceException, ValidationException;

    /**
     * updates a taxRate object in the underlying datasource
     * @param taxRate taxRate to update
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('MANAGER')")
    void updateTaxRate(TaxRate taxRate) throws ServiceException, ValidationException;

    /**
     * removes a taxRate object from the underlying datasource
     * @param taxRate taxRate to remove
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('MANAGER')")
    void removeTaxRate(TaxRate taxRate) throws ServiceException, ValidationException;

    /**
     * returns all taxRates from the underlying datasource which paramters
     * match the paramter of the matcher object
     * @param matcher matcher
     * @throws ServiceException if an error processing the request ocurred
     */
    List<TaxRate> findTaxRate(TaxRate matcher) throws ServiceException;

    /**
     * returns all taxRates from the underlying datasource
     * @throws ServiceException if an error processing the request ocurred
     */
    List<TaxRate> getAllTaxRates() throws ServiceException;

    /**
     * @param taxRate object to get the history for
     * @return returns the history of changes for the object
     * @throws ValidationException if the taxRate object parameters are
     *         not valid for this action
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<History<TaxRate>> getTaxRateHistory(TaxRate taxRate) throws ValidationException, ServiceException;
}
