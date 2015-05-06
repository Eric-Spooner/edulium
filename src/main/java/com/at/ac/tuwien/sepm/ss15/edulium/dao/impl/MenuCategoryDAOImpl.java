package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.MenuCategoryDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * H2 Database Implementation of the MenuCategoryDAO interface
 */
@Repository
class MenuCategoryDAOImpl implements MenuCategoryDAO {
    @Autowired
    private DataSource dataSource;

    @Override
    public void create(MenuCategory menuCategory) throws DAOException {

    }

    @Override
    public void update(MenuCategory menuCategory) throws DAOException {

    }

    @Override
    public void delete(MenuCategory menuCategory) throws DAOException {

    }

    @Override
    public List<MenuCategory> find(MenuCategory menuCategory) throws DAOException {
        return null;
    }

    @Override
    public List<MenuCategory> getAll() throws DAOException {
        return null;
    }
}
