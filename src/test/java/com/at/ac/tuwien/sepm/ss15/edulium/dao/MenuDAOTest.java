package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by - on 11.05.2015.
 */
public class MenuDAOTest extends AbstractDAOTest {
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private DAO<Menu> menuDAO;
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

    private Menu createMenu(String name, List<MenuEntry> menueEntries) throws ValidationException, DAOException {
        for(MenuEntry entry:menueEntries){
            menuEntryDAO.create(entry);
        }
        Menu menu = new Menu();
        menu.setName(name);
        menu.setEntries(menueEntries);

        return menu;
    }

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        //Given
        LinkedList<MenuEntry> list = new LinkedList<MenuEntry>();
        list.add(createMenuEntry("entry", "desc", "cat", 20, 0.2, true));
        Menu men = createMenu("Menu", list);

        //When
        menuDAO.create(men);

        // THEN
        // check if identity is set
        assertNotNull(men.getIdentity());

        // check retrieving object
        List<Menu> storedObjects = menuDAO.find(Menu.withIdentity(men.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(men, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingEmptyObjectShouldFail() throws DAOException, ValidationException {
        //Given
        Menu men = null;

        //When
        try {
            menuDAO.create(men);
        } finally {
            assertNull(men.getIdentity());
        }
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        //  GIVEN
        LinkedList<MenuEntry> list = new LinkedList<MenuEntry>();
        list.add(createMenuEntry("entry", "desc", "cat", 20, 0.2, true));
        Menu men = createMenu("Menu", list);
        menuDAO.create(men);

        // check if entry is stored
        assertEquals(men, menuDAO.find(Menu.withIdentity(men.getIdentity())).get(0));

        // WHEN
        LinkedList<MenuEntry> list = new LinkedList<MenuEntry>();
        list.add(createMenuEntry("entry2", "descnew", "newcat", 19, 0.2, true));
        Menu updatedMenu = createMenu("newMenu", list);
        updatedMenu.setIdentity(men.getIdentity());
        menuDAO.update(men);

        // THEN
        // check if entry was updatedupdatedMenu
        List<Menu> storedObjects = menuDAO.find(Menu.withIdentity(updatedMenu.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(updatedMenu, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        //Given
        LinkedList<MenuEntry> list = new LinkedList<MenuEntry>();
        list.add(createMenuEntry("entry", "desc", "cat", 20, 0.2, true));
        Menu men = createMenu("Menu", list);

        //When
        menuDAO.update(men);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        LinkedList<MenuEntry> list = new LinkedList<MenuEntry>();
        list.add(createMenuEntry("entry", "desc", "cat", 20, 0.2, true));
        Menu men = createMenu("Menu", list);
        men.setIdentity(identity);

        // generate identity which is not used by any persistent object
        try {
            while (!menuDAO.find(men).isEmpty()) {
                identity++;
                men.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            fail();
        }

        // WHEN
        menuDAO.update(men);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // GIVEN
        LinkedList<MenuEntry> list = new LinkedList<MenuEntry>();
        list.add(createMenuEntry("entry", "desc", "cat", 20, 0.2, true));
        Menu men = createMenu("Menu", list);
        menuDAO.create(men);

        // check if entry created
        List<Menu> objects = menuDAO.find(Menu.withIdentity(men.getIdentity()));
        assertEquals(1, objects.size());
        assertEquals(men, objects.get(0));

        // WHEN
        menuDAO.delete(men);

        // THEN
        // check if entry was removed
        assertTrue(menuDAO.find(Menu.withIdentity(men.getIdentity())).isEmpty());
        assertTrue(menuDAO.getAll().isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Menu men = new Menu();

        // WHEN
        menuDAO.delete(men);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        Menu men = new Menu();
        men.setIdentity(identity);

        // generate identity which is not used by any persistent object
        try {
            while (!menuDAO.find(men).isEmpty()) {
                identity++;
                men.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            fail();
        }

        // WHEN
        menuDAO.delete(men);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // GIVEN
        LinkedList<MenuEntry> list1 = new LinkedList<MenuEntry>();
        list1.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men1 = createMenu("Menu1", list1);
        menuDAO.create(men1);

        LinkedList<MenuEntry> list2 = new LinkedList<MenuEntry>();
        list2.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men2 = createMenu("Menu2", list2);
        menuDAO.create(men2);

        LinkedList<MenuEntry> list3 = new LinkedList<MenuEntry>();
        list3.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men3 = createMenu("Menu3", list3);
        menuDAO.create(men3);

        // WHEN
        List<Menu> objects = menuDAO.find(Menu.withIdentity(men1.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(men1, objects.get(0));

        // WHEN
        List<Menu> objects2 = menuDAO.find(Menu.withIdentity(men2.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(men2, objects.get(0));

        // WHEN
        List<Menu> objects3 = menuDAO.find(Menu.withIdentity(men3.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(men3, objects.get(0));
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException, ValidationException {
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
    }

    @Test
    public void testGetAll_shouldReturnEmptyList() throws DAOException {
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {

    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {

    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {

    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {

    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {

    }
}
