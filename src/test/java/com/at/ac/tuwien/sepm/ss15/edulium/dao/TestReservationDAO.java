package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import static org.junit.Assert.*;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Unit Test for the ReservationDAO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/Spring-DAO.xml")
@Transactional
public class TestReservationDAO {
    @Autowired
    private ReservationDAO reservationDAO;
    @Autowired
    private TableDAO tableDAO; // only to create test tables

    @Test
    public void testCreate_shouldAddValidObject() throws DAOException {
        // PREPARE
        List<Table> tables = new ArrayList<>();

        Table table1 = new Table();
        table1.setNumber(1L);
        table1.setColumn(1);
        table1.setRow(1);
        table1.setSeats(4);

        tableDAO.create(table1);
        assertEquals(1, tableDAO.find(table1).size());
        tables.add(table1);

        Table table2 = new Table();
        table2.setNumber(2L);
        table2.setColumn(2);
        table2.setRow(2);
        table2.setSeats(2);

        tableDAO.create(table2);
        assertEquals(1, tableDAO.find(table2).size());
        tables.add(table2);

        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setName("Saucy Salamander");
        reservation.setTime(new Date());
        reservation.setDuration(140);
        reservation.setQuantity(6);
        reservation.setTables(tables);

        // WHEN
        reservationDAO.create(reservation);

        // THEN
        // check if identity is set
        assertNotNull(reservation.getIdentity());

        // try to find the reservation and compare it
        Reservation matcher = new Reservation();
        matcher.setIdentity(reservation.getIdentity());

        List<Reservation> storedObjects = reservationDAO.find(matcher);
        assertEquals(1, storedObjects.size());
        assertEquals(reservation, storedObjects.get(0));
    }

    @Test(expected = DAOException.class)
    public void testCreate_addingInvalidObjecShouldFail() throws DAOException {
        // GIVEN
        Reservation reservation = new Reservation();

        // WHEN
        reservationDAO.create(reservation);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException {
        // PREPARE
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        List<Table> tablesReservation = new ArrayList<>(); // table 1
        List<Table> tablesReservationUpdated = new ArrayList<>(); // table 2, table 3

        // table 1
        Table table1 = new Table();
        table1.setNumber(1L);
        table1.setColumn(3);
        table1.setRow(3);
        table1.setSeats(4);

        tableDAO.create(table1);
        assertEquals(1, tableDAO.find(table1).size());
        tablesReservation.add(table1);

        // table 2
        Table table2 = new Table();
        table2.setNumber(2L);
        table2.setColumn(2);
        table2.setRow(2);
        table2.setSeats(6);

        tableDAO.create(table2);
        assertEquals(1, tableDAO.find(table2).size());
        tablesReservationUpdated.add(table2);

        // table 3
        Table table3 = new Table();
        table3.setNumber(3L);
        table3.setColumn(3);
        table3.setRow(3);
        table3.setSeats(6);

        tableDAO.create(table3);
        assertEquals(1, tableDAO.find(table3).size());
        tablesReservationUpdated.add(table3);

        // reservation
        Reservation reservation = new Reservation();
        reservation.setName("Hoary Hedgehog");
        reservation.setTime(calendar.getTime());
        reservation.setDuration(60);
        reservation.setQuantity(4);
        reservation.setTables(tablesReservation);

        reservationDAO.create(reservation);
        assertEquals(1, reservationDAO.find(reservation).size());

        // GIVEN
        calendar.set(Calendar.DAY_OF_MONTH, 2);

        Reservation updatedReservation = new Reservation();
        updatedReservation.setIdentity(reservation.getIdentity());
        updatedReservation.setName("Warty Warthog");
        updatedReservation.setTime(calendar.getTime());
        updatedReservation.setDuration(120);
        updatedReservation.setQuantity(10);
        updatedReservation.setTables(tablesReservationUpdated);

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
    public void testUpdate_shouldUpdateObjectThreeTimes() throws DAOException {
        // PREPARE
        List<Table> tablesReservation = new ArrayList<>(); // table 1, table 3
        List<Table> tablesReservationUpdated1 = new ArrayList<>(); // table 2
        List<Table> tablesReservationUpdated2 = new ArrayList<>(); // table 1, table 2, table 3

        // table 1
        Table table1 = new Table();
        table1.setNumber(1L);
        table1.setColumn(3);
        table1.setRow(3);
        table1.setSeats(6);

        tableDAO.create(table1);
        assertEquals(1, tableDAO.find(table1).size());
        tablesReservation.add(table1);
        tablesReservationUpdated2.add(table1);

        // table 2
        Table table2 = new Table();
        table2.setNumber(2L);
        table2.setColumn(2);
        table2.setRow(2);
        table2.setSeats(4);

        tableDAO.create(table2);
        assertEquals(1, tableDAO.find(table2).size());
        tablesReservationUpdated1.add(table2);
        tablesReservationUpdated2.add(table2);

        // table 3
        Table table3 = new Table();
        table3.setNumber(3L);
        table3.setColumn(3);
        table3.setRow(3);
        table3.setSeats(6);

        tableDAO.create(table3);
        assertEquals(1, tableDAO.find(table3).size());
        tablesReservation.add(table3);
        tablesReservationUpdated2.add(table3);

        // reservation
        Reservation reservation = new Reservation();
        reservation.setName("Oneiric Ocelot");
        reservation.setTime(new Date());
        reservation.setDuration(60);
        reservation.setQuantity(10);
        reservation.setTables(tablesReservation);

        reservationDAO.create(reservation);
        assertEquals(1, reservationDAO.find(reservation).size());

        // GIVEN
        Reservation updatedReservation1 = new Reservation();
        updatedReservation1.setIdentity(reservation.getIdentity());
        updatedReservation1.setName("Oneiric Ocelot");
        updatedReservation1.setTime(new Date());
        updatedReservation1.setDuration(60);
        updatedReservation1.setQuantity(4);
        updatedReservation1.setTables(tablesReservationUpdated1);

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
        updatedReservation2.setTime(new Date());
        updatedReservation2.setDuration(60);
        updatedReservation2.setQuantity(16);
        updatedReservation2.setTables(tablesReservationUpdated2);

        // WHEN
        reservationDAO.update(updatedReservation2);

        // THEN
        // check if the reservation has been updated
        List<Reservation> storedObjects2 = reservationDAO.find(reservation);
        assertEquals(1, storedObjects2.size());
        assertEquals(updatedReservation2, storedObjects2.get(0));
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setName("Breezy Badger");
        reservation.setTime(new Date());
        reservation.setDuration(90);
        reservation.setQuantity(8);

        // WHEN
        reservationDAO.update(reservation);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setName("Dapper Drake");
        reservation.setTime(new Date());
        reservation.setDuration(160);
        reservation.setQuantity(14);

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
        reservationDAO.update(reservation);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException {
        // PREPARE
        final int numberOfReservationsBefore = reservationDAO.getAll().size();

        List<Table> tables = new ArrayList<>();

        Table table1 = new Table();
        table1.setNumber(1L);
        table1.setColumn(1);
        table1.setRow(1);
        table1.setSeats(4);

        tableDAO.create(table1);
        assertEquals(1, tableDAO.find(table1).size());
        tables.add(table1);

        Table table2 = new Table();
        table2.setNumber(2L);
        table2.setColumn(2);
        table2.setRow(2);
        table2.setSeats(2);

        tableDAO.create(table2);
        assertEquals(1, tableDAO.find(table2).size());
        tables.add(table2);

        Reservation reservation = new Reservation();
        reservation.setName("Edgy Eft");
        reservation.setTime(new Date());
        reservation.setDuration(130);
        reservation.setQuantity(12);
        reservation.setTables(tables);

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

    @Test(expected = DAOException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setName("Feisty Fawn");
        reservation.setTime(new Date());
        reservation.setDuration(130);
        reservation.setQuantity(12);

        // WHEN
        reservationDAO.delete(reservation);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setName("Gutsy Gibbon");
        reservation.setTime(new Date());
        reservation.setDuration(170);
        reservation.setQuantity(18);

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
    public void testFind_byIdentityShouldReturnObject() throws DAOException {
        // PREPARE
        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date date1 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 3);
        Date date2 = calendar.getTime();

        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(date1);
        reservation1.setDuration(200);
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(date2);
        reservation2.setDuration(200);
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(date1);
        reservation3.setDuration(200);
        reservation3.setQuantity(30);

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());
        // GIVEN
        Reservation matcher1 = new Reservation();
        matcher1.setIdentity(reservation1.getIdentity());

        Reservation matcher2 = new Reservation();
        matcher2.setIdentity(reservation2.getIdentity());

        Reservation matcher3 = new Reservation();
        matcher3.setIdentity(reservation3.getIdentity());

        // WHEN
        List<Reservation> result1 = reservationDAO.find(matcher1); // for reservation 1
        List<Reservation> result2 = reservationDAO.find(matcher2); // for reservation 2
        List<Reservation> result3 = reservationDAO.find(matcher3); // for reservation 3

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(reservation1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(reservation2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(reservation3));
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException {
        // PREPARE
        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date date1 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 3);
        Date date2 = calendar.getTime();

        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(date1);
        reservation1.setDuration(200);
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(date2);
        reservation2.setDuration(200);
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(date1);
        reservation3.setDuration(200);
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
    public void testFind_byDateShouldReturnObjects() throws DAOException {
        // PREPARE
        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date date1 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 3);
        Date date2 = calendar.getTime();

        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(date1);
        reservation1.setDuration(200);
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(date2);
        reservation2.setDuration(200);
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(date1);
        reservation3.setDuration(200);
        reservation3.setQuantity(30);

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());

        // GIVEN
        Reservation matcher1 = new Reservation(); // for reservation 1 and reservation 3
        matcher1.setTime(date1);

        Reservation matcher2 = new Reservation(); // for reservation 2
        matcher2.setTime(date2);

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
    public void testFind_byQuantityShouldReturnObjects() throws DAOException {
        // PREPARE
        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date date1 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 3);
        Date date2 = calendar.getTime();

        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(date1);
        reservation1.setDuration(200);
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(date2);
        reservation2.setDuration(200);
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(date1);
        reservation3.setDuration(200);
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
    public void testFind_byDurationShouldReturnObjects() throws DAOException {
        // PREPARE
        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date date1 = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 3);
        Date date2 = calendar.getTime();

        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(date1);
        reservation1.setDuration(200);
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(date2);
        reservation2.setDuration(200);
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(date1);
        reservation3.setDuration(200);
        reservation3.setQuantity(30);

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());

        // GIVEN
        Reservation matcher = new Reservation(); // for reservation 1, reservation 2 and reservation 3
        matcher.setDuration(200);

        // WHEN
        List<Reservation> result = reservationDAO.find(matcher);

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(reservation1));
        assertTrue(result.contains(reservation2));
        assertTrue(result.contains(reservation3));
    }

    @Test
    public void testFind_byTablesShouldReturnObjects() throws DAOException {
        // PREPARE
        List<Table> tablesReservation1 = new ArrayList<>(); // table 1
        List<Table> tablesReservation2 = new ArrayList<>(); // table 1, table 2

        Table table1 = new Table();
        table1.setNumber(1L);
        table1.setColumn(1);
        table1.setRow(1);
        table1.setSeats(4);

        tableDAO.create(table1);
        assertEquals(1, tableDAO.find(table1).size());
        tablesReservation1.add(table1);
        tablesReservation2.add(table1);

        Table table2 = new Table();
        table2.setNumber(2L);
        table2.setColumn(2);
        table2.setRow(2);
        table2.setSeats(2);

        tableDAO.create(table2);
        assertEquals(1, tableDAO.find(table2).size());
        tablesReservation2.add(table2);

        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Hardy Heron");
        reservation1.setTime(new Date());
        reservation1.setDuration(200);
        reservation1.setQuantity(21);
        reservation1.setTables(tablesReservation1);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Hardy Heron");
        reservation2.setTime(new Date());
        reservation2.setDuration(200);
        reservation2.setQuantity(30);
        reservation2.setTables(tablesReservation2);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Jaunty Jackalope");
        reservation3.setTime(new Date());
        reservation3.setDuration(200);
        reservation3.setQuantity(30);
        reservation3.setTables(tablesReservation2);

        reservationDAO.create(reservation3);
        assertEquals(1, reservationDAO.find(reservation3).size());

        // GIVEN
        Reservation matcher1 = new Reservation(); // for reservation 1, reservation 2 and reservation 3
        matcher1.setTables(tablesReservation1);

        Reservation matcher2 = new Reservation(); // for reservation 2 and reservation 3
        matcher2.setTables(tablesReservation2);

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
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException {
        // PREPARE
        final int numberOfReservationsBefore = reservationDAO.getAll().size();

        // reservation 1
        Reservation reservation1 = new Reservation();
        reservation1.setName("Karmic Koala");
        reservation1.setTime(new Date());
        reservation1.setDuration(200);
        reservation1.setQuantity(21);

        reservationDAO.create(reservation1);
        assertEquals(1, reservationDAO.find(reservation1).size());

        // reservation 2
        Reservation reservation2 = new Reservation();
        reservation2.setName("Lucid Lynx");
        reservation2.setTime(new Date());
        reservation2.setDuration(200);
        reservation2.setQuantity(30);

        reservationDAO.create(reservation2);
        assertEquals(1, reservationDAO.find(reservation2).size());

        // reservation 3
        Reservation reservation3 = new Reservation();
        reservation3.setName("Maverick Meerkat");
        reservation3.setTime(new Date());
        reservation3.setDuration(200);
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
