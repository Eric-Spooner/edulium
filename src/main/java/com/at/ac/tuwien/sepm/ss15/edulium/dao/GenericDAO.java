package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * Generic DAO interface
 */
public interface GenericDAO<T> {

    /**
     * writes the object to the underlying datasource;
     *
     * sets the identity parameter of object, if the data was stored
     * successfully
     * @param object object to store
     * @throws DAOException if the object couldn't be stored
     */
    void create(T object) throws DAOException, ValidationException;

    /**
     * updates data of the object in the underlying datasource
     * @param object object to update
     * @throws DAOException if the object couldn't be updated
     */
    void update(T object) throws DAOException, ValidationException;

    /**
     * removes the object from the underlying datasource
     * @param object object to remove
     * @throws DAOException if the object couldn't be removed
     */
    void delete(T object) throws DAOException, ValidationException;

    /**
     * returns all objects which parameters match the
     * parameters of the parameter object
     *
     * all parameters with value NULL will not be used for matching
     * @param object object used for matching
     * @return a list of matched objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<T> find(T object) throws DAOException;

    /**
     * @return returns all stored objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<T> getAll() throws DAOException;
}
