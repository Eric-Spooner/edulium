package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.ReservationDAO;
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

/**
 * Unit Test for the ReservationService class
 */
public class TestReservationService extends AbstractServiceTest {
    @Autowired
    ReservationService reservationService;
    @Mock
    ReservationDAO reservationDAO;
    @Mock
    Validator<Reservation> reservationValidator;
    @Mock
    InteriorService interiorService;

    Table t1, t2, t3, t4, t5, t6;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(getTargetObject(reservationService), "reservationDAO", reservationDAO);
        ReflectionTestUtils.setField(getTargetObject(reservationService), "reservationValidator", reservationValidator);
        ReflectionTestUtils.setField(getTargetObject(reservationService), "interiorService", interiorService);
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

    private Reservation createReservation(long id, int day, int hour, int min, List<Table> tables) {
        Reservation reservation = Reservation.withIdentity(id);
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

        Mockito.when(interiorService.getAllTables()).thenReturn(Arrays.asList(t1, t2, t3, t4, t5, t6));
        Mockito.when(interiorService.getAllSections()).thenReturn(Arrays.asList(room1, room2));
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testFindTablesForReservation_shouldReturn() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1L, 1, 18, 120, Arrays.asList(t1));
        Reservation res2 = createReservation(2L, 1, 18, 120, Arrays.asList(t5));
        Reservation res = createReservation(3L, 1, 18, 150, null);
        res.setQuantity(4);

        Mockito.when(reservationDAO.findBetween(res.getTime(), res.getTime().plus(res.getDuration())))
                .thenReturn(Arrays.asList(res1, res2));

        // WHEN
        reservationService.findTablesForReservation(res);

        // THEN
        int seats = 0;
        for(Table t : res.getTables()) {
            seats += t.getSeats();
        }
        assertTrue(seats >= res.getQuantity());
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testFindTablesForReservation_shouldReturn2() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1L, 1, 18, 120, Arrays.asList(t3));
        Reservation res = createReservation(2L, 1, 17, 150, null);
        res.setQuantity(6);

        Mockito.when(reservationDAO.findBetween(res.getTime(), res.getTime().plus(res.getDuration())))
                .thenReturn(Arrays.asList(res1));
        // WHEN
        reservationService.findTablesForReservation(res);

        // THEN
        int seats = 0;
        for(Table t : res.getTables()) {
            seats += t.getSeats();
        }
        assertTrue(seats >= res.getQuantity());
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testFindTablesForReservation_shouldReturn3() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1L, 1, 18, 120, Arrays.asList(t3));
        Reservation res = createReservation(2L, 1, 19, 120, null);
        res.setQuantity(12);
        Mockito.when(reservationDAO.findBetween(res.getTime(), res.getTime().plus(res.getDuration())))
                .thenReturn(Arrays.asList(res1));

        // WHEN
        reservationService.findTablesForReservation(res);

        // THEN
        // no single table and no tables next to each other are available
        // should choose any tables which can host 12 persons
        int seats = 0;
        for(Table t : res.getTables()) {
            seats += t.getSeats();
        }
        assertTrue(seats >= res.getQuantity());
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testFindTablesForReservation_notEnoughTablesFreeShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res1 = createReservation(1L, 1, 18, 120, Arrays.asList(t3));
        Reservation res2 = createReservation(2L, 1, 17, 240, Arrays.asList(t5, t6));
        Reservation res = createReservation(3L, 1, 19, 120, null);
        res.setQuantity(12);

        Mockito.when(reservationDAO.findBetween(res.getTime(), res.getTime().plus(res.getDuration())))
                .thenReturn(Arrays.asList(res1, res2));

        // WHEN
        // tables for max 9 persons free
        reservationService.findTablesForReservation(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddReservation_addingPastReservationShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        res.setTime(LocalDateTime.of(1999, 1, 1, 18, 0));
        res.setDuration(Duration.ofMinutes(150));

        // WHEN
        reservationService.addReservation(res);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddReservation_withInvalidObjectShouldFail() throws ServiceException, ValidationException {
        // create reservation from 17:00 to 21:00
        Reservation res = new Reservation();

        Mockito.doThrow(new ValidationException("")).when(reservationValidator).validateForCreate(res);

        // WHEN
        reservationService.addReservation(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddReservation_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation res = new Reservation();
        res.setTime(LocalDateTime.of(2999, 1, 1, 18, 0));
        res.setDuration(Duration.ofMinutes(150));
        res.setQuantity(1);
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
        Reservation resWithId = Reservation.withIdentity(1L);

        Reservation res = new Reservation();
        res.setIdentity(resWithId.getIdentity());
        res.setTime(LocalDateTime.of(2999, 1, 1, 18, 0));
        res.setDuration(Duration.ofMinutes(150));

        Mockito.when(reservationDAO.find(resWithId)).thenReturn(Arrays.asList(res));

        // WHEN
        reservationService.updateReservation(res);

        // THEN
        Mockito.verify(reservationDAO).update(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateReservation_updateCurrentReservationShouldWork() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation resWithId = Reservation.withIdentity(1L);

        Reservation res = new Reservation();
        res.setIdentity(resWithId.getIdentity());
        LocalDateTime now = LocalDateTime.now();
        now.minusMinutes(10);

        res.setTime(now);
        res.setDuration(Duration.ofMinutes(120));

        Mockito.when(reservationDAO.find(resWithId)).thenReturn(Arrays.asList(res));

        // WHEN
        reservationService.updateReservation(res);

        // THEN
        Mockito.verify(reservationDAO).update(res);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateReservation_withInvalidObjectShouldFail() throws ServiceException, ValidationException {
        // PREPARE
        Reservation res = new Reservation();

        Mockito.doThrow(new ValidationException("")).when(reservationValidator).validateForUpdate(res);

        // WHEN
        reservationService.updateReservation(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateReservation_updatingPastReservationShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation resWithId = Reservation.withIdentity(1L);

        Reservation res = new Reservation();
        res.setIdentity(resWithId.getIdentity());
        res.setTime(LocalDateTime.of(1999, 1, 1, 18, 0));
        res.setDuration(Duration.ofMinutes(150));

        Mockito.when(reservationDAO.find(resWithId)).thenReturn(Arrays.asList(res));

        // WHEN
        reservationService.updateReservation(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateReservation_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation resWithId = Reservation.withIdentity(1L);

        Reservation res = new Reservation();
        res.setIdentity(resWithId.getIdentity());
        res.setTime(LocalDateTime.of(2999, 1, 1, 18, 0));
        res.setDuration(Duration.ofMinutes(150));

        Mockito.doThrow(new DAOException("")).when(reservationDAO).update(res);
        Mockito.when(reservationDAO.find(resWithId)).thenReturn(Arrays.asList(res));

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
        Reservation resWithId = Reservation.withIdentity(1L);

        Reservation res = new Reservation();
        res.setTime(LocalDateTime.of(2999, 1, 1, 18, 0));
        res.setDuration(Duration.ofMinutes(150));
        res.setIdentity(resWithId.getIdentity());

        Mockito.when(reservationDAO.find(resWithId)).thenReturn(Arrays.asList(res));

        // WHEN
        reservationService.cancelReservation(res);

        // THEN
        Mockito.verify(reservationDAO).delete(res);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testCancelReservation_cancelCurrentReservationShouldWork() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation resWithId = Reservation.withIdentity(1L);

        Reservation res = new Reservation();
        res.setIdentity(resWithId.getIdentity());
        LocalDateTime now = LocalDateTime.now();
        now.minusMinutes(10);

        res.setTime(now);
        res.setDuration(Duration.ofMinutes(120));

        Mockito.when(reservationDAO.find(resWithId)).thenReturn(Arrays.asList(res));

        // WHEN
        reservationService.cancelReservation(res);

        // THEN
        Mockito.verify(reservationDAO).delete(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testCancelReservation_cancellingPastReservationShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation resWithId = Reservation.withIdentity(1L);

        Reservation res = new Reservation();
        res.setIdentity(resWithId.getIdentity());
        res.setTime(LocalDateTime.of(1999, 1, 1, 18, 0));
        res.setDuration(Duration.ofMinutes(150));

        Mockito.when(reservationDAO.find(resWithId)).thenReturn(Arrays.asList(res));

        // WHEN
        reservationService.cancelReservation(res);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testCancelReservation_withInvalidObjectShouldFail() throws ServiceException, ValidationException {
        // create reservation from 17:00 to 21:00
        Reservation res = new Reservation();

        Mockito.doThrow(new ValidationException("")).when(reservationValidator).validateForDelete(res);

        // WHEN
        reservationService.cancelReservation(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testCancelReservation_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Reservation resWithId = Reservation.withIdentity(1L);

        Reservation res = new Reservation();
        res.setIdentity(resWithId.getIdentity());
        res.setTime(LocalDateTime.of(2999, 1, 1, 18, 0));
        res.setDuration(Duration.ofMinutes(150));

        Mockito.when(reservationDAO.find(resWithId)).thenReturn(Arrays.asList(res));
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
    public void testFindReservationIn_shouldReturnObjects() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        LocalDateTime from= LocalDateTime.of(2015, 05, 15, 15, 00);
        LocalDateTime to = from.plus(Duration.ofMinutes(60));

        Reservation res = new Reservation();
        Mockito.when(reservationDAO.findBetween(from, to)).thenReturn(Arrays.asList(res));

        // WHEN
        List<Reservation> reservations = reservationService.findReservationBetween(from, to);

        // THEN
        assertEquals(1, reservations.size());
        assertTrue(reservations.contains(res));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testFindReservationIn_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        LocalDateTime from = LocalDateTime.of(2015, 05, 15, 15, 00);
        LocalDateTime to = from.plus(Duration.ofMinutes(60));

        Reservation res = new Reservation();
        Mockito.doThrow(new DAOException("")).when(reservationDAO).findBetween(from, to);

        // WHEN
        reservationService.findReservationBetween(from, to);
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

    @Test
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
    }
}
