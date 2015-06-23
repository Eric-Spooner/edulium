package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    private List<MenuEntry> createRandomEntries() throws ValidationException, DAOException {
        int count = 1 + (int) Math.random() * 10;
        List<MenuEntry> entries = new ArrayList();

        for(int i = 0; i < count; i++) {
            MenuEntry entry = createMenuEntry(Long.valueOf((long)Math.random() * 99), "entry", "desc", "cat", 20, 0.2, true);
            entries.add(entry);
        }

        return entries;
    }

    private OnetimeSale createOnetimeSale(Long identity, String name, LocalDateTime fromTime, LocalDateTime toTime, List<MenuEntry> entries) {
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(identity);
        onetimeSale.setName(name);
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
        OnetimeSale onetimeSale = createOnetimeSale(123L);

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
        OnetimeSale onetimeSale = createOnetimeSale(123L);

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
        OnetimeSale onetimeSale =  createOnetimeSale(123L);

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
        OnetimeSale onetimeSale1 = createOnetimeSale(123L);

        onetimeSaleDAO.create(onetimeSale1);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale1).size());

        // one time sale 2
        OnetimeSale onetimeSale2 =  createOnetimeSale(124L);

        onetimeSaleDAO.create(onetimeSale2);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale2).size());

        // one time sale 3
        OnetimeSale onetimeSale3 =  createOnetimeSale(125L);

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
        assertTrue(result1.contains(onetimeSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(onetimeSale2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(onetimeSale3));
    }

    @Test
    public void testFind_byFromTimeShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare
        LocalDateTime time1 = LocalDateTime.parse("2007-12-03T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2008-12-03T10:15:30");

        // one time sale 1
        OnetimeSale onetimeSale1 = createOnetimeSale(123L);
        onetimeSale1.setFromTime(time1);

        onetimeSaleDAO.create(onetimeSale1);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale1).size());

        // one time sale 2
        OnetimeSale onetimeSale2 =  createOnetimeSale(124L);
        onetimeSale2.setFromTime(time2);

        onetimeSaleDAO.create(onetimeSale2);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale2).size());

        // GIVEN
        OnetimeSale matcher1 = OnetimeSale.withIdentity(onetimeSale1.getIdentity()); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setFromTime(onetimeSale1.getFromTime());
        OnetimeSale matcher2 = OnetimeSale.withIdentity(onetimeSale2.getIdentity()); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setFromTime(onetimeSale2.getFromTime());

        // WHEN
        List<OnetimeSale> result1 = onetimeSaleDAO.find(matcher1);
        List<OnetimeSale> result2 = onetimeSaleDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(onetimeSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(onetimeSale2));
    }

    @Test
    public void testFind_byToTimeShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare
        LocalDateTime time1 = LocalDateTime.parse("2027-12-03T10:15:30");
        LocalDateTime time2 = LocalDateTime.parse("2028-12-03T10:15:30");

        // one time sale 1
        OnetimeSale onetimeSale1 = createOnetimeSale(123L);
        onetimeSale1.setToTime(time1);

        onetimeSaleDAO.create(onetimeSale1);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale1).size());

        // one time sale 2
        OnetimeSale onetimeSale2 =  createOnetimeSale(124L);
        onetimeSale2.setToTime(time2);

        onetimeSaleDAO.create(onetimeSale2);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale2).size());

        // GIVEN
        OnetimeSale matcher1 = OnetimeSale.withIdentity(onetimeSale1.getIdentity()); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setToTime(onetimeSale1.getToTime());
        OnetimeSale matcher2 = OnetimeSale.withIdentity(onetimeSale2.getIdentity()); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setToTime(onetimeSale2.getToTime());

        // WHEN
        List<OnetimeSale> result1 = onetimeSaleDAO.find(matcher1);
        List<OnetimeSale> result2 = onetimeSaleDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(onetimeSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(onetimeSale2));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException, ValidationException {
        // Prepare

        // one time sale 1
        OnetimeSale onetimeSale1 = createOnetimeSale(123L);

        onetimeSaleDAO.create(onetimeSale1);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale1).size());

        // GIVEN
        OnetimeSale matcher1 = OnetimeSale.withIdentity(onetimeSale1.getIdentity().intValue()+3492189); // for sale 1

        // WHEN
        List<OnetimeSale> result1 = onetimeSaleDAO.find(matcher1);

        // THEN
        assertEquals(0, result1.size());
    }

    @Test
    public void testFind_nullObjectShouldReturnEmptyList() throws DAOException, ValidationException {
        // Prepare

        // one time sale 1
        OnetimeSale onetimeSale1 = createOnetimeSale(123L);

        onetimeSaleDAO.create(onetimeSale1);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale1).size());

        // GIVEN
        OnetimeSale matcher1 = null; // for sale 1

        // WHEN
        List<OnetimeSale> result1 = onetimeSaleDAO.find(matcher1);

        // THEN
        assertEquals(0, result1.size());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // one time sale 1
        OnetimeSale onetimeSale1 = createOnetimeSale(123L);

        onetimeSaleDAO.create(onetimeSale1);

        // one time sale 2
        OnetimeSale onetimeSale2 =  createOnetimeSale(124L);

        onetimeSaleDAO.create(onetimeSale2);

        // one time sale 3
        OnetimeSale onetimeSale3 =  createOnetimeSale(125L);

        onetimeSaleDAO.create(onetimeSale3);

        // WHEN
        List<OnetimeSale> result1 = onetimeSaleDAO.getAll();

        // THEN
        assertEquals(3, result1.size());
        assertTrue(result1.contains(onetimeSale1));
        assertTrue(result1.contains(onetimeSale2));
        assertTrue(result1.contains(onetimeSale3));
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
    public void testCreate_shouldAddRemoveAndReaddObject() throws DAOException, ValidationException {
        OnetimeSale onetimeSale = createOnetimeSale(123L);

        onetimeSaleDAO.create(onetimeSale);
        assertEquals(onetimeSaleDAO.find(onetimeSale).size(), 1);
        assertTrue(onetimeSaleDAO.find(onetimeSale).contains(onetimeSale));

        onetimeSaleDAO.delete(onetimeSale);
        assertEquals(onetimeSaleDAO.find(onetimeSale).size(), 0);
        assertFalse(onetimeSaleDAO.find(onetimeSale).contains(onetimeSale));

        onetimeSaleDAO.create(onetimeSale);
        assertEquals(onetimeSaleDAO.find(onetimeSale).size(), 1);
        assertTrue(onetimeSaleDAO.find(onetimeSale).contains(onetimeSale));
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjects() throws DAOException, ValidationException {
        // Prepare

        // one time sale 1
        OnetimeSale onetimeSale1 = createOnetimeSale(123L);

        onetimeSaleDAO.create(onetimeSale1);

        // one time sale 2
        OnetimeSale onetimeSale2 =  createOnetimeSale(124L);

        onetimeSaleDAO.create(onetimeSale2);

        // one time sale 3
        OnetimeSale onetimeSale3 =  createOnetimeSale(125L);

        onetimeSaleDAO.create(onetimeSale3);

        List<OnetimeSale> list = Arrays.asList(onetimeSale1, onetimeSale2, onetimeSale3);

        // WHEN
        List<OnetimeSale> result1 = onetimeSaleDAO.populate(list);

        // THEN
        assertEquals(3, result1.size());
        assertTrue(result1.contains(onetimeSale1));
        assertTrue(result1.contains(onetimeSale2));
        assertTrue(result1.contains(onetimeSale3));
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjectsOfDeletedObjects() throws DAOException, ValidationException {
        // Prepare

        // one time sale 1
        OnetimeSale onetimeSale1 = createOnetimeSale(123L);

        onetimeSaleDAO.create(onetimeSale1);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale1).size());
        onetimeSaleDAO.delete(onetimeSale1);
        assertEquals(0, onetimeSaleDAO.find(onetimeSale1).size());

        // one time sale 2
        OnetimeSale onetimeSale2 =  createOnetimeSale(124L);

        onetimeSaleDAO.create(onetimeSale2);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale2).size());
        onetimeSaleDAO.delete(onetimeSale2);
        assertEquals(0, onetimeSaleDAO.find(onetimeSale2).size());

        // one time sale 3
        OnetimeSale onetimeSale3 =  createOnetimeSale(125L);

        onetimeSaleDAO.create(onetimeSale3);
        assertEquals(1, onetimeSaleDAO.find(onetimeSale3).size());
        onetimeSaleDAO.delete(onetimeSale3);
        assertEquals(0, onetimeSaleDAO.find(onetimeSale3).size());

        List<OnetimeSale> list = Arrays.asList(onetimeSale1, onetimeSale2, onetimeSale3);

        // WHEN
        List<OnetimeSale> result1 = onetimeSaleDAO.populate(list);

        // THEN
        assertEquals(3, result1.size());
        assertTrue(result1.contains(onetimeSale1));
        assertTrue(result1.contains(onetimeSale2));
        assertTrue(result1.contains(onetimeSale3));
    }

    @Test
    public void testPopulate_nullListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // GIVEN
        List<OnetimeSale> invalidSales = null;

        // WHEN
        List<OnetimeSale> result = onetimeSaleDAO.populate(invalidSales);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPopulate_emptyListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // GIVEN
        List<OnetimeSale> invalidSales = Arrays.asList();

        // WHEN
        List<OnetimeSale> result = onetimeSaleDAO.populate(invalidSales);
        assertTrue(result.isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithInvalidObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<OnetimeSale> invalidSales = Arrays.asList(new OnetimeSale());

        // WHEN
        List<OnetimeSale> result = onetimeSaleDAO.populate(invalidSales);
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithNullObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<OnetimeSale> invalidSales = new ArrayList<>();
        invalidSales.add(null);

        // WHEN
        List<OnetimeSale> result = onetimeSaleDAO.populate(invalidSales);
    }


    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN
        // create data
        OnetimeSale onetimeSale = createOnetimeSale(123L);
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