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
}
