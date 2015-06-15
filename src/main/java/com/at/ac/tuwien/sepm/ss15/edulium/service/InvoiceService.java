package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

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
     * Updates the invoice id in the entries of order in the underlying datasource
     * @param invoice The invoice
     * @param orders The orders list
     * @throws ServiceException If the update fails
     * @throws ValidationException If an object doesn't pass the validation
     */
    @PreAuthorize("hasRole('SERVICE')")
    void setOrders(Invoice invoice, List<Order> orders) throws ServiceException, ValidationException;

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
    List<History<Invoice>> getInvoiceHistory(Invoice invoice) throws ServiceException, ValidationException;
}
