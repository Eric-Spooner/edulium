package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Generic DAO interface
 */
@Repository
@PreAuthorize("isAuthenticated()")
@Transactional(propagation = Propagation.MANDATORY)
public interface DAO<T> {

    /**
     * writes the object to the underlying datasource;
     *
     * sets the identity parameter of object if the data was stored
     * successfully and a new identity has been generated by the
     * underlying datasource
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

    /**
     * @param object object to get the history for
     * @return returns the history of changes for the object
     * @throws DAOException if the data couldn't be retrieved
     * @throws ValidationException if the object parameters are
     *         not valid for this action
     */
    List<History<T>> getHistory(T object) throws DAOException, ValidationException;

    /**
     * populates the given list of objects (ignores if the object is marked as deleted)
     * @param objects A list of objects which should be fully populated (all objects must have a valid identity)
     * @return fully populated list of objects
     * @throws DAOException if the data couldn't be retrieved
     * @throws ValidationException if not all objects have a valid identity
     */
    List<T> populate(List<T> objects) throws DAOException, ValidationException;
}
