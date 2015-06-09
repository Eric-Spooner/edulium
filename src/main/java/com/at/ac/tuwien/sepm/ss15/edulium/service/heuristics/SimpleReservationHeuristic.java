package com.at.ac.tuwien.sepm.ss15.edulium.service.heuristics;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * a simple reservation heuristic
 */
public class SimpleReservationHeuristic implements ReservationHeuristic {
    @Autowired
    private ReservationService reservationService;

    @Override
    public List<Table> getTablesForReservation(Reservation reservation, List<Table> t) throws ValidationException, ServiceException {
        List<Reservation> reservations = reservationService.findReservationIn(reservation.getTime(), reservation.getDuration());

        // create new arraylist because 't' could be immutable
        ArrayList<Table> tables = new ArrayList<>(t);

        // remove tables which are already reserved
        Iterator<Table> it = tables.iterator();
        while(it.hasNext()) {
            Table table = it.next();

            for(Reservation res : reservations) {
                if(res.getTables().contains(table)) {
                    it.remove();
                    break;
                }
            }
        }

        // check if a single table would fit for this reservation
        for(Table table : tables) {
            if(table.getSeats() == reservation.getQuantity()) {
                return Arrays.asList(table);
            }
        }

        int seatsLeft = reservation.getQuantity();
        List<Table> tablesForReservation = new ArrayList<>();

        // check multiple tables
        for(Table table : tables) {
            tablesForReservation.add(table);
            seatsLeft -= table.getSeats();
            if(seatsLeft < 0) {
                return tablesForReservation;
            }
        }

        throw new ServiceException("could not find free tables");
    }

}
