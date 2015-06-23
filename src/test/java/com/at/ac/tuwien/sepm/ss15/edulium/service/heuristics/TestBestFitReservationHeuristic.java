package com.at.ac.tuwien.sepm.ss15.edulium.service.heuristics;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.ReservationDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.AbstractServiceTest;
import com.at.ac.tuwien.sepm.ss15.edulium.service.InteriorService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ReservationService;
import com.at.ac.tuwien.sepm.ss15.edulium.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by phili on 6/15/15.
 */
public class TestBestFitReservationHeuristic extends AbstractServiceTest {
    ReservationHeuristic reservationHeuristic = new BestFitReservationHeuristic();

    @Mock
    ReservationService reservationService;
    @Mock
    InteriorService interiorService;

    Section room1, room2;
    Table t1, t2, t3, t4, t5, t6;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(getTargetObject(reservationHeuristic), "reservationService", reservationService);
        createTestData();
    }

    private Table createTable(long nr, int col, int row, int seats, Section section) {
        Table t = new Table();
        t.setNumber(nr);
        t.setColumn(col);
        t.setRow(row);
        t.setSeats(seats);
        t.setSection(section);
        return t;
    }

    private Reservation createReservation(int day, int hour, int min, List<Table> tables) {
        Reservation reservation = new Reservation();
        reservation.setDuration(Duration.ofMinutes(min));
        reservation.setTime(LocalDateTime.of(2099, 1, day, hour, 0));
        reservation.setTables(tables);
        return reservation;
    }


    /* table-layout:
          room1:   room2:
        | T1  T2 |   T5   |
        | T3  T4 |   T6   |
     */
    private void createTestData() throws ServiceException {
        room1 = new Section();
        room1.setName("room1");
        room1.setIdentity(1L);

        room2 = new Section();
        room2.setName("room2");
        room2.setIdentity(2L);

        // create tables
        t1 = createTable(1L, 1, 1, 4, room1);
        t2 = createTable(2L, 2, 1, 2, room1);
        t3 = createTable(3L, 1, 2, 6, room1);
        t4 = createTable(4L, 2, 2, 3, room1);
        t5 = createTable(5L, 1, 1, 4, room2);
        t6 = createTable(6L, 1, 2, 4, room2);

        Mockito.when(interiorService.getAllTables()).thenReturn(Arrays.asList(t1, t2, t3, t4, t5, t6));
        Mockito.when(interiorService.getAllSections()).thenReturn(Arrays.asList(room1, room2));
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void test_OneTableFitsRequest_shouldReturnOneTable() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1, 18, 120, Arrays.asList(t1));
        Reservation res2 = createReservation(1, 18, 120, Arrays.asList(t5));
        Reservation res = createReservation(1, 18, 150, null);
        res.setQuantity(4);

        Mockito.when(reservationService.findReservationBetween(res.getTime(), res.getTime().plus(res.getDuration())))
                .thenReturn(Arrays.asList(res1, res2));

        // WHEN
        List<Table> tables = reservationHeuristic.getTablesForReservation(res, interiorService.getAllTables());

        // THEN
        assertEquals(1, tables.size());
        // t6 can fit 4 persons
        assertEquals(t6, tables.get(0));
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void test_NoTableFitsRequest_shouldReturnTwoTablesNextToEachOther() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1, 18, 120, Arrays.asList(t3));
        Reservation res = createReservation(1, 17, 150, null);
        res.setQuantity(6);

        Mockito.when(reservationService.findReservationBetween(res.getTime(), res.getTime().plus(res.getDuration())))
                .thenReturn(Arrays.asList(res1));

        // WHEN
        List<Table> tables = reservationHeuristic.getTablesForReservation(res, interiorService.getAllTables());

        // THEN
        assertEquals(2, tables.size());

        // should contain t1 and t2 because they are next to each other and their seats sum up to 6
        assertTrue(tables.contains(t1));
        assertTrue(tables.contains(t2));
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void test_NoTablesNextToEachOtherFitRequest_shouldReturnThreeTables() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1, 18, 120, Arrays.asList(t3));
        Reservation res = createReservation(1, 19, 120, null);
        res.setQuantity(12);
        Mockito.when(reservationService.findReservationBetween(res.getTime(), res.getTime().plus(res.getDuration())))
                .thenReturn(Arrays.asList(res1));

        // WHEN
        List<Table> tables = reservationHeuristic.getTablesForReservation(res, interiorService.getAllTables());

        // THEN
        // no single table and no tables next to each other are available
        assertEquals(3, tables.size());

        // evenly distributed choice with the least number of tables
        assertTrue(tables.contains(t1));
        assertTrue(tables.contains(t5));
        assertTrue(tables.contains(t6));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void test_notEnoughTablesFreeShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1, 18, 120, Arrays.asList(t3));
        Reservation res2 = createReservation(1, 17, 240, Arrays.asList(t5, t6));
        Reservation res = createReservation(1, 19, 120, null);
        res.setQuantity(12);

        Mockito.when(reservationService.findReservationBetween(res.getTime(), res.getTime().plus(res.getDuration())))
                .thenReturn(Arrays.asList(res1, res2));

        // WHEN
        // tables for max 9 persons free
        reservationHeuristic.getTablesForReservation(res, interiorService.getAllTables());
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void test_largeNumberOfTables_shouldReturnTablesNextToEachOther() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        List<Table> tables = new ArrayList<>();
        for(int i=0; i < 100; i++) {
            tables.add(createTable(i, i%10, i/10, 4, room1));
        }
        for(int i=0; i < 50; i++) {
            tables.add(createTable(100 + i, i%5, i/10, 4, room2));
        }

        Reservation res = createReservation(1, 19, 120, null);
        res.setQuantity(8);

        // WHEN
        tables = reservationHeuristic.getTablesForReservation(res, tables);

        // THEN
        assertEquals(2, tables.size());

        double xDiff = tables.get(0).getColumn() - tables.get(1).getColumn();
        double yDiff = tables.get(0).getRow() - tables.get(1).getRow();

        assertEquals(tables.get(0).getSection(), tables.get(1).getSection());
        assertTrue(Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2)) < Math.sqrt(2));
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void test_largeNumberOfTables_shouldReturnTwoTables() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        List<Table> tables = new ArrayList<>();
        for(int i=0; i < 100; i++) {
            tables.add(createTable(i, i%10, i/10, 4, room1));
        }
        for(int i=0; i < 50; i++) {
            tables.add(createTable(100 + i, i%5, i/10, 4, room2));
        }

        tables.get(10).setSeats(3);
        tables.get(11).setSeats(3);

        Reservation res = createReservation(1, 19, 120, null);
        res.setQuantity(6);

        // WHEN
        List<Table> t = reservationHeuristic.getTablesForReservation(res, tables);

        assertEquals(2, t.size());

        // only two tables which fit the request
        assertTrue(t.contains(tables.get(10)));
        assertTrue(t.contains(tables.get(11)));
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void test_NoSetOfTableFits_shouldReturnTwoTables() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        List<Table> tables = new ArrayList<>();
        for(int i=0; i < 100; i++) {
            tables.add(createTable(i, i%10, i/10, 4, room1));
        }
        for(int i=0; i < 50; i++) {
            tables.add(createTable(100 + i, i%5, i/10, 4, room2));
        }

        tables.get(10).setSeats(3);
        tables.get(11).setSeats(3);

        Reservation res = createReservation(1, 19, 120, null);
        res.setQuantity(5);

        // WHEN
        List<Table> t = reservationHeuristic.getTablesForReservation(res, tables);

        assertEquals(2, t.size());

        // best solution with only 1 free seat
        assertTrue(t.contains(tables.get(10)));
        assertTrue(t.contains(tables.get(11)));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void test_RequestWithMoreThanTheLimitOfFiveTables_ShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        List<Table> tables = new ArrayList<>();
        for(int i=0; i < 100; i++) {
            tables.add(createTable(i, i%10, i/10, 4, room1));
        }
        for(int i=0; i < 50; i++) {
            tables.add(createTable(100 + i, i%5, i/10, 4, room2));
        }

        tables.get(10).setSeats(3);
        tables.get(11).setSeats(3);

        Reservation res = createReservation(1, 19, 120, null);
        res.setQuantity(26);

        // WHEN
        reservationHeuristic.getTablesForReservation(res, tables);
    }
}
