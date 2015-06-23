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

        if (isManager(user)) {
            // the user is a manager, we need to perform some additional checks

            if (!user.getRole().equals("ROLE_MANAGER") && getNumberOfManagers() < 2) {
                // the role has been changed and we have only one manager left
                throw new ServiceException("Only one remaining manager exists, the role can't be changed");
            }
        }

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

        if (isManager(user)) {
            // the user is a manager, we need to perform some additional checks

            if (getNumberOfManagers() < 2) {
                // only one manager left
                throw new ServiceException("Only one remaining manager exists, the user can't be deleted");
            }
        }

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

    /**
     * Checks if the given user has the MANAGER role.
     * Uses the persistent version of user to perform the check.
     * @param user User
     * @return True if the user is a manager, false otherwise.
     * @throws ServiceException When the user doesn't exist
     */
    private boolean isManager(User user) throws ServiceException {
        List<User> existingUsers = findUsers(User.withIdentity(user.getIdentity()));
        if (existingUsers.isEmpty()) {
            throw new ServiceException("User does not exist");
        }

        User existingUser = existingUsers.get(0);
        return existingUser.getRole().equals("ROLE_MANAGER");
    }

    /**
     * Gets the number of available managers.
     * @return Number of managers
     * @throws ServiceException
     */
    private int getNumberOfManagers() throws ServiceException {
        User managerMatcher = new User();
        managerMatcher.setRole("ROLE_MANAGER");
        return findUsers(managerMatcher).size();
    }
}
