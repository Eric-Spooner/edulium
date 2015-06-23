package com.at.ac.tuwien.sepm.ss15.edulium.service.heuristics;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Reservation Heuristics Interface
 */
public abstract class ReservationHeuristic {
    @Resource(name = "reservationService")
    private ReservationService reservationService;

    public abstract List<Table> getTablesForReservation(Reservation reservation, List<Table> allTables) throws ValidationException, ServiceException;

    private List<Reservation> getOverlappingReservations(Reservation reservation) throws ServiceException, ValidationException {
        List<Reservation> reservations = reservationService.findReservationBetween(reservation.getTime(),
                reservation.getTime().plus(reservation.getDuration()));

        // ignore current reservation
        reservations.removeIf(res -> res.getIdentity().equals(reservation.getIdentity()));

        return reservations;
    }

    protected List<Table> getFreeTables( Reservation reservation, List<Table> allTables) throws ServiceException, ValidationException {
        List<Reservation> reservations = getOverlappingReservations(reservation);

        // create new arraylist because 't' could be immutable
        ArrayList<Table> tables = new ArrayList<>(allTables);

        // remove tables which are already reserved
        Iterator<Table> it = tables.iterator();
        while (it.hasNext()) {
            Table table = it.next();

            for (Reservation res : reservations) {
                if(res.getTables().contains(table)) {
                    it.remove();
                    break;
                }
            }
        }

        return  tables;
    }

}
