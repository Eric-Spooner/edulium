package com.at.ac.tuwien.sepm.ss15.edulium.dao.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.ReservationDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * H2 Database Implementation of the ReservationDAO interface
 */
@Repository
class ReservationDAOImpl implements ReservationDAO {
    @Override
    public void create(Reservation reservation) throws DAOException {

    }

    @Override
    public void update(Reservation reservation) throws DAOException {

    }

    @Override
    public void delete(Reservation reservation) throws DAOException {

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
