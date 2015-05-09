package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * data access object for the user domain
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface OrderDAO {
    /**
     * writes the object order to the underlying datasource;
     *
     * @param order object to store
     * @throws DAOException if the object couldn't be stored
     */
    void create(Order order) throws DAOException, ValidationException;

    /**
     * updates data of object order in the underlying datasource
     * @param order object to update
     * @throws DAOException if the object couldn't be updated
     */
    void update(Order order) throws DAOException, ValidationException;

    /**
     * removes the object order from the underlying datasource
     * @param order object to remove
     * @throws DAOException if the object couldn't be removed
     */
    void delete(Order order) throws DAOException, ValidationException;

    /**
     * returns all objects which parameters match the parameters of the object user
     *
     * all parameters with value NULL will not be used for matching
     * @param order object used for matching
     * @return a list of matched objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<Order> find(Order order) throws DAOException;

    /**
     * @return returns all stored objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<Order> getAll() throws DAOException;
}
