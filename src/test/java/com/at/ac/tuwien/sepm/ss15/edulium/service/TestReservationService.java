package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;

/**
 * Unit Test for the ReservationService class
 */
public class TestReservationService extends AbstractServiceTest {
    @Autowired
    ReservationService reservationService;
    @Mock
    DAO<Reservation> reservationDAO;
    @Mock
    Validator<Reservation> reservationValidator;

    Table t1, t2, t3, t4, t5, t6;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(getTargetObject(reservationService), "reservationDAO", reservationDAO);
        ReflectionTestUtils.setField(getTargetObject(reservationService), "reservationValidator", reservationValidator);
        createTestData();
    }

    private Table createTable(long nr, int col, int row, int seats, Section section) {
        Table t = new Table();
        t.setNumber(nr);
        t.setColumn(col);
        t.setRow(row);
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
    private void createTestData() {
        Section room1 = new Section();
        room1.setName("room1");
        room1.setIdentity(1L);

        Section room2 = new Section();
        room2.setName("room2");
        room2.setIdentity(2L);

        // create tables
        t1 = createTable(1L, 1, 1, 4, room1);
        t2 = createTable(2L, 2, 1, 2, room1);
        t3 = createTable(3L, 1, 2, 6, room1);
        t4 = createTable(4L, 2, 2, 3, room1);
        t5 = createTable(5L, 1, 1, 4, room2);
        t6 = createTable(6L, 1, 2, 4, room2);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetFreeTables_shouldReturnTables1() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1, 18, 120, Arrays.asList(t1));
        Reservation res2 = createReservation(1, 18, 120, Arrays.asList(t5));

        Mockito.when(reservationDAO.getAll()).thenReturn(Arrays.asList(res1, res2));

        Reservation res = createReservation(1, 18, 150, null);
        List<Table> tables = reservationService.getFreeTables(res);

        assertEquals(4, tables.size());
        assertTrue(tables.contains(t2));
        assertTrue(tables.contains(t3));
        assertTrue(tables.contains(t4));
        assertTrue(tables.contains(t6));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetFreeTables_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        Mockito.doThrow(new DAOException("")).when(reservationDAO).create(res);

        // WHEN
        reservationService.getFreeTables(res);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddTaxRate_withNullObjectShouldFail() throws ServiceException, ValidationException, DAOException {
        // WHEN
        reservationService.getFreeTables(null);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddTaxRate_withoutTimeShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        res.setDuration(Duration.ofMinutes(90));

        // WHEN
        reservationService.getFreeTables(res);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddTaxRate_withoutDurationShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        res.setTime(LocalDateTime.of(2099, 1, 1, 18, 30));

        // WHEN
        reservationService.getFreeTables(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddReservation_shouldCreateReservation1() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1, 18, 120, Arrays.asList(t1));
        Reservation res2 = createReservation(1, 18, 120, Arrays.asList(t5));

        Mockito.when(reservationDAO.getAll()).thenReturn(Arrays.asList(res1, res2));

        // WHEN
        Reservation res = createReservation(1, 18, 150, null);
        res.setQuantity(4);
        reservationService.addReservation(res);

        // THEN
        // T6 is the only free table which can host exactly 4 persons
        assertEquals(1, res.getTables().size());
        assertTrue(res.getTables().contains(t6));

        // check if reservation is stored
        Mockito.verify(reservationDAO).create(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddReservation_shouldCreateReservation2() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1, 18, 120, Arrays.asList(t3));

        Mockito.when(reservationDAO.getAll()).thenReturn(Arrays.asList(res1));

        // WHEN
        Reservation res = createReservation(1, 17, 150, null);
        res.setQuantity(6);
        reservationService.addReservation(res);

        // THEN
        // no single table for 6 persons is available; should choose tables which are next to each other
        assertEquals(2, res.getTables().size());
        assertTrue(res.getTables().contains(t1));
        assertTrue(res.getTables().contains(t2));

        // check if reservation is stored
        Mockito.verify(reservationDAO).create(res);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddReservation_shouldCreateReservation3() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1, 18, 120, Arrays.asList(t3));

        Mockito.when(reservationDAO.getAll()).thenReturn(Arrays.asList(res1));

        // WHEN
        Reservation res = createReservation(1, 19, 120, null);
        res.setQuantity(12);
        reservationService.addReservation(res);

        // THEN
        // no single table and no tables next to each other are available
        // should choose any tables which can host 12 persons
        int seats = 0;
        for(Table t : res.getTables()) {
            seats += t.getSeats();
        }
        assertEquals(12, seats);

        // check if reservation is stored
        Mockito.verify(reservationDAO).create(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddReservation_withInvalidObjectShouldFail() throws ServiceException, ValidationException {
        // create reservation from 17:00 to 21:00
        Reservation res = new Reservation();

        Mockito.doThrow(new ValidationException("")).when(reservationValidator).validateForCreate(res);

        reservationService.addReservation(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddReservation_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        Mockito.doThrow(new DAOException("")).when(reservationDAO).create(res);

        // WHEN
        reservationService.addReservation(res);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testAddReservation_withoutPermissionShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();

        // WHEN
        reservationService.addReservation(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateReservation_shouldUpdate() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();

        // WHEN
        reservationService.updateReservation(res);

        // THEN
        Mockito.verify(reservationDAO).update(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateReservation_withInvalidObjectShouldFail() throws ServiceException, ValidationException {
        // create reservation from 17:00 to 21:00
        Reservation res = new Reservation();

        Mockito.doThrow(new ValidationException("")).when(reservationValidator).validateForUpdate(res);

        reservationService.updateReservation(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateReservation_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        Mockito.doThrow(new DAOException("")).when(reservationDAO).update(res);

        // WHEN
        reservationService.updateReservation(res);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testUpdateReservation_withoutPermissionShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();

        // WHEN
        reservationService.updateReservation(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testCancelReservation_shouldCancel() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();

        // WHEN
        reservationService.cancelReservation(res);

        // THEN
        Mockito.verify(reservationDAO).delete(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testCancelReservation_withInvalidObjectShouldFail() throws ServiceException, ValidationException {
        // create reservation from 17:00 to 21:00
        Reservation res = new Reservation();

        Mockito.doThrow(new ValidationException("")).when(reservationValidator).validateForDelete(res);

        reservationService.cancelReservation(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testCancelReservation_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        Mockito.doThrow(new DAOException("")).when(reservationDAO).delete(res);

        // WHEN
        reservationService.cancelReservation(res);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"COOK"})
    public void testCancelReservation_withoutPermissionShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();

        // WHEN
        reservationService.cancelReservation(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testFindReservation_shouldReturnObjects() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        Mockito.when(reservationDAO.find(res)).thenReturn(Arrays.asList(res));

        // WHEN
        List<Reservation> reservations = reservationService.findReservation(res);

        // THEN
        assertEquals(1, reservations.size());
        assertTrue(reservations.contains(res));

    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testFindReservation_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        Mockito.doThrow(new DAOException("")).when(reservationDAO).find(res);

        // WHEN
        reservationService.findReservation(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetAllReservations_shouldReturnObjects() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        Reservation reservation3 = new Reservation();
        Mockito.when(reservationDAO.getAll()).thenReturn(Arrays.asList(reservation1, reservation2, reservation3));

        // WHEN
        List<Reservation> reservations = reservationService.getAllReservations();

        // THEN
        assertEquals(3, reservations.size());
        assertTrue(reservations.contains(reservation1));
        assertTrue(reservations.contains(reservation2));
        assertTrue(reservations.contains(reservation3));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetAllReservation_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Mockito.doThrow(new DAOException("")).when(reservationDAO).getAll();

        // WHEN
        reservationService.getAllReservations();
    }

    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetReservationHistory_shouldReturn() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        History<Reservation> history = new History<>();
        history.setData(res);

        Mockito.when(reservationDAO.getHistory(res)).thenReturn(Arrays.asList(history));

        // WHEN
        List<History<Reservation>> changes = reservationService.getReservationHistory(res);

        // THEN
        assertEquals(1, changes.size());
        assertTrue(changes.contains(history));
        Mockito.verify(reservationDAO).getHistory(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetReservationHistory_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        Mockito.doThrow(new DAOException("")).when(reservationDAO).getHistory(res);

        // WHEN
        reservationService.getReservationHistory(res);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetReservationHistory_onValidationExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        Mockito.doThrow(new ValidationException("")).when(reservationValidator).validateIdentity(res);

        // WHEN
        reservationService.getReservationHistory(res);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetReservationHistory_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Reservation res = new Reservation();

        // WHEN
        reservationService.getReservationHistory(res);

        // THEN
        Mockito.verify(reservationDAO, never()).getHistory(res);
    }

}
