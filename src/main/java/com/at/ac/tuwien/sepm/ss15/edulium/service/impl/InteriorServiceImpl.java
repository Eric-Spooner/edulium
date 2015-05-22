package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Implementation of InteriorService
 */
public class InteriorServiceImpl implements InteriorService {
    private static final Logger LOGGER = LogManager.getLogger(InteriorServiceImpl.class);

    @Autowired
    private DAO<Table> tableDAO;
    @Autowired
    private DAO<Section> sectionDAO;

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

    @Override
    public void addSection(Section section) throws ServiceException {
        LOGGER.debug("entering addSection with parameters " + section);

        try {
            sectionDAO.create(section);
        } catch(DAOException e) {
            throw new ServiceException("couldn't add section", e);
        } catch(ValidationException e) {
            throw new ServiceException("couldn't validate section", e);
        }
    }

    @Override
    public void updateSection(Section section) throws ServiceException {
        LOGGER.debug("entering updateSection with parameters " + section);

        try {
            sectionDAO.update(section);
        } catch(DAOException e) {
            throw new ServiceException("couldn't update section", e);
        } catch(ValidationException e) {
            throw new ServiceException("couldn't validate section", e);
        }
    }

    @Override
    public void deleteSection(Section section) throws ServiceException {
        LOGGER.debug("entering deleteSection with parameters " + section);

        try {
            sectionDAO.delete(section);
        } catch(DAOException e) {
            throw new ServiceException("couldn't delete section", e);
        } catch(ValidationException e) {
            throw new ServiceException("couldn't validate section", e);
        }
    }

    @Override
    public List<Section> findSections(Section section) throws ServiceException {
        LOGGER.debug("entering findSections with parameters " + section);

        try {
            return sectionDAO.find(section);
        } catch(DAOException e) {
            throw new ServiceException("couldn't find sections", e);
        }
    }

    @Override
    public List<Section> getAllSections() throws ServiceException {
        LOGGER.debug("entering getAllSections");

        try {
            return sectionDAO.getAll();
        } catch(DAOException e) {
            throw new ServiceException("couldn't find sections", e);
        }
    }
}
