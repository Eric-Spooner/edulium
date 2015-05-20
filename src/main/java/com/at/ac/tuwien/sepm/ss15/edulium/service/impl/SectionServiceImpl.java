package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.SectionService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 19.05.2015.
 */
public class SectionServiceImpl implements SectionService {
    private static final Logger LOGGER = LogManager.getLogger(TableServiceImpl.class);

    @Autowired
    private DAO<Section> sectionDAO;
    @Autowired
    private DAO<Table> tableDAO;

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

    @Override
    public void addTable(Section section, Table table) throws ServiceException {
        LOGGER.debug("entering addTable with parameters " + section + ", " + table);

        if(table != null)
            table.setSection(section);

        try {
            tableDAO.create(table);
        } catch(DAOException e) {
            throw new ServiceException("couldn't add table to section", e);
        } catch(ValidationException e) {
            throw new ServiceException("couldn't validate table", e);
        }
    }

    @Override
    public List<Table> getAllTables(Section section) throws ServiceException {
        LOGGER.debug("entering getAllTables with parameters " + section);

        List<Table> sectionTables = new ArrayList<Table>();

        try {
            for(Table table : tableDAO.getAll()) {
                if(table.getSection().equals(section)) {
                    sectionTables.add(table);
                }
            }
        } catch(DAOException e) {
            throw new ServiceException("couldn't find tables", e);
        }

        return sectionTables;
    }
}
