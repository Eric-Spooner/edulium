package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.UserService;

import java.util.List;

/**
 * Implementation of the UserService
 */
class UserServiceImpl implements UserService {
    @Override
    public void addUser(User user) throws ServiceException, ValidationException {

    }

    @Override
    public void updateUser(User user) throws ServiceException, ValidationException {

    }

    @Override
    public void deleteUser(User user) throws ServiceException, ValidationException {

    }

    @Override
    public List<User> findUsers(User userTemplate) throws ServiceException {
        return null;
    }

    @Override
    public List<User> getAllUsers() throws ServiceException {
        return null;
    }
}
