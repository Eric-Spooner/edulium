package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.TableDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * H2 Database Implementation of the MenuCategoryDAO interface
 */
@Repository
class TableDAOImpl implements TableDAO {
    @Autowired
    private DataSource dataSource;

    @Override
    public void create(Table table) throws DAOException {

    }

    @Override
    public void update(Table table) throws DAOException {

    }

    @Override
    public void delete(Table table) throws DAOException {

    }

    @Override
    public List<Table> find(Table table) throws DAOException {
        return null;
    }

    @Override
    public List<Table> getAll() throws DAOException {
        return null;
    }
}