package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.OnetimeSale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test for the OnetimeSaleDAO
 */
public class TestOnetimeSaleDAO extends AbstractDAOTest {
    @Autowired
    private DAO<OnetimeSale> onetimeSaleDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleDAO.create(onetimeSale);

        // THEN
        // try to find the onetimesale and compare it
        List<OnetimeSale> storedObjects = onetimeSaleDAO.find(OnetimeSale.withIdentity(onetimeSale.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(onetimeSale, storedObjects.get(0));
    }


    @Test(expected = DAOException.class)
    public void testCreate_addingTwoObjectsWithSameIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleDAO.create(onetimeSale);

        // GIVEN
        OnetimeSale onetimeSale2 = new OnetimeSale();
        onetimeSale2.setIdentity(new Long(123));
        onetimeSale2.setFromTime(LocalDateTime.now());
        onetimeSale2.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleDAO.create(onetimeSale2);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleDAO.create(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        onetimeSaleDAO.create(onetimeSale);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // check if user is stored
        onetimeSaleDAO.create(onetimeSale);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale).size());

        // GIVEN
        OnetimeSale onetimeSale2 = new OnetimeSale();
        onetimeSale2.setIdentity(new Long(123));
        onetimeSale2.setFromTime(LocalDateTime.now());
        onetimeSale2.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleDAO.update(onetimeSale2);

        // THEN
        // check if the user has been updated;
        List<OnetimeSale> storedObjects = onetimeSaleDAO.find(onetimeSale2);
        assertEquals(1, storedObjects.size());
        assertEquals(onetimeSale2, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
       // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleDAO.update(onetimeSale);
    }


    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        onetimeSaleDAO.update(onetimeSale);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale sale = new OnetimeSale();

        // search for a non-existing onetimesale identity
        try {
            do {
                sale.setIdentity(new Long((int)(Math.random()*999999999)));
            } while (!onetimeSaleDAO.find(sale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing onetimesale identity");
        }

        sale.setFromTime(LocalDateTime.now());
        sale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleDAO.update(sale);
    }
}
