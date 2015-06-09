package com.at.ac.tuwien.sepm.ss15.edulium.service.heuristics;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

/**
 * a simple reservation heuristic
 */
public class SimpleReservationHeuristic implements ReservationHeuristic {
    @Autowired
    private ReservationService reservationService;

    @Override
    public List<Table> getTablesForReservation(Reservation reservation, List<Table> tables) throws ValidationException, ServiceException {
        return null;
    }

}
