package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service handling the reservations for tables
 */
public interface ReservationService extends Service {

    /**
     * adds the reservation to the underlying datasource;
     * if the tables parameter of reservation is empty or null, this method automatically
     * chooses tables for the reservation
     * @param reservation reservation to add
     * @throws ServiceException if an error in the service or persistence layer has occurred
     *                          or no fitting tables could be found (if tables are assigned automatically)
     * @throws ValidationException if the reservation object is not valid for this action
     */
    @PreAuthorize("hasRole('SERVICE')")
    void addReservation(Reservation reservation) throws ServiceException, ValidationException;

    /**
     * updates a reservation object in the underlying datasource
     * @param reservation reservation to update
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('SERVICE')")
    void updateReservation(Reservation reservation) throws ServiceException, ValidationException;

    /**
     * removes a reservation object from the underlying datasource
     * @param reservation reservation to remove
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the data is invalid
     */
    @PreAuthorize("hasRole('SERVICE')")
    void cancelReservation(Reservation reservation) throws ServiceException, ValidationException;

    /**
     * returns all reservations from the underlying datasource which paramters
     * match the paramter of the matcher object
     * @param matcher matcher
     * @throws ServiceException if an error processing the request ocurred
     */
    List<Reservation> findReservation(Reservation matcher) throws ServiceException;

    /**
     * returns all reservations in the given interval
     * @param from start of the interval
     * @param to end of the interval
     * @throws ServiceException if an error processing the request ocurred
     * @throws ValidationException if the parameters are invalid
     */
    List<Reservation> findReservationBetween(LocalDateTime from, LocalDateTime to) throws ServiceException, ValidationException;

    /**
     * returns all reservations from the underlying datasource; ordered by date and time
     * @throws ServiceException if an error processing the request ocurred
     */
    List<Reservation> getAllReservations() throws ServiceException;

    /**
     * @param reservation object to get the history for
     * @return returns the history of changes for the object
     * @throws ValidationException if the reservation object parameters are
     *         not valid for this action
     * @throws ServiceException if an error in the service or persistence layer has occurred
     */
    @PreAuthorize("hasRole('MANAGER')")
    List<History<Reservation>> getReservationHistory(Reservation reservation) throws ValidationException, ServiceException;
}
