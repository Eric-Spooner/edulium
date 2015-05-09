package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface InvoiceDAO {

    /**
     * Writes the invoice object to the underlying datasource
     *
     * Sets the identity parameter of invoice, if the data was successfully stored
     * @param invoice Object to be stored
     * @throws DAOException Thrown in case the object cannot be stored
     * @throws ValidationException Thrown in case the validation of invoice fails
     */
    void create(Invoice invoice) throws DAOException, ValidationException;

    /**
     * Updates data of type invoice in the underlying datasource
     *
     * @param invoice The object to be updated
     * @throws DAOException Thrown in case the object could not be updated
     * @throws ValidationException Thrown in case the validation of invoice fails
     */
    void update(Invoice invoice) throws DAOException, ValidationException;

    /**
     * Removes the object invoice from the underlying datasource
     *
     * @param invoice The object to be deleted
     * @throws DAOException Thrown in case the object could not be deleted
     * @throws ValidationException Thrown in case the validation of invoice fails
     */
    void delete(Invoice invoice) throws DAOException, ValidationException;

    /**
     * Finds all invoice objects in the underlying datasource which match the
     * parameters of the provided invoice object
     *
     * @param invoice The invoice object containing parameters for the search query
     * @return A list containing the results of the query
     * @throws DAOException Thrown in case the search fails
     */
    List<Invoice> find(Invoice invoice) throws DAOException;

    /**
     * Retrieves all invoice objects stored in the underlying datasource
     *
     * @return A list containing all existing invoices
     * @throws DAOException Thrown in case the search fails
     */
    List<Invoice> getAll() throws DAOException;
}
