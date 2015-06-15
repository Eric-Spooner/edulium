package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * Implementation of the UserService interface
 */
class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Resource(name = "userDAO")
    private DAO<User> userDAO;
    @Resource(name = "userValidator")
    private Validator<User> userValidator;

    @Override
    public void addUser(User user) throws ServiceException, ValidationException {
        LOGGER.debug("Entering addUser with parameters: " + user);

        userValidator.validateForCreate(user);

        try {
            userDAO.create(user);
        } catch (DAOException e) {
            throw new ServiceException("Adding user has failed", e);
        }
    }

    @Override
    public void updateUser(User user) throws ServiceException, ValidationException {
        LOGGER.debug("Entering updateUser with parameters: " + user);

        userValidator.validateForUpdate(user);

        try {
            userDAO.update(user);
        } catch (DAOException e) {
            throw new ServiceException("Updating user has failed", e);
        }
    }

    @Override
    public void deleteUser(User user) throws ServiceException, ValidationException {
        LOGGER.debug("Entering deleteUser with parameters: " + user);

        userValidator.validateForDelete(user);

        try {
            userDAO.delete(user);
        } catch (DAOException e) {
            throw new ServiceException("Deleting user has failed", e);
        }
    }

    @Override
    public List<User> findUsers(User userTemplate) throws ServiceException {
        LOGGER.debug("Entering findUsers with parameters: " + userTemplate);

        try {
            return userDAO.find(userTemplate);
        } catch (DAOException e) {
            throw new ServiceException("Searching for users has failed", e);
        }
    }

    @Override
    public List<User> getAllUsers() throws ServiceException {
        LOGGER.debug("Entering getAllUsers");

        try {
            return userDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Searching for users has failed", e);
        }
    }
}
