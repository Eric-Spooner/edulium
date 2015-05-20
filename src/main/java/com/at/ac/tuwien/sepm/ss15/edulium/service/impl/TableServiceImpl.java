package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.TableService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 19.05.2015.
 */
public class TableServiceImpl implements TableService {
    private static final Logger LOGGER = LogManager.getLogger(TableServiceImpl.class);

    @Autowired
    private DAO<Table> tableDAO;

    @Override
    public void addTable(Table table) throws ServiceException {
        LOGGER.debug("entering addTable with parameters " + table);

        try {
            tableDAO.create(table);
        } catch(DAOException e) {
            throw new ServiceException("couldn't add table", e);
        } catch(ValidationException e) {
            throw new ServiceException("couldn't validate table", e);
        }
    }

    @Override
    public void updateTable(Table table) throws ServiceException {
        LOGGER.debug("entering updateTable with parameters " + table);

        try {
            tableDAO.update(table);
        } catch(DAOException e) {
            throw new ServiceException("couldn't update table", e);
        } catch(ValidationException e) {
            throw new ServiceException("couldn't validate table", e);
        }
    }

    @Override
    public void deleteTable(Table table) throws ServiceException {
        LOGGER.debug("entering deleteTable with parameters " + table);

        try {
            tableDAO.delete(table);
        } catch(DAOException e) {
            throw new ServiceException("couldn't delete table", e);
        } catch(ValidationException e) {
            throw new ServiceException("couldn't validate table", e);
        }
    }

    @Override
    public List<Table> findTables(Table table) throws ServiceException {
        LOGGER.debug("entering findTables with parameters " + table);

        try {
            return tableDAO.find(table);
        } catch(DAOException e) {
            throw new ServiceException("couldn't find tables", e);
        }
    }

    @Override
    public List<Table> getAllTables() throws ServiceException {
        LOGGER.debug("entering getAllTables");

        try {
            return tableDAO.getAll();
        } catch(DAOException e) {
            throw new ServiceException("couldn't find tables", e);
        }
    }
}
