package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * implementation of the ReservationService
 */
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    DAO<Reservation> reservationDAO;
    @Autowired
    Validator<Reservation> reservationValidator;

    @Override
    public List<Table> getFreeTables(Reservation reservation) throws ServiceException, ValidationException {
        return null;
    }

    @Override
    public void addReservation(Reservation reservation) throws ServiceException, ValidationException {

    }

    @Override
    public void updateReservation(Reservation reservation) throws ServiceException, ValidationException {

    }

    @Override
    public void cancelReservation(Reservation reservation) throws ServiceException, ValidationException {

    }

    @Override
    public List<Reservation> findReservation(Reservation matcher) throws ServiceException {
        return null;
    }

    @Override
    public List<Reservation> getAllReservations() throws ServiceException {
        return null;
    }

    @Override
    public List<History<Reservation>> getReservationHistory(Reservation reservation) throws ValidationException, ServiceException {
        return null;
    }
}
