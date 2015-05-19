package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.IntermittentSale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test for the IntermittentSaleDAO
 */
public class TestIntermittentSaleDAO extends AbstractDAOTest {
    @Autowired
    private DAO<IntermittentSale> intermittentSaleDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setDuration(120);
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(true);
        intermittentSale.setSaturday(true);
        intermittentSale.setSunday(true);

        // WHEN
        intermittentSaleDAO.create(intermittentSale);

        // THEN
        // try to find the intermittentsale and compare it
        List<IntermittentSale> storedObjects = intermittentSaleDAO.find(IntermittentSale.withIdentity(intermittentSale.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(intermittentSale, storedObjects.get(0));
    }


    @Test(expected = DAOException.class)
    public void testCreate_addingTwoObjectsWithSameIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setDuration(120);
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(true);
        intermittentSale.setSaturday(true);
        intermittentSale.setSunday(true);

        // WHEN
        intermittentSaleDAO.create(intermittentSale);

        // GIVEN
        IntermittentSale intermittentSale2 = new IntermittentSale();
        intermittentSale2.setIdentity(new Long(123));
        intermittentSale2.setFromDayTime(LocalDateTime.now());
        intermittentSale2.setDuration(60);
        intermittentSale2.setMonday(true);
        intermittentSale2.setTuesday(false);
        intermittentSale2.setWednesday(false);
        intermittentSale2.setThursday(true);
        intermittentSale2.setFriday(true);
        intermittentSale2.setSaturday(true);
        intermittentSale2.setSunday(true);

        // WHEN
        intermittentSaleDAO.create(intermittentSale2);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setDuration(120);
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(true);
        intermittentSale.setSaturday(true);
        intermittentSale.setSunday(true);

        // WHEN
        intermittentSaleDAO.create(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        intermittentSaleDAO.create(intermittentSale);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setDuration(120);
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(true);
        intermittentSale.setSaturday(true);
        intermittentSale.setSunday(true);

        // check if user is stored
        intermittentSaleDAO.create(intermittentSale);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale).size());

        // GIVEN
        IntermittentSale intermittentSale2 = new IntermittentSale();
        intermittentSale2.setIdentity(new Long(123));
        intermittentSale2.setFromDayTime(LocalDateTime.now());
        intermittentSale2.setDuration(180);
        intermittentSale2.setMonday(false);
        intermittentSale2.setTuesday(true);
        intermittentSale2.setWednesday(false);
        intermittentSale2.setThursday(true);
        intermittentSale2.setFriday(true);
        intermittentSale2.setSaturday(true);
        intermittentSale2.setSunday(true);

        // WHEN
        intermittentSaleDAO.update(intermittentSale2);

        // THEN
        // check if the user has been updated;
        List<IntermittentSale> storedObjects = intermittentSaleDAO.find(intermittentSale2);
        assertEquals(1, storedObjects.size());
        assertEquals(intermittentSale, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setDuration(120);
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(true);
        intermittentSale.setSaturday(true);
        intermittentSale.setSunday(true);

        // WHEN
        intermittentSaleDAO.update(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        intermittentSaleDAO.update(intermittentSale);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();

        // search for a non-existing intermittent sale identity
        try {
            do {
                intermittentSale.setIdentity(new Long((int)(Math.random()*999999999)));
            } while (!intermittentSaleDAO.find(intermittentSale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing intermittent sale identity");
        }

        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setDuration(90);
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(false);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(true);
        intermittentSale.setSaturday(true);
        intermittentSale.setSunday(true);

        // WHEN
        intermittentSaleDAO.update(intermittentSale);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // PREPARE
        final int numberBefore = intermittentSaleDAO.getAll().size();
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setDuration(120);
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(true);
        intermittentSale.setSaturday(true);
        intermittentSale.setSunday(true);

        // check if user is stored
        intermittentSaleDAO.create(intermittentSale);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale).size());

        // GIVEN
        IntermittentSale intermittentSale2 = new IntermittentSale();
        intermittentSale2.setIdentity(intermittentSale.getIdentity());

        // WHEN
        intermittentSaleDAO.delete(intermittentSale2);

        // THEN
        // check if the user has been updated;
        List<IntermittentSale> storedObjects = intermittentSaleDAO.find(intermittentSale2);
        assertEquals(numberBefore, storedObjects.size());
        assertEquals(0, intermittentSaleDAO.find(intermittentSale).size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        intermittentSaleDAO.delete(intermittentSale);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();

        // search for a non-existing intermittent sale identity
        try {
            do {
                intermittentSale.setIdentity(new Long((int)(Math.random()*999999999)));
            } while (!intermittentSaleDAO.find(intermittentSale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing intermittent sale identity");
        }

        // WHEN
        intermittentSaleDAO.delete(intermittentSale);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = new IntermittentSale();
        intermittentSale1.setIdentity(new Long(123));
        intermittentSale1.setFromDayTime(LocalDateTime.now());
        intermittentSale1.setDuration(120);
        intermittentSale1.setMonday(true);
        intermittentSale1.setTuesday(true);
        intermittentSale1.setWednesday(true);
        intermittentSale1.setThursday(true);
        intermittentSale1.setFriday(true);
        intermittentSale1.setSaturday(true);
        intermittentSale1.setSunday(true);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = new IntermittentSale();
        intermittentSale2.setIdentity(new Long(124));
        intermittentSale2.setFromDayTime(LocalDateTime.now());
        intermittentSale2.setDuration(120);
        intermittentSale2.setMonday(true);
        intermittentSale2.setTuesday(true);
        intermittentSale2.setWednesday(true);
        intermittentSale2.setThursday(true);
        intermittentSale2.setFriday(true);
        intermittentSale2.setSaturday(true);
        intermittentSale2.setSunday(true);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = new IntermittentSale();
        intermittentSale3.setIdentity(new Long(125));
        intermittentSale3.setFromDayTime(LocalDateTime.now());
        intermittentSale3.setDuration(120);
        intermittentSale3.setMonday(true);
        intermittentSale3.setTuesday(true);
        intermittentSale3.setWednesday(true);
        intermittentSale3.setThursday(true);
        intermittentSale3.setFriday(true);
        intermittentSale3.setSaturday(true);
        intermittentSale3.setSunday(true);

        intermittentSaleDAO.create(intermittentSale3);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale3).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(intermittentSale1.getIdentity()); // for sale 1
        IntermittentSale matcher2 = IntermittentSale.withIdentity(intermittentSale2.getIdentity()); // for sale 2
        IntermittentSale matcher3 = IntermittentSale.withIdentity(intermittentSale3.getIdentity()); // for sale 3

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);
        List<IntermittentSale> result3 = intermittentSaleDAO.find(matcher3);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(intermittentSale3));
    }

    @Test
    public void testFind_byFromDayTimeShouldReturnObjects() throws DAOException, ValidationException {
        //TODO: complete
    }

    @Test
    public void testFind_byDurationShouldReturnObjects() throws DAOException, ValidationException {
        //TODO: complete
    }

    @Test
    public void testFind_byMondayShouldReturnObjects() throws DAOException, ValidationException {
        //TODO: complete
        //TODO: add tests for other days
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        //find a non existent id
        //TODO: complete
    }

    @Test
    public void testFind_nullObjectShouldReturnEmptyList() throws DAOException {
        //TODO: complete
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        //TODO: complete
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        intermittentSaleDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();

        // WHEN
        intermittentSaleDAO.getHistory(intermittentSale);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();

        // search for a non-existing intermittent sale identity
        try {
            do {
                intermittentSale.setIdentity(new Long((int)(Math.random()*999999999)));
            } while (!intermittentSaleDAO.find(intermittentSale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing intermittent sale identity");
        }

        assertTrue(intermittentSaleDAO.getHistory(intermittentSale).isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        //TODO: complete
    }
}
