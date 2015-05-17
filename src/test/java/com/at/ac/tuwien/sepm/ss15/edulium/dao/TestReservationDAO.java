package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import static org.junit.Assert.*;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Unit Test for the ReservationDAO
 */
public class TestReservationDAO extends AbstractDAOTest {
    @Autowired
    private DAO<Reservation> reservationDAO;
    @Autowired
    private DAO<Table> tableDAO; // only to create test tables
    @Autowired
    private DAO<Section> sectionDAO; // only to create test sections

    private Table table1;
    private Table table2;
    private Table table3;

    @Before
    public void before() throws ValidationException, DAOException {
        Section section = new Section();
        section.setIdentity(1L);
        section.setName("Section");

        sectionDAO.create(section);
        assertEquals(1, sectionDAO.find(section).size());

        table1 = new Table();
        table1.setSection(section);
        table1.setNumber(1L);
        table1.setColumn(1);
        table1.setRow(1);
        table1.setSeats(4);

        tableDAO.create(table1);
        assertEquals(1, tableDAO.find(table1).size());

        table2 = new Table();
        table2.setSection(section);
        table2.setNumber(2L);
        table2.setColumn(2);
        table2.setRow(2);
        table2.setSeats(2);

        tableDAO.create(table2);
        assertEquals(1, tableDAO.find(table2).size());

        table3 = new Table();
        table3.setNumber(3L);
        table3.setColumn(3);
        table3.setRow(3);
        table3.setSeats(6);

        tableDAO.create(table3);
        assertEquals(1, tableDAO.find(table3).size());
    }

    @Test
    public void testCreate_shouldAddValidObject() throws DAOException, ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setName("Saucy Salamander");
        reservation.setTime(LocalDateTime.now());
        reservation.setDuration(Duration.ofMinutes(140));
        reservation.setQuantity(6);
        reservation.setTables(Arrays.asList(table1, table2));

        // WHEN
        reservationDAO.create(reservation);

        // THEN
        // check if identity is set
        assertNotNull(reservation.getIdentity());

        // try to find the reservation and compare it
        List<Reservation> storedObjects = reservationDAO.find(Reservation.withIdentity(reservation.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(reservation, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingInvalidObjecShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();

        // WHEN
        reservationDAO.create(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Reservation reservation = null;

        // WHEN
        reservationDAO.create(reservation);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // PREPARE
        Reservation reservation = new Reservation();
        reservation.setName("Hoary Hedgehog");
        reservation.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation.setDuration(Duration.ofMinutes(60));
        reservation.setQuantity(4);
        reservation.setTables(Arrays.asList(table1));

        reservationDAO.create(reservation);
        assertEquals(1, reservationDAO.find(reservation).size());

        // GIVEN
        Reservation updatedReservation = new Reservation();
        updatedReservation.setIdentity(reservation.getIdentity());
        updatedReservation.setName("Warty Warthog");
        updatedReservation.setTime(LocalDateTime.of(2016, 06, 17, 12, 00));
        updatedReservation.setDuration(Duration.ofMinutes(120));
        updatedReservation.setQuantity(10);
        updatedReservation.setTables(Arrays.asList(table2, table3));

        // WHEN
        reservationDAO.update(updatedReservation);

        // THEN
        // check if the reservation has been updated
        List<Reservation> storedObjects = reservationDAO.find(reservation);
        assertEquals(1, storedObjects.size());
        assertEquals(updatedReservation, storedObjects.get(0));
    }

    /**
     * 1. Create a reservation with quantity 10 - add tables 1 (6 seats) and 3 (6 seats)
     * 2. Set the quantity from 10 to 4 - remove tables 1 and 3 - add table 2 (4 seats)
     * 3. Set the quantity from 4 to 16 - re-add tables 1 (6 seats) and 3 (6 seats)
     */
    @Test
    public void testUpdate_shouldUpdateObjectTwoTimes() throws DAOException, ValidationException {
        // PREPARE
        Reservation reservation = new Reservation();
        reservation.setName("Oneiric Ocelot");
        reservation.setTime(LocalDateTime.now());
        reservation.setDuration(Duration.ofMinutes(60));
        reservation.setQuantity(10);
        reservation.setTables(Arrays.asList(table1, table3));

        reservationDAO.create(reservation);
        assertEquals(1, reservationDAO.find(reservation).size());

        // GIVEN
        Reservation updatedReservation1 = new Reservation();
        updatedReservation1.setIdentity(reservation.getIdentity());
        updatedReservation1.setName("Oneiric Ocelot");
        updatedReservation1.setTime(LocalDateTime.now());
        updatedReservation1.setDuration(Duration.ofMinutes(60));
        updatedReservation1.setQuantity(4);
        updatedReservation1.setTables(Arrays.asList(table2));

        // WHEN
        reservationDAO.update(updatedReservation1);

        // THEN
        // check if the reservation has been updated
        List<Reservation> storedObjects1 = reservationDAO.find(reservation);
        assertEquals(1, storedObjects1.size());
        assertEquals(updatedReservation1, storedObjects1.get(0));

        // GIVEN
        Reservation updatedReservation2 = new Reservation();
        updatedReservation2.setIdentity(reservation.getIdentity());
        updatedReservation2.setName("Oneiric Ocelot");
        updatedReservation2.setTime(LocalDateTime.now());
        updatedReservation2.setDuration(Duration.ofMinutes(60));
        updatedReservation2.setQuantity(16);
        updatedReservation2.setTables(Arrays.asList(table1, table2, table3));

        // WHEN
        reservationDAO.update(updatedReservation2);

        // THEN
        // check if the reservation has been updated
        List<Reservation> storedObjects2 = reservationDAO.find(reservation);
        assertEquals(1, storedObjects2.size());
        assertEquals(updatedReservation2, storedObjects2.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setName("Breezy Badger");
        reservation.setTime(LocalDateTime.now());
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setQuantity(8);

        // WHEN
        reservationDAO.update(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Reservation reservation = null;

        // WHEN
        reservationDAO.update(reservation);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();

        // search for a non-existing reservation identity
        try {
            reservation.setIdentity(0L);
            while (!reservationDAO.find(reservation).isEmpty()) {
                reservation.setIdentity(reservation.getIdentity() + 1L);
            }
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing reservation identity");
        }

        reservation.setName("Dapper Drake");
        reservation.setTime(LocalDateTime.now());
        reservation.setDuration(Duration.ofMinutes(160));
        reservation.setQuantity(14);

        // WHEN
        reservationDAO.update(reservation);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // PREPARE
        final int numberOfReservationsBefore = reservationDAO.getAll().size();

        Reservation reservation = new Reservation();
        reservation.setName("Edgy Eft");
        reservation.setTime(LocalDateTime.now());
        reservation.setDuration(Duration.ofMinutes(130));
        reservation.setQuantity(12);
        reservation.setTables(Arrays.asList(table1, table2));

        reservationDAO.create(reservation);

        // check if reservation is stored
        assertEquals(1, reservationDAO.find(reservation).size());
        assertEquals(numberOfReservationsBefore + 1, reservationDAO.getAll().size());

        // GIVEN
        Reservation reservationForDelete = new Reservation();
        reservationForDelete.setIdentity(reservation.getIdentity());

        // WHEN
        reservationDAO.delete(reservationForDelete);

        // THEN
        assertEquals(0, reservationDAO.find(reservation).size());
        assertEquals(numberOfReservationsBefore, reservationDAO.getAll().size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();

        // WHEN
        reservationDAO.delete(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Reservation reservation = null;

        // WHEN
        reservationDAO.delete(reservation);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();

        // search for a non-existing reservation identity
        try {
            reservation.setIdentity(0L);
            while (!reservationDAO.find(reservation).isEmpty()) {
                reservation.setIdentity(reservation.getIdentity() + 1L);
            }
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing reservation identity");
        }

        // WHEN
        reservationDAO.delete(reservation);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // PREPARE
        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation1.setDuration(Duration.ofMinutes(200));
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(LocalDateTime.of(2015, 05, 18, 17, 30));
        reservation2.setDuration(Duration.ofMinutes(200));
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation3.setDuration(Duration.ofMinutes(200));
        reservation3.setQuantity(30);

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());

        // GIVEN
        Reservation matcher1 = Reservation.withIdentity(reservation1.getIdentity()); // for reservation 1
        Reservation matcher2 = Reservation.withIdentity(reservation2.getIdentity()); // for reservation 2
        Reservation matcher3 = Reservation.withIdentity(reservation3.getIdentity()); // for reservation 3

        // WHEN
        List<Reservation> result1 = reservationDAO.find(matcher1);
        List<Reservation> result2 = reservationDAO.find(matcher2);
        List<Reservation> result3 = reservationDAO.find(matcher3);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(reservation1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(reservation2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(reservation3));
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation1.setDuration(Duration.ofMinutes(200));
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(LocalDateTime.of(2015, 05, 18, 17, 30));
        reservation2.setDuration(Duration.ofMinutes(200));
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation3.setDuration(Duration.ofMinutes(200));
        reservation3.setQuantity(30);

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());

        // GIVEN
        Reservation matcher1 = new Reservation(); // for reservation 1 and reservation 2
        matcher1.setName("Hardy Heron");

        Reservation matcher2 = new Reservation(); // for reservation 3
        matcher2.setName("Jaunty Jackalope");

        // WHEN
        List<Reservation> result1 = reservationDAO.find(matcher1);
        List<Reservation> result2 = reservationDAO.find(matcher2);

        // THEN
        assertEquals(2, result1.size());
        assertTrue(result1.contains(reservation1));
        assertTrue(result1.contains(reservation2));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(reservation3));
    }

    @Test
    public void testFind_byDateShouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation1.setDuration(Duration.ofMinutes(200));
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(LocalDateTime.of(2015, 05, 18, 17, 30));
        reservation2.setDuration(Duration.ofMinutes(200));
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation3.setDuration(Duration.ofMinutes(200));
        reservation3.setQuantity(30);

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());

        // GIVEN
        Reservation matcher1 = new Reservation(); // for reservation 1 and reservation 3
        matcher1.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));

        Reservation matcher2 = new Reservation(); // for reservation 2
        matcher2.setTime(LocalDateTime.of(2015, 05, 18, 17, 30));

        // WHEN
        List<Reservation> result1 = reservationDAO.find(matcher1);
        List<Reservation> result2 = reservationDAO.find(matcher2);

        // THEN
        assertEquals(2, result1.size());
        assertTrue(result1.contains(reservation1));
        assertTrue(result1.contains(reservation3));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(reservation2));
    }

    @Test
    public void testFind_byQuantityShouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation1.setDuration(Duration.ofMinutes(200));
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(LocalDateTime.of(2015, 05, 18, 17, 30));
        reservation2.setDuration(Duration.ofMinutes(200));
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation3.setDuration(Duration.ofMinutes(200));
        reservation3.setQuantity(30);

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());

        // GIVEN
        Reservation matcher1 = new Reservation(); // for reservation 1
        matcher1.setQuantity(21);

        Reservation matcher2 = new Reservation(); // for reservation 2 and reservation 3
        matcher2.setQuantity(30);

        // WHEN
        List<Reservation> result1 = reservationDAO.find(matcher1);
        List<Reservation> result2 = reservationDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(reservation1));

        assertEquals(2, result2.size());
        assertTrue(result2.contains(reservation2));
        assertTrue(result2.contains(reservation3));
    }

    @Test
    public void testFind_byDurationShouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation1.setDuration(Duration.ofMinutes(200));
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(LocalDateTime.of(2015, 05, 18, 17, 30));
        reservation2.setDuration(Duration.ofMinutes(200));
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(LocalDateTime.of(2015, 05, 15, 18, 30));
        reservation3.setDuration(Duration.ofMinutes(200));
        reservation3.setQuantity(30);

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());

        // GIVEN
        Reservation matcher = new Reservation(); // for reservation 1, reservation 2 and reservation 3
        matcher.setDuration(Duration.ofMinutes(200));

        // WHEN
        List<Reservation> result = reservationDAO.find(matcher);

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(reservation1));
        assertTrue(result.contains(reservation2));
        assertTrue(result.contains(reservation3));
    }

    @Test
    public void testFind_byTablesShouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(LocalDateTime.now());
        reservation1.setDuration(Duration.ofMinutes(200));
        reservation1.setQuantity(21);
        reservation1.setTables(Arrays.asList(table1));

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(LocalDateTime.now());
        reservation2.setDuration(Duration.ofMinutes(200));
        reservation2.setQuantity(30);
        reservation2.setTables(Arrays.asList(table1, table2));

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(LocalDateTime.now());
        reservation3.setDuration(Duration.ofMinutes(200));
        reservation3.setQuantity(30);
        reservation3.setTables(Arrays.asList(table1, table2));

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());

        // GIVEN
        Reservation matcher1 = new Reservation(); // for reservation 1, reservation 2 and reservation 3
        matcher1.setTables(Arrays.asList(table1));

        Reservation matcher2 = new Reservation(); // for reservation 2 and reservation 3
        matcher2.setTables(Arrays.asList(table1, table2));

        // WHEN
        List<Reservation> result1 = reservationDAO.find(matcher1);
        List<Reservation> result2 = reservationDAO.find(matcher2);

        // THEN
        assertEquals(3, result1.size());
        assertTrue(result1.contains(reservation1));
        assertTrue(result1.contains(reservation2));
        assertTrue(result1.contains(reservation3));

        assertEquals(2, result2.size());
        assertTrue(result2.contains(reservation2));
        assertTrue(result2.contains(reservation3));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        Reservation matcher = new Reservation();

        // search for a non-existing reservation identity
        try {
            matcher.setIdentity(0L);
            while (!reservationDAO.find(matcher).isEmpty()) {
                matcher.setIdentity(matcher.getIdentity() + 1L);
            }
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing reservation identity");
        }

        // WHEN
        List<Reservation> result = reservationDAO.find(matcher);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        final int numberOfReservationsBefore = reservationDAO.getAll().size();

        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Karmic Koala");
        reservation1.setTime(LocalDateTime.now());
        reservation1.setDuration(Duration.ofMinutes(200));
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Lucid Lynx");
        reservation2.setTime(LocalDateTime.now());
        reservation2.setDuration(Duration.ofMinutes(200));
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Maverick Meerkat");
        reservation3.setTime(LocalDateTime.now());
        reservation3.setDuration(Duration.ofMinutes(200));
        reservation3.setQuantity(30);

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());

        // WHEN
        List<Reservation> result = reservationDAO.getAll();

        // THEN
        assertEquals(numberOfReservationsBefore + 3, result.size());
        assertTrue(result.contains(reservation1));
        assertTrue(result.contains(reservation2));
        assertTrue(result.contains(reservation3));
    }
}
