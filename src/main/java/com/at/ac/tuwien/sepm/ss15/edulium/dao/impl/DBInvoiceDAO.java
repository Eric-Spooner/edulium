package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * Database implementation of the DAO interface for Invoice objects
 */
class DBInvoiceDAO implements DAO<Invoice> {

    /**
     * Writes the invoice object into the database and sets the identity
     * parameter that was generated there
     */
    @Override
    public void create(Invoice invoice) throws DAOException, ValidationException {
        // TODO: Implement after writing the tests
    }

    /**
     * Updates the invoice object in the database
     */
    @Override
    public void update(Invoice invoice) throws DAOException, ValidationException {
        // TODO: Implement after writing the tests
    }

    /**
     * Removes the invoice object from the database
     */
    @Override
    public void delete(Invoice invoice) throws DAOException, ValidationException {
        // TODO: Implement after writing the tests
    }

    /**
     * Finds all invoice objects in the database which match the
     * parameters of the provided invoice object
     */
    @Override
    public List<Invoice> find(Invoice invoice) throws DAOException {
        // TODO: Implement after writing the tests
        return null;
    }

    /**
     * Retrieves all invoice entries from database
     */
    @Override
    public List<Invoice> getAll() throws DAOException {
        // TODO: Implement after writing the tests
        return null;
    }


    /**
     * Gets the history of changes for the invoice object from
     * the database
     */
    @Override
    public List<History<Invoice>> getHistory(Invoice object) throws DAOException, ValidationException {
        // TODO: Implement after writing the tests
        return null;
    }
}
