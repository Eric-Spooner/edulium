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
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test for the MenuEntryDAO
 */
public class MenuEntryDAOTest extends AbstractDAOTest {
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

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        //  GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        menuEntryDAO.create(entry);

        // check if entry is stored
        assertEquals(entry, menuEntryDAO.find(MenuEntry.withIdentity(entry.getIdentity())).get(0));

        // WHEN
        MenuEntry updatedEntry = createMenuEntry("newEntry", "newDesc", "newCat", 19.99, 0.5, false);
        updatedEntry.setIdentity(entry.getIdentity());
        menuEntryDAO.update(entry);

        // THEN
        // check if entry was updated
        List<MenuEntry> storedObjects = menuEntryDAO.find(MenuEntry.withIdentity(updatedEntry.getIdentity()));
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
        // GIVEN
        Long identity = (long) 1;
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
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
        menuEntryDAO.update(entry);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        menuEntryDAO.create(entry);

        // check if entry created
        List<MenuEntry> objects = menuEntryDAO.find(MenuEntry.withIdentity(entry.getIdentity()));
        assertEquals(1, objects.size());
        assertEquals(entry, objects.get(0));

        // WHEN
        menuEntryDAO.delete(entry);

        // THEN
        // check if entry was removed
        assertTrue(menuEntryDAO.find(MenuEntry.withIdentity(entry.getIdentity())).isEmpty());
        assertTrue(menuEntryDAO.getAll().isEmpty());
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
    public void testGetAll_shouldReturnEmptyList() throws DAOException {
        // WHEN / THEN
        assertTrue(menuEntryDAO.getAll().isEmpty());
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
        assertEquals(3, objects.size());
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
}
