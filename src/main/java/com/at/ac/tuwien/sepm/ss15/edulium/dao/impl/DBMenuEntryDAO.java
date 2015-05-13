package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * H2 Database Implementation of the MenuEntry DAO interface
 */
class DBMenuEntryDAO implements DAO<MenuEntry> {
    @Override
    public void create(MenuEntry object) throws DAOException, ValidationException {

    }

    @Override
    public void update(MenuEntry object) throws DAOException, ValidationException {

    }

    @Override
    public void delete(MenuEntry object) throws DAOException, ValidationException {

    }

    @Override
    public List<MenuEntry> find(MenuEntry object) throws DAOException {
        return null;
    }

    @Override
    public List<MenuEntry> getAll() throws DAOException {
        return null;
    }

    @Override
    public List<History<MenuEntry>> getHistory(MenuEntry object) throws DAOException, ValidationException {
        return null;
    }
}
