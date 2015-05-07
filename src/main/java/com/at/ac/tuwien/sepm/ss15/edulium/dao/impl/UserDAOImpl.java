package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.UserDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * H2 Database Implementation of the UserDAO interface
 */
@Repository
public class UserDAOImpl implements UserDAO {
    @Override
    public void create(User user) throws DAOException, ValidationException {

    }

    @Override
    public void update(User user) throws DAOException, ValidationException {

    }

    @Override
    public void delete(User user) throws DAOException, ValidationException {

    }

    @Override
    public List<User> find(User user) throws DAOException {
        return null;
    }

    @Override
    public List<User> getAll() throws DAOException {
        return null;
    }
}
