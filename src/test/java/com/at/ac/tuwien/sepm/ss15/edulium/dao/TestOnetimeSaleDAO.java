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
 * Unit Test for the OnetimeSaleDAO
 */
public class TestOnetimeSaleDAO extends AbstractDAOTest {
    @Autowired
    private DAO<OnetimeSale> onetimeSaleDAO;
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

    private OnetimeSale createOnetimeSale(Long identity, String name, LocalDateTime fromTime, LocalDateTime toTime, Hashtable<MenuEntry, BigDecimal> entries) {
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(identity);
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());
        onetimeSale.setEntries(entries);
        return onetimeSale;
    }

    private OnetimeSale createOnetimeSale(Long identity) throws ValidationException, DAOException {
        return createOnetimeSale(identity, "Sale", LocalDateTime.now(), LocalDateTime.now(), createRandomEntries());
    }

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = createOnetimeSale(new Long(123));

        // WHEN
        onetimeSaleDAO.create(onetimeSale);

        // THEN
        // try to find the onetimesale and compare it
        List<OnetimeSale> storedObjects = onetimeSaleDAO.find(OnetimeSale.withIdentity(onetimeSale.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(onetimeSale, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = createOnetimeSale(null);

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
        OnetimeSale onetimeSale = createOnetimeSale(new Long(123));

        // check if user is stored
        onetimeSaleDAO.create(onetimeSale);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale).size());

        // GIVEN
        OnetimeSale onetimeSale2 = createOnetimeSale(onetimeSale.getIdentity(), "Sale2",onetimeSale.getFromTime(), onetimeSale.getToTime(),onetimeSale.getEntries());

        // WHEN
        onetimeSaleDAO.update(onetimeSale2);

        // THEN
        // check if the sale has been updated;
        List<OnetimeSale> storedObjects = onetimeSaleDAO.find(onetimeSale2);
        assertEquals(1, storedObjects.size());
        assertEquals(onetimeSale2, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
       // GIVEN
        OnetimeSale onetimeSale = createOnetimeSale(null);
        onetimeSale.setIdentity(null);

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
        OnetimeSale sale = null;
        try {
            sale = createOnetimeSale(null);
        } catch (Exception e) {
            fail("Exception occurred too early.");
        }

        // search for a non-existing onetimesale identity
        try {
            do {
                sale.setIdentity(new Long((int)(Math.random()*999999999)));
            } while (!onetimeSaleDAO.find(sale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing onetimesale identity");
        }

        // WHEN
        onetimeSaleDAO.update(sale);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // PREPARE
        final int numberBefore = onetimeSaleDAO.getAll().size();
        OnetimeSale onetimeSale =  createOnetimeSale(new Long(123));

        // check if sale is stored
        onetimeSaleDAO.create(onetimeSale);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale).size());

        // GIVEN
        OnetimeSale onetimeSale2 = createOnetimeSale(onetimeSale.getIdentity());

        // WHEN
        onetimeSaleDAO.delete(onetimeSale2);

        // THEN
        // check if the user has been updated;
        List<OnetimeSale> storedObjects = onetimeSaleDAO.find(onetimeSale2);
        assertEquals(numberBefore, storedObjects.size());
        assertEquals(0, onetimeSaleDAO.find(onetimeSale).size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        onetimeSaleDAO.delete(onetimeSale);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale sale = null;
        try {
            sale = createOnetimeSale(null);
        } catch (Exception e) {
            fail("Exception occurred too early.");
        }

        // search for a non-existing onetimesale identity
        try {
            do {
                sale.setIdentity(new Long((int)(Math.random()*999999999)));
            } while (!onetimeSaleDAO.find(sale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing onetimesale identity");
        }

        // WHEN
        onetimeSaleDAO.delete(sale);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // Prepare

        // one time sale 1
        OnetimeSale onetimeSale1 = createOnetimeSale(new Long(123));

        onetimeSaleDAO.create(onetimeSale1);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale1).size());

        // one time sale 2
        OnetimeSale onetimeSale2 =  createOnetimeSale(new Long(124));

        onetimeSaleDAO.create(onetimeSale2);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale2).size());

        // one time sale 3
        OnetimeSale onetimeSale3 =  createOnetimeSale(new Long(125));

        onetimeSaleDAO.create(onetimeSale3);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale3).size());

        // GIVEN
        OnetimeSale matcher1 = OnetimeSale.withIdentity(onetimeSale1.getIdentity()); // for sale 1
        OnetimeSale matcher2 = OnetimeSale.withIdentity(onetimeSale2.getIdentity()); // for sale 2
        OnetimeSale matcher3 = OnetimeSale.withIdentity(onetimeSale3.getIdentity()); // for sale 3

        // WHEN
        List<OnetimeSale> result1 = onetimeSaleDAO.find(matcher1);
        List<OnetimeSale> result2 = onetimeSaleDAO.find(matcher2);
        List<OnetimeSale> result3 = onetimeSaleDAO.find(matcher3);

        // THEN
        assertEquals(1, result1.size());
        System.out.println(onetimeSale1);
        System.out.println(result1.get(0));
        assertTrue(result1.contains(onetimeSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(onetimeSale2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(onetimeSale3));
    }

    @Test
    public void testFind_byFromTimeShouldReturnObjects() throws DAOException, ValidationException {

    }

    @Test
    public void testFind_byToTimeShouldReturnObjects() throws DAOException, ValidationException {

    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        //find a non existent id
    }

    @Test
    public void testFind_nullObjectShouldReturnEmptyList() throws DAOException {

    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {

    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        onetimeSaleDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();

        // WHEN
        onetimeSaleDAO.getHistory(onetimeSale);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        OnetimeSale sale =  createOnetimeSale(null);

        // search for a non-existing onetimesale identity
        try {
            do {
                sale.setIdentity(new Long((int)(Math.random()*999999999)));
            } while (!onetimeSaleDAO.find(sale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing onetimesale identity");
        }

        assertTrue(onetimeSaleDAO.getHistory(sale).isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
// PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN
        // create data
        OnetimeSale onetimeSale = createOnetimeSale(new Long(123));
        LocalDateTime createTime = LocalDateTime.now();
        onetimeSaleDAO.create(onetimeSale);

        // update data
        OnetimeSale onetimeSale2 = createOnetimeSale(onetimeSale.getIdentity(), "Sale2", onetimeSale.getFromTime(), onetimeSale.getToTime(), onetimeSale.getEntries());
        LocalDateTime updateTime = LocalDateTime.now();
        onetimeSaleDAO.update(onetimeSale2);

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        onetimeSaleDAO.delete(onetimeSale2);

        // WHEN
        List<History<OnetimeSale>> history = onetimeSaleDAO.getHistory(onetimeSale);

        // THEN
        assertEquals(3, history.size());

        // check create history
        History<OnetimeSale> entry = history.get(0);
        assertEquals(Long.valueOf(1), entry.getChangeNumber());
        assertEquals(onetimeSale, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(createTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check update history
        entry = history.get(1);
        assertEquals(Long.valueOf(2), entry.getChangeNumber());
        assertEquals(onetimeSale2, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(updateTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check delete history
        entry = history.get(2);
        assertEquals(Long.valueOf(3), entry.getChangeNumber());
        assertEquals(onetimeSale2, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(deleteTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertTrue(entry.isDeleted());
    }
}
