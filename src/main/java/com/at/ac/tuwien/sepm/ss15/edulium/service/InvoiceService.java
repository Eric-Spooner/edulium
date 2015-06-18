package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Instalment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("isAuthenticated()")
public interface InvoiceService extends Service {

    /**
     * Adds the invoice object to the underlying datasource.
     *
     * Sets the identity parameter of invoice if the data was stored
     * successfully and a new identity has been generated in the underlying
     * datasource
     * @param invoice Object to store
     * @throws ServiceException If the object couldn't be stored
     * @throws ValidationException If the object doesn't pass the validation
     */
    @PreAuthorize("hasRole('SERVICE')")
    void addInvoice(Invoice invoice) throws ServiceException, ValidationException;

    /**
     * Updates data of the invoice object in the underlying datasource.
     * @param invoice Object to update
     * @throws ServiceException If the object couldn't be updated
     * @throws ValidationException If the object doesn't pass the validation
     */
    @PreAuthorize("hasRole('SERVICE')")
    void updateInvoice(Invoice invoice) throws ServiceException, ValidationException;

    /**
     * Removes the object from the underlying datasource
     * @param invoice Object to delete
     * @throws ServiceException If the object couldn't be deleted
     * @throws ValidationException If the object doesn't pass the validation
     */
    @PreAuthorize("hasRole('SERVICE')")
    void deleteInvoice(Invoice invoice) throws ServiceException, ValidationException;

    /**
     * Returns all sections which parameters match the
     * parameters of the parameter object.
     *
     * All parameters with value NULL will not be used for matching
     * @param invoice Object used for matching
     * @return The list of matched sections
     * @throws ServiceException If the data couldn't be retrieved
     */
    List<Invoice> findInvoices(Invoice invoice) throws ServiceException;

    /**
     * @return Returns all stored sections
     * @throws ServiceException If the data couldn't be retrieved
     */
    List<Invoice> getAllInvoices() throws ServiceException;

    /**
     * @param invoice Object to get the history for
     * @return The history of changes to the object
     * @throws ServiceException If the history of the object couldn't be retrieved
     * @throws ValidationException If the object doesn't pass the validation
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<History<Invoice>> getInvoiceHistory(Invoice invoice) throws ServiceException, ValidationException;

    /**
     * Adds an instalment object to the underlying datasource.
     *
     * Sets the identity parameter of instalment if the data was stored
     * successfully and a new identity has been generated in the underlying
     * datasource
     * @param instalment Object to store
     * @throws ServiceException If the object couldn't be stored
     * @throws ValidationException If the object doesn't pass the validation
     */
    @PreAuthorize("hasRole('SERVICE')")
    void addInstalment(Instalment instalment) throws  ServiceException, ValidationException;

    /**
     * Returns all sections which parameters match the
     * parameters of the parameter object.
     * @param instalment Object used for matching
     * @return The list of matched sections
     * @throws ServiceException If the data couldn't be retrieved
     */
    @PreAuthorize("hasRole('SERVICE')")
    List<Instalment> findInstalments(Instalment instalment) throws ServiceException;

    /**
     * @return Returns all stored sections
     * @throws ServiceException If the data couldn't be retrieved
     */
    @PreAuthorize("hasRole('SERVICE')")
    List<Instalment> getAllInstalments() throws ServiceException;
}
