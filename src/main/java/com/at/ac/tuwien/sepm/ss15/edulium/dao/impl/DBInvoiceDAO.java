package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.InvoiceDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

public class DBInvoiceDAO implements InvoiceDAO {

    /**
     * Writes the invoice object into the database and sets the identity
     * parameter that was generated there
     * @param invoice Object to be stored
     * @throws DAOException Thrown if an error at the database occurred
     * @throws ValidationException Thrown if the validation of invoice failed
     */
    @Override
    public void create(Invoice invoice) throws DAOException, ValidationException {
        // TODO: Implement after writing the tests
    }

    /**
     * Updates the invoice object in the database
     * @param invoice Object to be updated
     * @throws DAOException Thrown if an error at the database occurred
     * @throws ValidationException Thrown if the validation of invoice failed
     */
    @Override
    public void update(Invoice invoice) throws DAOException, ValidationException {
        // TODO: Implement after writing the tests
    }

    /**
     * Removes the invoice object from the database
     * @param invoice Object to be deleted
     * @throws DAOException Thrown if an error at the database occurred
     * @throws ValidationException Thrown if the validation of invoice failed
     */
    @Override
    public void delete(Invoice invoice) throws DAOException, ValidationException {
        // TODO: Implement after writing the tests
    }

    /**
     * Finds all invoice objects in the database which match the
     * parameters of the provided invoice object
     * @param invoice The invoice object containing parameters for the search query
     * @return A list containing the results from the query
     * @throws DAOException Thrown if an error at the database occurred
     */
    @Override
    public List<Invoice> find(Invoice invoice) throws DAOException {
        // TODO: Implement after writing the tests
        return null;
    }

    /**
     * Retrieves all invoice entries from database
     * @return A list containing all invoices
     * @throws DAOException Thrown if an error at the database occurred
     */
    @Override
    public List<Invoice> getAll() throws DAOException {
        // TODO: Implement after writing the tests
        return null;
    }
}
