package com.at.ac.tuwien.sepm.ss15.edulium.service.heuristics;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * a simple reservation heuristic
 */
class SimpleReservationHeuristic extends ReservationHeuristic {

    @Override
    public List<Table> getTablesForReservation(Reservation reservation, List<Table> allTables) throws ValidationException, ServiceException {
        List<Table> tables = getFreeTables(reservation, allTables);

        // check if a single table would fit for this reservation
        for(Table table : tables) {
            if (table.getSeats().equals(reservation.getQuantity())) {
                return Collections.singletonList(table);
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
