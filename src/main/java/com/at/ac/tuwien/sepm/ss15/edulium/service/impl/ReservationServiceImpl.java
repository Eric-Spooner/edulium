package com.at.ac.tuwien.sepm.ss15.edulium.service.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.ReservationDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.heuristics.ReservationHeuristic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * implementation of the ReservationService
 */
class ReservationServiceImpl implements ReservationService {
    private static final Logger LOGGER = LogManager.getLogger(MenuServiceImpl.class);
    @Autowired
    private ReservationDAO reservationDAO;
    @Autowired
    private Validator<Reservation> reservationValidator;
    @Autowired
    private InteriorService interiorService;
    @Autowired
    private ReservationHeuristic reservationHeuristic;

    @Override
    public void addReservation(Reservation reservation) throws ServiceException, ValidationException {
        LOGGER.debug("Entering addReservation with parameter: " + reservation);

        reservationValidator.validateForCreate(reservation);

        LocalDateTime resEndTime = reservation.getTime().plus(reservation.getDuration());

        if (resEndTime.isBefore(LocalDateTime.now())) {
            LOGGER.error("Cannot add past reservation");
            throw new ServiceException("Cannot add past reservation");
        }

        try {
            List<Table> tables = reservationHeuristic.getTablesForReservation(reservation, interiorService.getAllTables());

            if(tables.isEmpty()) {
                LOGGER.error("Could not find any free tables");
                throw new ServiceException("Could not find any free tables");
            }

            reservation.setTables(tables);
            reservationDAO.create(reservation);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public void updateReservation(Reservation reservation) throws ServiceException, ValidationException {
        LOGGER.debug("Entering updateReservation with parameter: " + reservation);

        reservationValidator.validateForUpdate(reservation);

        if(reservation.getTime() != null && reservation.getDuration() != null) {
            LocalDateTime resEndTime = reservation.getTime().plus(reservation.getDuration());

            if (resEndTime.isBefore(LocalDateTime.now())) {
                LOGGER.error("Cannot update past reservation");
                throw new ServiceException("Cannot update past reservation");
            }
        }

        try {
            reservationDAO.update(reservation);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public void cancelReservation(Reservation reservation) throws ServiceException, ValidationException {
        LOGGER.debug("Entering cancelReservation with parameter: " + reservation);

        reservationValidator.validateForDelete(reservation);

        try {
            List<Reservation> resList = reservationDAO.find(Reservation.withIdentity(reservation.getIdentity()));

            if(resList.isEmpty()) {
                LOGGER.error("reservation not found");
                throw new ServiceException("reservation not found");
            }

            reservation = resList.get(0);
            LocalDateTime resEndTime = reservation.getTime().plus(reservation.getDuration());

            if(resEndTime.isBefore(LocalDateTime.now())) {
                LOGGER.error("Cannot cancel past reservation");
                throw new ServiceException("Cannot cancel past reservation");
            }

            reservationDAO.delete(reservation);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public List<Reservation> findReservation(Reservation matcher) throws ServiceException {
        LOGGER.debug("Entering findReservation with parameter: " + matcher);

        try {
            return reservationDAO.find(matcher);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public List<Reservation> findReservationIn(LocalDateTime start, Duration duration) throws ServiceException, ValidationException {
        LOGGER.debug("Entering findReservationIn with parameters: " + start + " / " + duration);

        try {
            return reservationDAO.findIn(start, duration);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public List<Reservation> getAllReservations() throws ServiceException {
        LOGGER.debug("Entering getAllReservations");

        try {
            return reservationDAO.getAll();
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public List<History<Reservation>> getReservationHistory(Reservation reservation) throws ValidationException, ServiceException {
        LOGGER.debug("Entering getReservationHistory with parameter: " + reservation);

        reservationValidator.validateIdentity(reservation);

        try {
            return reservationDAO.getHistory(reservation);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }
}
