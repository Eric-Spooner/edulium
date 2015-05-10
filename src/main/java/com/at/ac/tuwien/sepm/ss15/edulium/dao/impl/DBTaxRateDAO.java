package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * H2 Database Implementation of the TaxRate DAO interface
 */
class DBTaxRateDAO implements DAO<TaxRate> {
    @Override
    public void create(TaxRate object) throws DAOException, ValidationException {

    }

    @Override
    public void update(TaxRate object) throws DAOException, ValidationException {

    }

    @Override
    public void delete(TaxRate object) throws DAOException, ValidationException {

    }

    @Override
    public List<TaxRate> find(TaxRate object) throws DAOException {
        return null;
    }

    @Override
    public List<TaxRate> getAll() throws DAOException {
        return null;
    }
}
