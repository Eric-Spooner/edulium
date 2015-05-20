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
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test for MenuDAO
 */
public class TestMenuDAO extends AbstractDAOTest {
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private DAO<Menu> menuDAO;
    @Autowired
    private DAO<MenuCategory> menuCategoryDAO;
    @Autowired
    private DAO<TaxRate> taxRateDAO;

    private MenuEntry createMenuEntry(String name, String desc, String cat, double price, double tax, boolean available)
            throws ValidationException, DAOException {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName(cat);
        menuCategoryDAO.create(menuCategory);

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(tax));
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
        LinkedList<MenuEntry> list = new LinkedList<>();
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
        Menu men = new Menu();

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
        LinkedList<MenuEntry> list = new LinkedList<>();
        list.add(createMenuEntry("entry", "desc", "cat", 20, 0.2, true));
        Menu men = createMenu("Menu", list);
        menuDAO.create(men);

        // check if entry is stored
        assertEquals(men, menuDAO.find(Menu.withIdentity(men.getIdentity())).get(0));

        // WHEN
        LinkedList<MenuEntry> list2 = new LinkedList<>();
        list2.add(createMenuEntry("entry2", "descnew", "newcat", 19, 0.2, true));
        Menu updatedMenu = createMenu("newMenu", list2);
        updatedMenu.setIdentity(men.getIdentity());
        menuDAO.update(updatedMenu);

        // THEN
        // check if entry was updated
        List<Menu> storedObjects = menuDAO.find(Menu.withIdentity(updatedMenu.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(updatedMenu, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        //Given
        LinkedList<MenuEntry> list = new LinkedList<>();
        list.add(createMenuEntry("entry", "desc", "cat", 20, 0.2, true));
        Menu men = createMenu("Menu", list);

        //When
        menuDAO.update(men);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
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
            //The Test shoudln't fail while searching
            fail();
        }

        LinkedList<MenuEntry> list = new LinkedList<>();
        list.add(createMenuEntry("entry", "desc", "cat", 20, 0.2, true));
        Menu men2 = createMenu("Menu", list);
        men2.setIdentity(men.getIdentity()+1);

        // WHEN
        menuDAO.update(men2);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // GIVEN
        LinkedList<MenuEntry> list = new LinkedList<>();
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
            // The test shouldn't fail while searching
            fail();
        }

        // WHEN
        menuDAO.delete(men);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // GIVEN
        LinkedList<MenuEntry> list1 = new LinkedList<>();
        list1.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men1 = createMenu("Menu1", list1);
        menuDAO.create(men1);

        LinkedList<MenuEntry> list2 = new LinkedList<>();
        list2.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men2 = createMenu("Menu2", list2);
        menuDAO.create(men2);

        LinkedList<MenuEntry> list3 = new LinkedList<>();
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
        assertEquals(1, objects2.size());
        assertEquals(men2, objects2.get(0));

        // WHEN
        List<Menu> objects3 = menuDAO.find(Menu.withIdentity(men3.getIdentity()));
        // THEN
        assertEquals(1, objects3.size());
        assertEquals(men3, objects3.get(0));
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        Menu matcher = new Menu();

        LinkedList<MenuEntry> list1 = new LinkedList<>();
        list1.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men1 = createMenu("Menu", list1);
        menuDAO.create(men1);

        LinkedList<MenuEntry> list2 = new LinkedList<>();
        list2.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men2 = createMenu("Menu", list2);
        menuDAO.create(men2);

        LinkedList<MenuEntry> list3 = new LinkedList<>();
        list3.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men3 = createMenu("Menu3", list3);
        menuDAO.create(men3);


        // WHEN
        matcher.setName(men1.getName());
        List<Menu> objects = menuDAO.find(matcher);

        // THEN
        assertEquals(2, objects.size());
        assertTrue(objects.contains(men1));
        assertTrue(objects.contains(men2));

        // WHEN
        matcher.setName(men3.getName());
        objects = menuDAO.find(matcher);

        // THEN
        assertEquals(1, objects.size());
        assertEquals(men3, objects.get(0));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        Long identity = (long) 1;
        Menu matcher = new Menu();
        matcher.setIdentity(identity);

        // generate identity which is not used by any persistent object
        while (!menuDAO.find(matcher).isEmpty()) {
            identity++;
            matcher.setIdentity(identity);
        }

        // WHEN
        List<Menu> storedObjects = menuDAO.find(matcher);

        // THEN
        assertTrue(storedObjects.isEmpty());
    }


    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        //WHEN THEN
        menuDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Menu entry = new Menu();

        // WHEN
        menuDAO.getHistory(entry);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        Menu matcher = new Menu();
        matcher.setIdentity(identity);

        // generate identity which is not used by any persistent object
        while (!menuDAO.find(matcher).isEmpty()) {
            identity++;
            matcher.setIdentity(identity);
        }

        //WHEN THEN
        assertTrue(menuDAO.getHistory(matcher).isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN
        // create data
        LinkedList<MenuEntry> list1 = new LinkedList<>();
        list1.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men1 = createMenu("Menu1", list1);
        LocalDateTime createTime = LocalDateTime.now();
        menuDAO.create(men1);

        LinkedList<MenuEntry> list2 = new LinkedList<>();
        list2.add(createMenuEntry("entry2", "desc", "cat", 20, 0.2, true));
        Menu men2 = createMenu("Menu2", list2);
        men2.setIdentity(men1.getIdentity());
        LocalDateTime updateTime = LocalDateTime.now();
        menuDAO.update(men2);



        // WHEN
        List<History<Menu>> history = menuDAO.getHistory(men2);

        // THEN
        assertEquals(2, history.size());

        // check create history
        History<Menu> historyEntry = history.get(0);
        assertEquals(Long.valueOf(1), historyEntry.getChangeNumber());
        assertEquals(men1, historyEntry.getData());
        assertEquals(user, historyEntry.getUser());
        assertTrue(Duration.between(createTime, historyEntry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(historyEntry.isDeleted());

        // check update history
        historyEntry = history.get(1);
        assertEquals(Long.valueOf(2), historyEntry.getChangeNumber());
        assertEquals(men2, historyEntry.getData());
        assertEquals(user, historyEntry.getUser());
        assertTrue(Duration.between(updateTime, historyEntry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(historyEntry.isDeleted());

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        menuDAO.delete(men2);

        // WHEN
        history = menuDAO.getHistory(men2);

        // THEN
        assertEquals(3, history.size());

        // check delete history
        historyEntry = history.get(2);
        assertEquals(Long.valueOf(3), historyEntry.getChangeNumber());
        assertEquals(men2, historyEntry.getData());
        assertEquals(user, historyEntry.getUser());
        assertTrue(Duration.between(deleteTime, historyEntry.getTimeOfChange()).getSeconds() < 1);
        assertTrue(historyEntry.isDeleted());
    }

    @Test
    public void testGetAll_shouldReturnEmptyList() throws DAOException {
        // WHEN / THEN
        assertTrue(menuDAO.getAll().isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        LinkedList<MenuEntry> list1 = new LinkedList<>();
        list1.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men1 = createMenu("Menu", list1);
        menuDAO.create(men1);

        LinkedList<MenuEntry> list2 = new LinkedList<>();
        list2.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men2 = createMenu("Menu", list2);
        menuDAO.create(men2);

        LinkedList<MenuEntry> list3 = new LinkedList<>();
        list3.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu men3 = createMenu("Menu3", list3);
        menuDAO.create(men3);

        // WHEN
        List<Menu> objects = menuDAO.getAll();

        // THEN
        assertEquals(3, objects.size());
        assertTrue(objects.contains(men1));
        assertTrue(objects.contains(men2));
        assertTrue(objects.contains(men3));
    }

    @Test
    public void testUpdate_DeleteMenuEntryAndAddSameEntryAgain() throws DAOException, ValidationException {
        //  GIVEN
        LinkedList<MenuEntry> list = new LinkedList<>();
        list.add(createMenuEntry("entry1", "desc1", "cat", 20, 0.2, true));
        list.add(createMenuEntry("entry2", "desc2", "cat", 20, 0.2, true));
        Menu men = createMenu("Menu", list);
        menuDAO.create(men);

        MenuEntry entryTwo = men.getEntries().get(1);

        // check if entry is stored
        assertEquals(men, menuDAO.find(Menu.withIdentity(men.getIdentity())).get(0));

        // WHEN
        List<MenuEntry> listNew = men.getEntries();
        listNew.remove(1);
        menuDAO.update(men);

        // THEN
        // check if entry was updated
        List<Menu> storedObjects = menuDAO.find(Menu.withIdentity(men.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(men, storedObjects.get(0));

        // WHEN
        listNew = men.getEntries();
        listNew.add(entryTwo);
        men.setEntries(listNew);
        menuDAO.update(men);

        // THEN
        // check if entry was updated
        storedObjects = menuDAO.find(Menu.withIdentity(men.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(men, storedObjects.get(0));
    }

    @Test
    public void testFind_searchForMenuByMenuEntries() throws DAOException, ValidationException {
        //GIVEN
        LinkedList<MenuEntry> list = new LinkedList<>();
        list.add(createMenuEntry("entry1", "desc1", "cat", 20, 0.2, true));
        list.add(createMenuEntry("entry2", "desc2", "cat", 20, 0.2, true));
        Menu men = createMenu("Menu", list);
        menuDAO.create(men);

        LinkedList<MenuEntry> list2 = new LinkedList<>();
        list2.add(createMenuEntry("entry3", "desc1", "cat", 20, 0.2, true));
        list2.add(createMenuEntry("entry4", "desc2", "cat", 20, 0.2, true));
        Menu men2 = createMenu("Menu2", list2);
        menuDAO.create(men2);


        //WHEN
        Menu matcher = new Menu();
        matcher.setEntries(men.getEntries());

        // THEN
        // check if entry was updated
        List<Menu> storedObjects = menuDAO.find(matcher);
        System.out.println(storedObjects);
        assertEquals(1, storedObjects.size());
        assertEquals(men, storedObjects.get(0));


        //WHEN
        matcher = new Menu();
        matcher.setEntries(men2.getEntries());

        // THEN
        // check if entry was updated
        storedObjects = menuDAO.find(matcher);
        System.out.println(storedObjects);
        assertEquals(1, storedObjects.size());
        assertEquals(men2, storedObjects.get(0));
    }

    @Test
    public void testFind_searchForMenuByMenuEntriesFurtherTests() throws DAOException, ValidationException {
        //GIVEN
        LinkedList<MenuEntry> list = new LinkedList<>();
        list.add(createMenuEntry("entry1", "desc1", "cat", 20, 0.2, true));
        list.add(createMenuEntry("entry2", "desc2", "cat", 20, 0.2, true));
        Menu men = createMenu("Menu", list);
        menuDAO.create(men);

        LinkedList<MenuEntry> list2 = new LinkedList<>();
        list2.add(createMenuEntry("entry3", "desc1", "cat", 20, 0.2, true));
        list2.add(createMenuEntry("entry4", "desc2", "cat", 20, 0.2, true));
        Menu men2 = createMenu("Menu2", list2);
        menuDAO.create(men2);

        //WHEN
        MenuEntry entry = men.getEntries().get(0);
        List<MenuEntry> newList = men2.getEntries();
        newList.add(entry);
        men2.setEntries(newList);
        menuDAO.update(men2);

        Menu matcher = new Menu();
        newList = new LinkedList<>();
        newList.add(entry);
        matcher.setEntries(newList);

        // THEN
        // check if entry was updated
        List<Menu> storedObjects = menuDAO.find(matcher);
        System.out.println(storedObjects);
        assertEquals(2, storedObjects.size());
        assertTrue(storedObjects.contains(men));
        assertTrue(storedObjects.contains(men2));
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjects() throws DAOException, ValidationException {
        // PREPARE
        // menu 1
        LinkedList<MenuEntry> list1 = new LinkedList<>();
        list1.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu menu1 = createMenu("Menu", list1);

        menuDAO.create(menu1);
        assertEquals(1, menuDAO.find(menu1).size());

        // menu 2
        LinkedList<MenuEntry> list2 = new LinkedList<>();
        list2.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu menu2 = createMenu("Menu", list2);

        menuDAO.create(menu2);
        assertEquals(1, menuDAO.find(menu2).size());

        // menu 3
        LinkedList<MenuEntry> list3 = new LinkedList<>();
        list3.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu menu3 = createMenu("Menu3", list3);

        menuDAO.create(menu3);
        assertEquals(1, menuDAO.find(menu3).size());

        // GIVEN
        Menu menuId1 = Menu.withIdentity(menu1.getIdentity());
        Menu menuId2 = Menu.withIdentity(menu2.getIdentity());
        Menu menuId3 = Menu.withIdentity(menu3.getIdentity());
        List<Menu> menuIds = Arrays.asList(menuId1, menuId2, menuId3);

        // WHEN
        List<Menu> result = menuDAO.populate(menuIds);

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(menu1));
        assertTrue(result.contains(menu2));
        assertTrue(result.contains(menu3));
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjectsOfDeletedObjects() throws DAOException, ValidationException {
        // PREPARE
        // menu 1
        LinkedList<MenuEntry> list1 = new LinkedList<>();
        list1.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu menu1 = createMenu("Menu", list1);

        menuDAO.create(menu1);
        assertEquals(1, menuDAO.find(menu1).size());
        menuDAO.delete(menu1);
        assertEquals(0, menuDAO.find(menu1).size());

        // menu 2
        LinkedList<MenuEntry> list2 = new LinkedList<>();
        list2.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu menu2 = createMenu("Menu", list2);

        menuDAO.create(menu2);
        assertEquals(1, menuDAO.find(menu2).size());
        menuDAO.delete(menu2);
        assertEquals(0, menuDAO.find(menu2).size());

        // menu 3
        LinkedList<MenuEntry> list3 = new LinkedList<>();
        list3.add(createMenuEntry("entry1", "desc", "cat", 20, 0.2, true));
        Menu menu3 = createMenu("Menu3", list3);

        menuDAO.create(menu3);
        assertEquals(1, menuDAO.find(menu3).size());
        menuDAO.delete(menu3);
        assertEquals(0, menuDAO.find(menu3).size());

        // GIVEN
        Menu menuId1 = Menu.withIdentity(menu1.getIdentity());
        Menu menuId2 = Menu.withIdentity(menu2.getIdentity());
        Menu menuId3 = Menu.withIdentity(menu3.getIdentity());
        List<Menu> menuIds = Arrays.asList(menuId1, menuId2, menuId3);

        // WHEN
        List<Menu> result = menuDAO.populate(menuIds);

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(menu1));
        assertTrue(result.contains(menu2));
        assertTrue(result.contains(menu3));
    }

    @Test
    public void testPopulate_nullListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // WHEN
        List<Menu> result = menuDAO.populate(null);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPopulate_emptyListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // WHEN
        List<Menu> result = menuDAO.populate(Arrays.asList());

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithInvalidObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<Menu> invalidMenus = Arrays.asList(new Menu());

        // WHEN
        List<Menu> result = menuDAO.populate(invalidMenus);
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithNullObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<Menu> invalidMenus = new ArrayList<>();
        invalidMenus.add(null);

        // WHEN
        List<Menu> result = menuDAO.populate(invalidMenus);
    }
}
