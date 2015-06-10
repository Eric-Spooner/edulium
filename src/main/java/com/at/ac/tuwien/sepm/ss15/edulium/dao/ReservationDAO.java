package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * interface for the reservation dao
 */
public interface ReservationDAO extends DAO<Reservation> {

    /**
     * returns all reservatoins which take place in the specified interval
     * @param from start of the interval
     * @param to end of the interval
     * @return a list of reservation
     * @throws DAOException if the data couldn't be retrieved
     * @throws ValidationException if the parameters are invalid
     */
    List<Reservation> findBetween(LocalDateTime from, LocalDateTime to) throws DAOException, ValidationException;
}
