package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Service interface for user management
 */
public interface UserService extends Service {
    /**
     * Adds a new user to the underlying data source.
     *
     * @param user User which should be added (should be valid according to UserValidator.validateForCreate)
     * @throws ServiceException if the user couldn't be stored
     * @throws ValidationException if the given user is invalid
     */
    @PreAuthorize("hasRole('MANAGER')")
    void addUser(User user) throws ServiceException, ValidationException;

    /**
     * Updates an existing user in the underlying data source.
     *
     * @param user User which should be updated (should be valid according to UserValidator.validateForUpdate)
     * @throws ServiceException if the user couldn't be stored
     * @throws ValidationException if the given user is invalid
     */
    @PreAuthorize("hasRole('MANAGER')")
    void updateUser(User user) throws ServiceException, ValidationException;

    /**
     * Removes an existing user from the underlying data source.
     *
     * @param user User which should be removed (should be valid according to UserValidator.validateForDelete)
     * @throws ServiceException if the user couldn't be removed
     * @throws ValidationException if the given user is invalid
     */
    @PreAuthorize("hasRole('MANAGER')")
    void deleteUser(User user) throws ServiceException, ValidationException;

    /**
     * Returns all existing users from the underlying data source which match with the given userTemplate.
     *
     * @param userTemplate A template user object used for matching
     * @return A list of existing users which match with the given userTemplate
     * @throws ServiceException if searching for users wasn't possible
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<User> findUsers(User userTemplate) throws ServiceException;

    /**
     * Returns all existing users from the underlying data source.
     *
     * @return A list of existing users
     * @throws ServiceException if searching for users wasn't possible
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<User> getAllUsers() throws ServiceException;
}
