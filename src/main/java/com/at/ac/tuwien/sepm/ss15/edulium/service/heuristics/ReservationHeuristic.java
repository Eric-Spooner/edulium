package com.at.ac.tuwien.sepm.ss15.edulium.service.heuristics;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import java.util.List;

/**
 * Reservation Heuristics Interface
 */
public interface ReservationHeuristic {

    List<Table> getTablesForReservation(Reservation reservation, List<Table> tables) throws ValidationException, ServiceException;

}
