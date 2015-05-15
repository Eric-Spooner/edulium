package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * H2 Database Implementation of the Order DAO interface
 */
class DBOrderDAO implements DAO<Order> {
    @Override
    public void create(Order object) throws DAOException, ValidationException {

    }

    @Override
    public void update(Order object) throws DAOException, ValidationException {

    }

    @Override
    public void delete(Order object) throws DAOException, ValidationException {

    }

    @Override
    public List<Order> find(Order object) throws DAOException {
        return null;
    }

    @Override
    public List<Order> getAll() throws DAOException {
        return null;
    }

    @Override
    public List<History<Order>> getHistory(Order object) throws DAOException, ValidationException {
        return null;
    }
}
