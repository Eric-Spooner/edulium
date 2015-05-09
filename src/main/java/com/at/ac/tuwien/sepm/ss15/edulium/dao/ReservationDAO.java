package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * data access object for the reservation domain
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface ReservationDAO {

    /**
     * writes the object reservation to the underlying datasource;
     *
     * sets the identity parameter of reservation, if the data was stored successfully
     * @param reservation object to store
     * @throws DAOException if the object couldn't be stored
     */
    void create(Reservation reservation) throws DAOException, ValidationException;

    /**
     * updates data of object reservation in the underlying datasource
     * @param reservation object to update
     * @throws DAOException if the object couldn't be updated
     */
    void update(Reservation reservation) throws DAOException, ValidationException;

    /**
     * removes the object reservation from the underlying datasource
     * @param reservation object to remove
     * @throws DAOException if the object couldn't be removed
     */
    void delete(Reservation reservation) throws DAOException, ValidationException;

    /**
     * returns all objects whose parameters match the parameters of the object reservation
     *
     * all parameters with value NULL will be ignored for matching
     * @param reservation object used for matching
     * @return a list of matched objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<Reservation> find(Reservation reservation) throws DAOException;

    /**
     * @return returns all stored objects
     * @throws DAOException if the data couldn't be retrieved
     */
    List<Reservation> getAll() throws DAOException;
}
