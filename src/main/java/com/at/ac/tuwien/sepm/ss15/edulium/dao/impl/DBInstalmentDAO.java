package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Instalment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * Database implementation of the DAO interface for Instalment objects
 */
class DBInstalmentDAO implements DAO<Instalment> {

    @Override
    public void create(Instalment object) throws DAOException, ValidationException {

    }

    @Override
    public void update(Instalment object) throws DAOException, ValidationException {

    }

    @Override
    public void delete(Instalment object) throws DAOException, ValidationException {

    }

    @Override
    public List<Instalment> find(Instalment object) throws DAOException {
        return null;
    }

    @Override
    public List<Instalment> getAll() throws DAOException {
        return null;
    }

    @Override
    public List<History<Instalment>> getHistory(Instalment object) throws DAOException, ValidationException {
        return null;
    }

    @Override
    public List<Instalment> populate(List<Instalment> objects) throws DAOException, ValidationException {
        return null;
    }
}
