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
//TODO: add tests
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
}
