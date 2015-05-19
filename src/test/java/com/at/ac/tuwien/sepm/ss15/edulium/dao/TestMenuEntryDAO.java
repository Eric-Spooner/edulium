package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test for the MenuEntryDAO
 */
public class TestMenuEntryDAO extends AbstractDAOTest {
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private DAO<MenuCategory> menuCategoryDAO;
    @Autowired
    private DAO<TaxRate> taxRateDAO;

    private MenuEntry createMenuEntry(String name, String desc, String cat,
                                      double price, double tax, boolean available) throws ValidationException, DAOException {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName(cat);

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(tax));

        menuCategoryDAO.create(menuCategory);
        taxRateDAO.create(taxRate);

        MenuEntry entry = new MenuEntry();
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        entry.setName(name);
        entry.setDescription(desc);
        entry.setPrice(BigDecimal.valueOf(price));
        entry.setAvailable(available);

        return entry;
    }

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        //  GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20, 0.2, true);

        // WHEN
        menuEntryDAO.create(entry);

        // THEN
        // check if identity is set
        assertNotNull(entry.getIdentity());

        // check retrieving object
        List<MenuEntry> storedObjects = menuEntryDAO.find(MenuEntry.withIdentity(entry.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(entry, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingEmptyObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuEntry entry = new MenuEntry();

        // WHEN
        try {
            menuEntryDAO.create(entry);
        } finally {
            assertNull(entry.getIdentity());
        }
    }

    @Test(expected = DAOException.class)
    public void testCreate_addingObjectWithoutPersistentCategoryShouldFail() throws DAOException, ValidationException {
        // PREPARE
        Long identity = 0L;
        MenuCategory cat = new MenuCategory();
        cat.setIdentity(identity);

        // generate identity for the category which is not used by any persistent category
        try {
            while (!menuCategoryDAO.find(cat).isEmpty()) {
                identity++;
                cat.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            fail();
        }

        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20, 0.2, true);
        // set category which has a identity, but is not persistent
        entry.setCategory(cat);

        // WHEN
        try {
            menuEntryDAO.create(entry);
        } finally {
            assertNull(entry.getIdentity());
        }
    }

    @Test(expected = DAOException.class)
    public void testCreate_addingObjectWithoutPersistentTaxRateShouldFail() throws DAOException, ValidationException {
        // PREPARE
        Long identity = 0L;
        TaxRate tax = new TaxRate();
        tax.setIdentity(identity);

        // generate identity for the taxRate which is not used by any persistent taxRate
        try {
            while (!taxRateDAO.find(tax).isEmpty()) {
                identity++;
                tax.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            fail();
        }

        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20, 0.2, true);
        // set taxRate which has a identity, but is not persistent
        entry.setTaxRate(tax);

        // WHEN
        try {
            menuEntryDAO.create(entry);
        } finally {
            assertNull(entry.getIdentity());
        }
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        //  GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        menuEntryDAO.create(entry);

        // check if entry is stored
        List<MenuEntry> storedObjects = menuEntryDAO.find(MenuEntry.withIdentity(entry.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(entry, storedObjects.get(0));

        // WHEN
        MenuEntry updatedEntry = createMenuEntry("newEntry", "newDesc", "newCat", 19.99, 0.5, false);
        updatedEntry.setIdentity(entry.getIdentity());
        menuEntryDAO.update(updatedEntry);

        // THEN
        // check if entry was updated
        storedObjects = menuEntryDAO.find(MenuEntry.withIdentity(updatedEntry.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(updatedEntry, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);

        // WHEN
        menuEntryDAO.update(entry);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // PREPARE
        Long identity = (long) 1;
        MenuEntry matcher = new MenuEntry();
        matcher.setIdentity(identity);
        // generate identity which is not used by any persistent object
        try {
            while (!menuEntryDAO.find(matcher).isEmpty()) {
                identity++;
                matcher.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            fail();
        }

        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setIdentity(identity);

        // WHEN
        menuEntryDAO.update(entry);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // GIVEN
        int initialSize = menuEntryDAO.getAll().size();
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        menuEntryDAO.create(entry);

        // check if entry created
        assertEquals(initialSize + 1, menuEntryDAO.getAll().size());

        // WHEN
        menuEntryDAO.delete(entry);

        // THEN
        // check if entry was removed
        assertTrue(menuEntryDAO.find(MenuEntry.withIdentity(entry.getIdentity())).isEmpty());
        assertEquals(initialSize, menuEntryDAO.getAll().size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuEntryDAO.delete(entry);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        MenuEntry entry = new MenuEntry();
        entry.setIdentity(identity);

        // generate identity which is not used by any persistent object
        try {
            while (!menuEntryDAO.find(entry).isEmpty()) {
                identity++;
                entry.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            fail();
        }

        // WHEN
        menuEntryDAO.delete(entry);
    }

    @Test
    public void testFind_withoutObjectShouldReturnEmptyList() throws DAOException, ValidationException {
        MenuEntry matcher = null;

        assertTrue(menuEntryDAO.find(matcher).isEmpty());
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // GIVEN
        MenuEntry entry1 = createMenuEntry("entry1", "desc1", "cat1", 10.0, 0.1, true);
        menuEntryDAO.create(entry1);

        MenuEntry entry2 = createMenuEntry("entry2", "desc2", "cat2", 20.0, 0.2, true);
        menuEntryDAO.create(entry2);

        MenuEntry entry3 = createMenuEntry("entry3", "desc3", "cat3", 30.0, 0.3, true);
        menuEntryDAO.create(entry3);

        // WHEN
        List<MenuEntry> objects = menuEntryDAO.find(MenuEntry.withIdentity(entry1.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(entry1, objects.get(0));

        // WHEN
        objects = menuEntryDAO.find(MenuEntry.withIdentity(entry2.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(entry2, objects.get(0));

        // WHEN
        objects = menuEntryDAO.find(MenuEntry.withIdentity(entry3.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(entry3, objects.get(0));
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        MenuEntry matcher = new MenuEntry();

        MenuEntry entry1 = createMenuEntry("entry1", "desc1", "cat1", 10.0, 0.1, true);
        menuEntryDAO.create(entry1);

        MenuEntry entry2 = createMenuEntry("entry1", "desc2", "cat2", 20.0, 0.2, true);
        menuEntryDAO.create(entry2);

        MenuEntry entry3 = createMenuEntry("entry2", "desc3", "cat3", 30.0, 0.3, true);
        menuEntryDAO.create(entry3);

        // WHEN
        matcher.setName(entry1.getName());
        List<MenuEntry> objects = menuEntryDAO.find(matcher);

        // THEN
        assertEquals(2, objects.size());
        assertTrue(objects.contains(entry1));
        assertTrue(objects.contains(entry2));

        // WHEN
        matcher.setName(entry3.getName());
        objects = menuEntryDAO.find(matcher);

        // THEN
        assertEquals(1, objects.size());
        assertEquals(entry3, objects.get(0));
    }

    @Test
    public void testFind_byAvailabilityShouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        MenuEntry matcher = new MenuEntry();

        MenuEntry entry1 = createMenuEntry("entry1", "desc1", "cat1", 10.0, 0.1, true);
        menuEntryDAO.create(entry1);

        MenuEntry entry2 = createMenuEntry("entry1", "desc2", "cat2", 20.0, 0.2, true);
        menuEntryDAO.create(entry2);

        MenuEntry entry3 = createMenuEntry("entry2", "desc3", "cat3", 30.0, 0.3, false);
        menuEntryDAO.create(entry3);

        // WHEN
        matcher.setAvailable(true);
        List<MenuEntry> objects = menuEntryDAO.find(matcher);

        // THEN
        assertEquals(2, objects.size());
        assertTrue(objects.contains(entry1));
        assertTrue(objects.contains(entry2));

        // WHEN
        matcher.setAvailable(false);
        objects = menuEntryDAO.find(matcher);

        // THEN
        assertEquals(1, objects.size());
        assertEquals(entry3, objects.get(0));
    }

    @Test
    public void testFind_byPriceShouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        MenuEntry matcher = new MenuEntry();

        MenuEntry entry1 = createMenuEntry("entry1", "desc1", "cat1", 10.0, 0.1, true);
        menuEntryDAO.create(entry1);

        MenuEntry entry2 = createMenuEntry("entry1", "desc2", "cat2", 10.0, 0.2, true);
        menuEntryDAO.create(entry2);

        MenuEntry entry3 = createMenuEntry("entry2", "desc3", "cat3", 20.0, 0.3, true);
        menuEntryDAO.create(entry3);

        // WHEN
        matcher.setPrice(BigDecimal.valueOf(10.00));
        List<MenuEntry> objects = menuEntryDAO.find(matcher);

        // THEN
        assertEquals(2, objects.size());
        assertTrue(objects.contains(entry1));
        assertTrue(objects.contains(entry2));

        // WHEN
        matcher.setPrice(BigDecimal.valueOf(20.000));
        objects = menuEntryDAO.find(matcher);

        // THEN
        assertEquals(1, objects.size());
        assertEquals(entry3, objects.get(0));
    }

    @Test
    public void testFind_byCategoryShouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        MenuEntry matcher = new MenuEntry();

        // create categories
        MenuCategory cat1 = new MenuCategory();
        cat1.setName("cat1");
        MenuCategory cat2 = new MenuCategory();
        cat2.setName("cat2");

        menuCategoryDAO.create(cat1);
        menuCategoryDAO.create(cat2);

        // check if categories created
        List<MenuCategory> entries = menuCategoryDAO.find(cat1);
        assertEquals(1, entries.size());
        assertEquals(cat1, entries.get(0));

        entries = menuCategoryDAO.find(cat2);
        assertEquals(1, entries.size());
        assertEquals(cat2, entries.get(0));

        // GIVEN
        MenuEntry entry1 = createMenuEntry("entry1", "desc1", "cat1", 10.0, 0.1, true);
        // override category from createMenuEntry()
        entry1.setCategory(cat1);
        menuEntryDAO.create(entry1);

        MenuEntry entry2 = createMenuEntry("entry1", "desc2", "cat2", 20.0, 0.2, true);
        // override category from createMenuEntry()
        entry2.setCategory(cat1);
        menuEntryDAO.create(entry2);

        MenuEntry entry3 = createMenuEntry("entry2", "desc3", "cat3", 30.0, 0.3, true);
        // override category from createMenuEntry()
        entry3.setCategory(cat2);
        menuEntryDAO.create(entry3);

        // WHEN
        matcher.setCategory(cat1);
        List<MenuEntry> objects = menuEntryDAO.find(matcher);

        // THEN
        assertEquals(2, objects.size());
        assertTrue(objects.contains(entry1));
        assertTrue(objects.contains(entry2));

        // WHEN
        matcher.setCategory(cat2);
        objects = menuEntryDAO.find(matcher);

        // THEN
        assertEquals(1, objects.size());
        assertEquals(entry3, objects.get(0));
    }

    @Test
    public void testFind_byTaxRateShouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        MenuEntry matcher = new MenuEntry();

        // create tax rates
        TaxRate tax1 = new TaxRate();
        tax1.setValue(BigDecimal.valueOf(0.1));
        TaxRate tax2 = new TaxRate();
        tax2.setValue(BigDecimal.valueOf(0.2));

        taxRateDAO.create(tax1);
        taxRateDAO.create(tax2);

        // check if tax rates created
        List<TaxRate> storedObjects = taxRateDAO.find(tax1);
        assertEquals(1, storedObjects.size());
        assertEquals(tax1, storedObjects.get(0));

        storedObjects = taxRateDAO.find(tax2);
        assertEquals(1, storedObjects.size());
        assertEquals(tax2, storedObjects.get(0));

        // GIVEN
        MenuEntry entry1 = createMenuEntry("entry1", "desc1", "cat1", 10.0, 0.1, true);
        // override taxRate from createMenuEntry()
        entry1.setTaxRate(tax1);
        menuEntryDAO.create(entry1);

        MenuEntry entry2 = createMenuEntry("entry1", "desc2", "cat2", 20.0, 0.2, true);
        // override taxRate from createMenuEntry()
        entry2.setTaxRate(tax1);
        menuEntryDAO.create(entry2);

        MenuEntry entry3 = createMenuEntry("entry2", "desc3", "cat3", 30.0, 0.3, true);
        // override taxRate from createMenuEntry()
        entry3.setTaxRate(tax2);
        menuEntryDAO.create(entry3);

        // WHEN
        matcher.setTaxRate(tax1);
        List<MenuEntry> objects = menuEntryDAO.find(matcher);

        // THEN
        assertEquals(2, objects.size());
        assertTrue(objects.contains(entry1));
        assertTrue(objects.contains(entry2));

        // WHEN
        matcher.setTaxRate(tax2);
        objects = menuEntryDAO.find(matcher);

        // THEN
        assertEquals(1, objects.size());
        assertEquals(entry3, objects.get(0));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        Long identity = (long) 1;
        MenuEntry matcher = new MenuEntry();
        matcher.setIdentity(identity);

        // generate identity which is not used by any persistent object
        while (!menuEntryDAO.find(matcher).isEmpty()) {
            identity++;
            matcher.setIdentity(identity);
        }

        // WHEN
        List<MenuEntry> storedObjects = menuEntryDAO.find(matcher);

        // THEN
        assertTrue(storedObjects.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        MenuEntry entry1 = createMenuEntry("entry1", "desc1", "cat1", 10.0, 0.1, true);
        menuEntryDAO.create(entry1);

        MenuEntry entry2 = createMenuEntry("entry1", "desc2", "cat2", 20.0, 0.2, true);
        menuEntryDAO.create(entry2);

        MenuEntry entry3 = createMenuEntry("entry2", "desc3", "cat3", 30.0, 0.3, true);
        menuEntryDAO.create(entry3);

        // WHEN
        List<MenuEntry> objects = menuEntryDAO.getAll();

        // THEN
        assertTrue(objects.contains(entry1));
        assertTrue(objects.contains(entry2));
        assertTrue(objects.contains(entry3));
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        menuEntryDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuEntryDAO.getHistory(entry);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        MenuEntry entry = new MenuEntry();
        entry.setIdentity(identity);

        // generate identity which is not used by any persistent object
        while (!menuEntryDAO.find(entry).isEmpty()) {
            identity++;
            entry.setIdentity(identity);
        }

        // WHEN / THEN
        assertTrue(menuEntryDAO.getHistory(entry).isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN
        // create data
        MenuEntry entry1 = createMenuEntry("entry1", "desc1", "cat1", 10.0, 0.1, true);
        LocalDateTime createTime = LocalDateTime.now();
        menuEntryDAO.create(entry1);

        // update data
        MenuEntry entry2 = createMenuEntry("entry2", "desc2", "cat2", 20.0, 0.2, false);
        entry2.setIdentity(entry1.getIdentity());
        LocalDateTime updateTime = LocalDateTime.now();
        menuEntryDAO.update(entry2);

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        menuEntryDAO.delete(entry2);

        // WHEN
        List<History<MenuEntry>> history = menuEntryDAO.getHistory(entry2);

        // THEN
        assertEquals(3, history.size());

        // check create history
        History<MenuEntry> historyEntry = history.get(0);
        assertEquals(Long.valueOf(1), historyEntry.getChangeNumber());
        assertEquals(entry1, historyEntry.getData());
        assertEquals(user, historyEntry.getUser());
        assertTrue(Duration.between(createTime, historyEntry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(historyEntry.isDeleted());

        // check update history
        historyEntry = history.get(1);
        assertEquals(Long.valueOf(2), historyEntry.getChangeNumber());
        assertEquals(entry2, historyEntry.getData());
        assertEquals(user, historyEntry.getUser());
        assertTrue(Duration.between(updateTime, historyEntry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(historyEntry.isDeleted());

        // check delete history
        historyEntry = history.get(2);
        assertEquals(Long.valueOf(3), historyEntry.getChangeNumber());
        assertEquals(entry2, historyEntry.getData());
        assertEquals(user, historyEntry.getUser());
        assertTrue(Duration.between(deleteTime, historyEntry.getTimeOfChange()).getSeconds() < 1);
        assertTrue(historyEntry.isDeleted());
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjects() throws DAOException, ValidationException {
        // PREPARE
        MenuEntry entry1 = createMenuEntry("entry1", "desc1", "cat1", 10.0, 0.1, true);
        menuEntryDAO.create(entry1);

        MenuEntry entry2 = createMenuEntry("entry1", "desc2", "cat2", 20.0, 0.2, true);
        menuEntryDAO.create(entry2);

        MenuEntry entry3 = createMenuEntry("entry2", "desc3", "cat3", 30.0, 0.3, true);
        menuEntryDAO.create(entry3);

        // GIVEN
        MenuEntry entryId1 = MenuEntry.withIdentity(entry1.getIdentity());
        MenuEntry entryId2 = MenuEntry.withIdentity(entry2.getIdentity());
        MenuEntry entryId3 = MenuEntry.withIdentity(entry3.getIdentity());
        List<MenuEntry> entryIds = Arrays.asList(entryId1, entryId2, entryId3);

        // WHEN
        List<MenuEntry> result = menuEntryDAO.populate(entryIds);

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(entry1));
        assertTrue(result.contains(entry2));
        assertTrue(result.contains(entry3));
    }

    @Test
    public void testPopulate_nullListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // WHEN
        List<MenuEntry> result = menuEntryDAO.populate(null);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPopulate_emptyListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // WHEN
        List<MenuEntry> result = menuEntryDAO.populate(Arrays.asList());

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithInvalidObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<MenuEntry> invalidEntries = Arrays.asList(new MenuEntry());

        // WHEN
        List<MenuEntry> result = menuEntryDAO.populate(invalidEntries);
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithNullObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<MenuEntry> invalidEntries = Arrays.asList(null);

        // WHEN
        List<MenuEntry> result = menuEntryDAO.populate(invalidEntries);
    }
}
