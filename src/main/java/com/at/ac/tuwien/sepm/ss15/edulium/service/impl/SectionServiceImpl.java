package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.service.SectionService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import java.util.List;

/**
 * Created by Administrator on 19.05.2015.
 */
public class SectionServiceImpl implements SectionService {
    @Override
    public void addSection(Section section) throws ServiceException {

    }

    @Override
    public void updateSection(Section section) throws ServiceException {

    }

    @Override
    public void deleteSection(Section section) throws ServiceException {

    }

    @Override
    public List<Section> findSections(Section section) throws ServiceException {
        return null;
    }

    @Override
    public List<Section> getAllSections() throws ServiceException {
        return null;
    }

    @Override
    public void addTable(Section section, Table table) throws ServiceException {

    }

    @Override
    public List<Table> getAllTables(Section section) throws ServiceException {
        return null;
    }
}
