package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;

/**
 * Implementation of InteriorService
 */
class InteriorServiceImpl implements InteriorService {
    private static final Logger LOGGER = LogManager.getLogger(InteriorServiceImpl.class);

    @Resource(name = "tableDAO")
    private DAO<Table> tableDAO;
    @Resource(name = "sectionDAO")
    private DAO<Section> sectionDAO;
    @Resource(name = "tableValidator")
    private Validator<Table> tableValidator;
    @Resource(name = "sectionValidator")
    private Validator<Section> sectionValidator;

    @Override
    public void addTable(Table table) throws ServiceException, ValidationException {
        LOGGER.debug("entering addTable with parameters " + table);
        tableValidator.validateForCreate(table);

        try {
            tableDAO.create(table);
        } catch(DAOException e) {
            throw new ServiceException("couldn't add table", e);
        }
    }

    @Override
    public void updateTable(Table table) throws ServiceException, ValidationException {
        LOGGER.debug("entering updateTable with parameters " + table);
        tableValidator.validateForUpdate(table);

        try {
            tableDAO.update(table);
        } catch(DAOException e) {
            throw new ServiceException("couldn't update table", e);
        }
    }

    @Override
    public void deleteTable(Table table) throws ServiceException, ValidationException {
        LOGGER.debug("entering deleteTable with parameters " + table);
        tableValidator.validateForDelete(table);

        try {
            tableDAO.delete(table);
        } catch(DAOException e) {
            throw new ServiceException("couldn't delete table", e);
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
    public void addSection(Section section) throws ServiceException, ValidationException {
        LOGGER.debug("entering addSection with parameters " + section);
        sectionValidator.validateForCreate(section);

        try {
            sectionDAO.create(section);
        } catch(DAOException e) {
            throw new ServiceException("couldn't add section", e);
        }
    }

    @Override
    public void updateSection(Section section) throws ServiceException, ValidationException {
        LOGGER.debug("entering updateSection with parameters " + section);
        sectionValidator.validateForUpdate(section);

        try {
            sectionDAO.update(section);
        } catch(DAOException e) {
            throw new ServiceException("couldn't update section", e);
        }
    }

    @Override
    public void deleteSection(Section section) throws ServiceException, ValidationException {
        LOGGER.debug("entering deleteSection with parameters " + section);
        sectionValidator.validateForDelete(section);

        try {
            sectionDAO.delete(section);
        } catch(DAOException e) {
            throw new ServiceException("couldn't delete section", e);
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
