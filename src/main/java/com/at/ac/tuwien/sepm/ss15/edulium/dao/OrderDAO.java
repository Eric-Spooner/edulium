package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * interface for the reservation dao
 */
public interface OrderDAO extends DAO<Order> {

    /**
     * returns all orders which were created in the specified interval
     * @param from start of the interval
     * @param to end of the interval
     * @return a list of orders
     * @throws DAOException if the data couldn't be retrieved
     * @throws ValidationException if the parameters are invalid
     */
    List<Order> findBetween(LocalDateTime from, LocalDateTime to) throws DAOException;
}
