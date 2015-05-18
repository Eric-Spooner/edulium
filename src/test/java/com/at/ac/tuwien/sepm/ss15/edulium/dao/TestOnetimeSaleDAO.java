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
//TODO: add tests
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
}
