package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

import java.util.List;

/**
 * H2 Database Implementation of the ReservationDAO interface
 */
class DBReservationDAO implements DAO<Reservation> {
    @Override
    public void create(Reservation reservation) throws DAOException, ValidationException {

    }

    @Override
    public void update(Reservation reservation) throws DAOException, ValidationException {

    }

    @Override
    public void delete(Reservation reservation) throws DAOException, ValidationException {

    }

    @Override
    public List<Reservation> find(Reservation reservation) throws DAOException {
        return null;
    }

    @Override
    public List<Reservation> getAll() throws DAOException {
        return null;
    }
}
