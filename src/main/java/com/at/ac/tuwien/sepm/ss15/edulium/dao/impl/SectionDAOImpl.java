package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

/**
 * Created by Administrator on 06.05.2015.
 */

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.SectionDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * H2 Database Implementation of the section interface
 */
@Repository
class SectionDAOImpl implements SectionDAO {
    @Autowired
    private DataSource dataSource;

    @Override
    public void create(Section section) throws DAOException {

    }

    @Override
    public void update(Section section) throws DAOException {

    }

    @Override
    public void delete(Section section) throws DAOException {

    }

    @Override
    public List<Section> find(Section section) throws DAOException {
        return null;
    }

    @Override
    public List<Section> getAll() throws DAOException {
        return null;
    }
}
