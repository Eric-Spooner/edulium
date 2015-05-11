package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * Created by - on 11.05.2015.
 */
public class MenuDAOImpl implements DAO<Menu> {
    @Override
    public void create(Menu object) throws DAOException, ValidationException {

    }

    @Override
    public void update(Menu object) throws DAOException, ValidationException {

    }

    @Override
    public void delete(Menu object) throws DAOException, ValidationException {
    }

    @Override
    public List<Menu> find(Menu object) throws DAOException {
        return null;
    }

    @Override
    public List<Menu> getAll() throws DAOException {
        return null;
    }

    @Override
    public List<History<Menu>> getHistory(Menu object) throws DAOException, ValidationException {
        return null;
    }
}
