package com.at.ac.tuwien.sepm.ss15.edulium.service.heuristics;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Reservation Heuristics Interface
 */
public abstract class ReservationHeuristic {
    @Resource(name = "reservationService")
    private ReservationService reservationService;

    public abstract List<Table> getTablesForReservation(Reservation reservation, List<Table> tables) throws ValidationException, ServiceException;

    protected List<Reservation> getOverlappingReservations(Reservation reservation) throws ServiceException, ValidationException {
        List<Reservation> reservations = reservationService.findReservationBetween(reservation.getTime(),
                reservation.getTime().plus(reservation.getDuration()));

        // ignore current reservation
        reservations.removeIf(res -> res.getIdentity().equals(reservation.getIdentity()));

        return reservations;
    }

}
