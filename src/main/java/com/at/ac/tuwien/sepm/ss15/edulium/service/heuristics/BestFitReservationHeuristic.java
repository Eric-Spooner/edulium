package com.at.ac.tuwien.sepm.ss15.edulium.service.heuristics;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;

import javax.annotation.Resource;
import java.util.*;

/**
 * best fit heuristics which prefers reservations with evenly distributed
 * and less tables.
 * Selects only up to 4 tables in a reservation
 */
class BestFitReservationHeuristic implements ReservationHeuristic {
    @Resource(name = "reservationService")
    private ReservationService reservationService;

    private final int MAX_TABLES_PER_RESERVATION = 4;

    private List<Table> currentSolution;
    private double currentRating;
    private Reservation reservation;

    @Override
    public List<Table> getTablesForReservation(Reservation reservation, List<Table> t) throws ValidationException, ServiceException {
        this.reservation = reservation;
        this.currentRating = Double.MAX_VALUE;
        this.currentSolution = new ArrayList<>();

        List<Table> tables = getFreeTables(t, reservation);

        getSolution(tables);

        if(currentSolution.isEmpty()) {
            throw new ServiceException("could not find free tables");
        }

        return currentSolution;
    }

    private void getSolution(List<Table> tables)
    {
        for(int i = 1; i <= MAX_TABLES_PER_RESERVATION; i++) {
            buildSubsets(0, 0, new ArrayList<>(), tables, i);
        }
    }

    private void buildSubsets(int i, int j, List<Table> subset, List<Table> set, int subsetSize) {
        // subset complete
        if (j == subsetSize) {
            double rating = calcRating(subset);
            if(rating < currentRating) {
                currentRating = rating;
                currentSolution = subset;
            }
            return;
        }

        // create new subsets
        for (; i < set.size(); ++i) {
            List<Table> tmp = new ArrayList<>(subset);
            tmp.add(set.get(i));
            buildSubsets(i + 1, j + 1, tmp, set, subsetSize);
        }
    }

    private double calcRating(List<Table> solution) {
        int numberOfSeats = 0;
        int numberOfTables = solution.size();
        int seatsDistribution = 0;
        double rating = 0.0;

        // calc number of seats
        for (Table t : solution) {
            numberOfSeats += t.getSeats();
        }

        // not enough seats -> return worst rating
        if (numberOfSeats < reservation.getQuantity()) {
            return Double.MAX_VALUE;
        }

        // calc seats distribution
        int mean = numberOfSeats / numberOfTables;
        for (Table t : solution) {
            seatsDistribution += Math.pow(t.getSeats() - mean, 2);
        }

        // calc placement measure
        // tables which are side by side, should be preferred (lower rating)
        // calculating the distance between the tables -> O(n^n)
        // approximation: calculate the number of different rows / cols and section
        Set<Section> sections = new HashSet<>();
        Set<Integer> rows = new HashSet<>();
        Set<Integer> cols = new HashSet<>();
        for (Table t : solution) {
            sections.add(t.getSection());
            rows.add(t.getRow());
            cols.add(t.getColumn());
        }

        double placement = Math.pow(sections.size(), 2) + rows.size() + cols.size();

        rating += solution.size();                                  // number of tables
        rating += 1 / (numberOfTables) * seatsDistribution;         // seats distribution
        rating += numberOfSeats - reservation.getQuantity();        // empty seats
        rating += placement;

        return rating;
    }

    private List<Table> getFreeTables(List<Table> t, Reservation reservation) throws ServiceException, ValidationException {
        List<Reservation> reservations = reservationService.findReservationBetween(reservation.getTime(),
                reservation.getTime().plus(reservation.getDuration()));

        // create new arraylist because 't' could be immutable
        ArrayList<Table> tables = new ArrayList<>(t);

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
