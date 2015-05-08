package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * data access object for the user domain
 */
public interface UserDAO {
    /**
     * writes the object user to the underlying datasource;
     *
     * @param user object to store
     * @throws DAOException if the object couldn't be stored
     */
    void create(User user) throws DAOException, ValidationException;

    /**
     * updates data of object user in the underlying datasource
     * @param user object to update
     * @throws DAOException if the object couldn't be updated
     */
    void update(User user) throws DAOException, ValidationException;

    /**
     * removes the object user from the underlying datasource
     * @param user object to remove
     * @throws DAOException if the object couldn't be removed
     */
    void delete(User user) throws DAOException, ValidationException;

    /**
     * returns all objects which parameters match the parameters of the object user
     *
     * all parameters with value NULL will not be used for matching
     * @param user object used for matching
     * @return a list of matched objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<User> find(User user) throws DAOException;

    /**
     * @return returns all stored objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<User> getAll() throws DAOException;
}
