package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test for the IntermittentSaleDAO
 */
public class TestIntermittentSaleDAO extends AbstractDAOTest {
    @Autowired
    private DAO<IntermittentSale> intermittentSaleDAO;
    @Autowired
    private DAO<MenuCategory> menuCategoryDAO;
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private DAO<TaxRate> taxRateDAO;

    private MenuEntry createMenuEntry(Long identity, String name, String description, String category, double price, double tax, boolean available)
            throws ValidationException, DAOException {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName(category);
        menuCategoryDAO.create(menuCategory);

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(tax));
        taxRateDAO.create(taxRate);

        MenuEntry entry = new MenuEntry();
        entry.setIdentity(identity);
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        entry.setName(name);
        entry.setDescription(description);
        entry.setPrice(BigDecimal.valueOf(price));
        entry.setAvailable(available);
        menuEntryDAO.create(entry);

        return entry;
    }

    private Hashtable<MenuEntry, BigDecimal> createRandomEntries() throws ValidationException, DAOException {
        MenuEntry entry = createMenuEntry(new Long((int)(Math.random()*999999999)),"entry", "desc", "cat", 20, 0.2, true);
        BigDecimal bigDecimal = new BigDecimal(10);
        Hashtable<MenuEntry, BigDecimal> hashtable = new Hashtable<>();
        hashtable.put(entry, bigDecimal);
        return hashtable;
    }

    private IntermittentSale createIntermittentSale(Long identity, String name, Hashtable<MenuEntry, BigDecimal> entries, Integer duration, Boolean enabled, LocalDateTime fromDayTime, Boolean monday, Boolean tuesday, Boolean wednesday, Boolean thursday, Boolean friday, Boolean saturday, Boolean sunday) {
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(identity);
        intermittentSale.setName(name);
        intermittentSale.setEntries(entries);
        intermittentSale.setDuration(duration);
        intermittentSale.setEnabled(enabled);
        intermittentSale.setFromDayTime(fromDayTime);
        intermittentSale.setMonday(monday);
        intermittentSale.setTuesday(tuesday);
        intermittentSale.setWednesday(wednesday);
        intermittentSale.setThursday(thursday);
        intermittentSale.setFriday(friday);
        intermittentSale.setSaturday(saturday);
        intermittentSale.setSunday(sunday);
        return intermittentSale;
    }

    private IntermittentSale createIntermittentSale(Long identity) throws ValidationException, DAOException {
        return createIntermittentSale(identity, "Sale", createRandomEntries(), 60, true, LocalDateTime.now(), new Boolean(true), new Boolean(true), new Boolean(false), new Boolean(false), new Boolean(true), new Boolean(true), new Boolean(true));
    }

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = createIntermittentSale(new Long(123));

        // WHEN
        intermittentSaleDAO.create(intermittentSale);

        // THEN
        // try to find the intermittentsale and compare it
        List<IntermittentSale> storedObjects = intermittentSaleDAO.find(IntermittentSale.withIdentity(intermittentSale.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(intermittentSale, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = createIntermittentSale(null);

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
        IntermittentSale intermittentSale = createIntermittentSale(new Long(123));

        // check if user is stored
        intermittentSaleDAO.create(intermittentSale);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale).size());

        // GIVEN
        IntermittentSale intermittentSale2 = createIntermittentSale(intermittentSale.getIdentity(), "Sale2", intermittentSale.getEntries(), intermittentSale.getDuration(), intermittentSale.getEnabled(), intermittentSale.getFromDayTime(), intermittentSale.getMonday(), intermittentSale.getTuesday(), intermittentSale.getWednesday(), intermittentSale.getThursday(), intermittentSale.getFriday(), intermittentSale.getSaturday(), intermittentSale.getSunday());

        // WHEN
        intermittentSaleDAO.update(intermittentSale2);

        // THEN
        // check if the user has been updated;
        List<IntermittentSale> storedObjects = intermittentSaleDAO.find(intermittentSale2);
        assertEquals(1, storedObjects.size());
        assertEquals(intermittentSale2, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = createIntermittentSale(null);

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

        intermittentSale = createIntermittentSale(intermittentSale.getIdentity());

        // WHEN
        intermittentSaleDAO.update(intermittentSale);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // PREPARE
        final int numberBefore = intermittentSaleDAO.getAll().size();
        IntermittentSale intermittentSale = createIntermittentSale(new Long(123));

        // check if user is stored
        intermittentSaleDAO.create(intermittentSale);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale).size());

        // GIVEN
        IntermittentSale intermittentSale2 = IntermittentSale.withIdentity(intermittentSale.getIdentity());

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
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = createIntermittentSale(new Long(125));

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
        // Prepare

        //FromDayTimes
        LocalDateTime time1 = LocalDateTime.parse("2007-12-03T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2008-09-04T10:16:00");
        LocalDateTime time3 = LocalDateTime.parse("2009-10-05T10:17:50");

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));
        intermittentSale1.setFromDayTime(time1);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));
        intermittentSale2.setFromDayTime(time2);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = createIntermittentSale(new Long(125));
        intermittentSale3.setFromDayTime(time3);

        intermittentSaleDAO.create(intermittentSale3);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale3).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(new Long(123)); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setFromDayTime(time1);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(new Long(123)); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setFromDayTime(time2);
        IntermittentSale matcher3 = IntermittentSale.withIdentity(new Long(123)); // for sale 3
        matcher3.setIdentity(null);
        matcher3.setFromDayTime(time3);

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
    public void testFind_byDurationShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        //FromDayTimes
        Integer integer1 = new Integer(50);
        Integer integer2 = new Integer(30);
        Integer integer3 = new Integer(120);

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));
        intermittentSale1.setDuration(integer1);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));
        intermittentSale2.setDuration(integer2);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = createIntermittentSale(new Long(125));
        intermittentSale3.setDuration(integer3);

        intermittentSaleDAO.create(intermittentSale3);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale3).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(new Long(123)); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setDuration(integer1);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(new Long(123)); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setDuration(integer2);
        IntermittentSale matcher3 = IntermittentSale.withIdentity(new Long(123)); // for sale 3
        matcher3.setIdentity(null);
        matcher3.setDuration(integer3);

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
    public void testFind_byMondayShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));
        intermittentSale1.setMonday(true);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));
        intermittentSale2.setMonday(false);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(new Long(123)); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setMonday(true);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(new Long(123)); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setMonday(false);

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));
    }

    @Test
    public void testFind_byTuesdayShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));
        intermittentSale1.setTuesday(true);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));
        intermittentSale2.setTuesday(false);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(new Long(123)); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setTuesday(true);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(new Long(123)); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setTuesday(false);

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));
    }

    @Test
    public void testFind_byWednesdayShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));
        intermittentSale1.setWednesday(true);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));
        intermittentSale2.setWednesday(false);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(new Long(123)); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setWednesday(true);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(new Long(123)); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setWednesday(false);

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));
    }

    @Test
    public void testFind_byThursdayShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));
        intermittentSale1.setThursday(true);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));
        intermittentSale2.setThursday(false);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(new Long(123)); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setThursday(true);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(new Long(123)); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setThursday(false);

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));
    }

    @Test
    public void testFind_byFridayShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));
        intermittentSale1.setFriday(true);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));
        intermittentSale2.setFriday(false);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(new Long(123)); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setFriday(true);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(new Long(123)); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setFriday(false);

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));
    }

    @Test
    public void testFind_bySaturdayShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));
        intermittentSale1.setSaturday(true);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));
        intermittentSale2.setSaturday(false);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(new Long(123)); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setSaturday(true);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(new Long(123)); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setSaturday(false);

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));
    }
    
    @Test
    public void testFind_bySundayShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));
        intermittentSale1.setSunday(true);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));
        intermittentSale2.setSunday(false);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(new Long(123)); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setSunday(true);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(new Long(123)); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setSunday(false);

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException, ValidationException {
        //Find a non-existent ID
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(intermittentSale1.getIdentity().intValue()+3492189); // for sale 1

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);

        // THEN
        assertEquals(0, result1.size());
    }

    @Test
    public void testFind_nullObjectShouldReturnEmptyList() throws DAOException {
        // GIVEN
        IntermittentSale matcher1 = null;

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);

        // THEN
        assertEquals(0, result1.size());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(new Long(123));

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(124));

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = createIntermittentSale(new Long(125));

        intermittentSaleDAO.create(intermittentSale3);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale3).size());

        // WHEN
        List<IntermittentSale> result = intermittentSaleDAO.getAll();
        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(intermittentSale1));
        assertTrue(result.contains(intermittentSale2));
        assertTrue(result.contains(intermittentSale3));
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
    public void testCreate_shouldAddRemoveAndReaddObject() throws DAOException, ValidationException {
        IntermittentSale intermittentSale = createIntermittentSale(new Long(123));

        intermittentSaleDAO.create(intermittentSale);
        assertEquals(intermittentSaleDAO.find(intermittentSale).size(), 1);
        assertTrue(intermittentSaleDAO.find(intermittentSale).contains(intermittentSale));

        intermittentSaleDAO.delete(intermittentSale);
        assertEquals(intermittentSaleDAO.find(intermittentSale).size(), 0);
        assertFalse(intermittentSaleDAO.find(intermittentSale).contains(intermittentSale));

        intermittentSaleDAO.create(intermittentSale);
        assertEquals(intermittentSaleDAO.find(intermittentSale).size(), 1);
        assertTrue(intermittentSaleDAO.find(intermittentSale).contains(intermittentSale));
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN
        // create data
        IntermittentSale intermittentSale = createIntermittentSale(new Long(123));
        LocalDateTime createTime = LocalDateTime.now();
        intermittentSaleDAO.create(intermittentSale);

        // update data
        IntermittentSale intermittentSale2 = createIntermittentSale(intermittentSale.getIdentity(), "Sale2", intermittentSale.getEntries(), intermittentSale.getDuration(), intermittentSale.getEnabled(), intermittentSale.getFromDayTime(), intermittentSale.getMonday(), intermittentSale.getTuesday(), intermittentSale.getWednesday(), intermittentSale.getThursday(), intermittentSale.getFriday(), intermittentSale.getSaturday(), intermittentSale.getSunday());
        LocalDateTime updateTime = LocalDateTime.now();
        intermittentSaleDAO.update(intermittentSale2);

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        intermittentSaleDAO.delete(intermittentSale2);

        // WHEN
        List<History<IntermittentSale>> history = intermittentSaleDAO.getHistory(intermittentSale);

        // THEN
        assertEquals(3, history.size());

        // check create history
        History<IntermittentSale> entry = history.get(0);
        assertEquals(Long.valueOf(1), entry.getChangeNumber());
        assertEquals(intermittentSale, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(createTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check update history
        entry = history.get(1);
        assertEquals(Long.valueOf(2), entry.getChangeNumber());
        assertEquals(intermittentSale2, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(updateTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check delete history
        entry = history.get(2);
        assertEquals(Long.valueOf(3), entry.getChangeNumber());
        assertEquals(intermittentSale2, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(deleteTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertTrue(entry.isDeleted());
    }
}
