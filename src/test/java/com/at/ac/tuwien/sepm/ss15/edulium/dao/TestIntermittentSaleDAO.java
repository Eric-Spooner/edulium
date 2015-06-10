package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.IntermittentSale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
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
        intermittentSale.setName("Sale");
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


    @Test(expected = DAOException.class)
    public void testCreate_addingTwoObjectsWithSameIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = createIntermittentSale(new Long(123));

        // WHEN
        intermittentSaleDAO.create(intermittentSale);

        // GIVEN
        IntermittentSale intermittentSale2 = createIntermittentSale(new Long(123));

        // WHEN
        intermittentSaleDAO.create(intermittentSale2);
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
        IntermittentSale intermittentSale2 = IntermittentSale.withIdentity(intermittentSale.getIdentity());
        intermittentSale2.setName("Sale2");

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
