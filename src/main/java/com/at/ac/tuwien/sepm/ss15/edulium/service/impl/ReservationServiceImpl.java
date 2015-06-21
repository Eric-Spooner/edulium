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
import org.mockito.cglib.core.Local;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * implementation of the ReservationService
 */
class ReservationServiceImpl implements ReservationService {
    private static final Logger LOGGER = LogManager.getLogger(MenuServiceImpl.class);

    @Resource(name = "reservationDAO")
    private ReservationDAO reservationDAO;
    @Resource(name = "reservationValidator")
    private Validator<Reservation> reservationValidator;
    @Resource(name = "interiorService")
    private InteriorService interiorService;
    @Resource(name = "reservationHeuristic")
    private ReservationHeuristic reservationHeuristic;

    @Override
    public void addReservation(Reservation reservation) throws ServiceException, ValidationException {
        LOGGER.debug("Entering addReservation with parameter: " + reservation);

        reservationValidator.validateForCreate(reservation);

        validateReservationTime(reservation);

        try {
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

        Reservation originalReservation = getReservationById(reservation.getIdentity());
        validateReservationTime(originalReservation);

        // set time / duration for validation
        if(reservation.getDuration() == null) {
            reservation.setDuration(originalReservation.getDuration());
        }

        if(reservation.getTime() == null) {
            reservation.setTime(originalReservation.getTime());
        }
        validateReservationTime(reservation);

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

        reservation = getReservationById(reservation.getIdentity());
        validateReservationTime(reservation);

        try {
            reservationDAO.delete(reservation);
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }
    }

    @Override
    public void findTablesForReservation(Reservation reservation) throws ServiceException, ValidationException {
        LOGGER.debug("Entering findTablesForReservation with parameter: " + reservation);

        reservationValidator.validateForCreate(reservation);

        validateReservationTime(reservation);

        List<Table> tables = reservationHeuristic.getTablesForReservation(reservation, interiorService.getAllTables());

        if(tables.isEmpty()) {
            LOGGER.debug("no free tables found");
            throw new ServiceException("no free tables found");
        }

        reservation.setTables(tables);
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
    public List<Reservation> findReservationBetween(LocalDateTime from, LocalDateTime to) throws ServiceException, ValidationException {
        LOGGER.debug("Entering findReservationBetween with parameters: " + from + " / " + to);

        try {
            return reservationDAO.findBetween(from, to);
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

    private void validateReservationTime(Reservation reservation) throws ServiceException {
        LocalDateTime resEndTime = reservation.getTime().plus(reservation.getDuration());

        if (resEndTime.isBefore(LocalDateTime.now())) {
            LOGGER.error("cannot add/update/cancel past reservations");
            throw new ServiceException("cannot add/update/cancel past reservations");
        }
    }

    private Reservation getReservationById(long id) throws ServiceException {
        List<Reservation> resList = null;
        try {
            resList = reservationDAO.find(Reservation.withIdentity(id));
        } catch (DAOException e) {
            LOGGER.error("An Error has occurred in the data access object", e);
            throw new ServiceException("An Error has occurred in the data access object");
        }

        if(resList.size() != 1) {
            LOGGER.error("reservation not found");
            throw new ServiceException("reservation not found");
        }

        return resList.get(0);
    }
}
