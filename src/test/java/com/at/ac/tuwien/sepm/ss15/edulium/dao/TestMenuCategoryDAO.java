package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Unit Test for the MenuCategoryDAO
 */
public class TestMenuCategoryDAO extends AbstractDAOTest {
    @Autowired
    private DAO<MenuCategory> menuCategoryDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");

        // WHEN
        menuCategoryDAO.create(cat);

        // THEN
        // check if identity is set
        assertNotNull(cat.getIdentity());

        // check retrieving object
        List<MenuCategory> storedObjects = menuCategoryDAO.find(MenuCategory.withIdentity(cat.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(cat, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingInvalidObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory category = new MenuCategory();

        // WHEN
        try {
            menuCategoryDAO.create(category);
        } finally {
            assertNull(category.getIdentity());
        }
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");
        menuCategoryDAO.create(cat);

        // check if cat is stored
        assertEquals(cat, menuCategoryDAO.find(MenuCategory.withIdentity(cat.getIdentity())).get(0));

        // WHEN
        cat.setName("newCat");
        menuCategoryDAO.update(cat);

        // THEN
        // check if category name was updated
        List<MenuCategory> storedObjects = menuCategoryDAO.find(MenuCategory.withIdentity(cat.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(cat, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");

        // WHEN
        menuCategoryDAO.update(cat);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        Long identity = (long) 1;
        cat.setIdentity(identity);
        cat.setName("cat");

        // generate identity which is not used by any persistent object
        try {
            while (!menuCategoryDAO.find(cat).isEmpty()) {
                identity++;
                cat.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            fail();
        }

        // WHEN
        menuCategoryDAO.update(cat);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");
        menuCategoryDAO.create(cat);

        // check if cat created
        List<MenuCategory> objects = menuCategoryDAO.find(MenuCategory.withIdentity(cat.getIdentity()));
        assertEquals(1, objects.size());
        assertEquals(cat, objects.get(0));

        // WHEN
        menuCategoryDAO.delete(cat);

        // THEN
        // check if category was removed
        assertTrue(menuCategoryDAO.find(MenuCategory.withIdentity(cat.getIdentity())).isEmpty());
        assertTrue(menuCategoryDAO.getAll().isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory cat = new MenuCategory();

        // WHEN
        menuCategoryDAO.delete(cat);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        Long identity = (long) 1;
        cat.setIdentity(identity);

        // generate identity which is not used by any persistent object
        try {
            while (!menuCategoryDAO.find(cat).isEmpty()) {
                identity++;
                cat.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            fail();
        }

        // WHEN
        menuCategoryDAO.delete(cat);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory cat1 = new MenuCategory();
        MenuCategory cat2 = new MenuCategory();
        MenuCategory cat3 = new MenuCategory();
        cat1.setName("cat1");
        cat2.setName("cat2");
        cat3.setName("cat3");
        menuCategoryDAO.create(cat1);
        menuCategoryDAO.create(cat2);
        menuCategoryDAO.create(cat3);

        // WHEN
        List<MenuCategory> objects = menuCategoryDAO.find(MenuCategory.withIdentity(cat1.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(cat1, objects.get(0));

        // WHEN
        objects = menuCategoryDAO.find(MenuCategory.withIdentity(cat2.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(cat2, objects.get(0));

        // WHEN
        objects = menuCategoryDAO.find(MenuCategory.withIdentity(cat3.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(cat3, objects.get(0));
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory matcher = new MenuCategory();
        MenuCategory cat1 = new MenuCategory();
        MenuCategory cat2 = new MenuCategory();
        MenuCategory cat3 = new MenuCategory();
        cat1.setName("cat");
        cat2.setName("cat");
        cat3.setName("cat2");
        menuCategoryDAO.create(cat1);
        menuCategoryDAO.create(cat2);
        menuCategoryDAO.create(cat3);

        // WHEN
        matcher.setName(cat1.getName());
        List<MenuCategory> objects = menuCategoryDAO.find(matcher);

        // THEN
        assertEquals(2, objects.size());
        assertTrue(objects.contains(cat1));
        assertTrue(objects.contains(cat2));

        // WHEN
        matcher.setName(cat3.getName());
        objects = menuCategoryDAO.find(matcher);

        // THEN
        assertEquals(1, objects.size());
        assertEquals(cat3, objects.get(0));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        Long identity = (long) 1;
        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(identity);

        // generate identity which is not used by any persistent object
        while (!menuCategoryDAO.find(matcher).isEmpty()) {
            identity++;
            matcher.setIdentity(identity);
        }

        // WHEN
        List<MenuCategory> storedObjects = menuCategoryDAO.find(matcher);

        // THEN
        assertTrue(storedObjects.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnEmptyList() throws DAOException {
        // WHEN / THEN
        assertTrue(menuCategoryDAO.getAll().isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory cat1 = new MenuCategory();
        MenuCategory cat2 = new MenuCategory();
        MenuCategory cat3 = new MenuCategory();
        cat1.setName("cat1");
        cat2.setName("cat2");
        cat3.setName("cat3");
        menuCategoryDAO.create(cat1);
        menuCategoryDAO.create(cat2);
        menuCategoryDAO.create(cat3);

        // WHEN
        List<MenuCategory> objects = menuCategoryDAO.getAll();

        // THEN
        assertEquals(3, objects.size());
        assertTrue(objects.contains(cat1));
        assertTrue(objects.contains(cat2));
        assertTrue(objects.contains(cat3));
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        menuCategoryDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory cat = new MenuCategory();

        // WHEN
        menuCategoryDAO.getHistory(cat);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        MenuCategory cat = new MenuCategory();
        cat.setIdentity(identity);

        // generate identity which is not used by any persistent object
        while (!menuCategoryDAO.find(cat).isEmpty()) {
            identity++;
            cat.setIdentity(identity);
        }

        // WHEN / THEN
        assertTrue(menuCategoryDAO.getHistory(cat).isEmpty());
    }

    @Test
    public void testFind_nullObjectShouldReturnEmptyList() throws DAOException {
        // WHEN
        List<MenuCategory> result = menuCategoryDAO.find(null);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN
        // create data
        MenuCategory cat1 = new MenuCategory();
        cat1.setName("cat");
        LocalDateTime createTime = LocalDateTime.now();
        menuCategoryDAO.create(cat1);

        // update data
        MenuCategory cat2 = MenuCategory.withIdentity(cat1.getIdentity());
        cat2.setName("update");
        LocalDateTime updateTime = LocalDateTime.now();
        menuCategoryDAO.update(cat2);

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        menuCategoryDAO.delete(cat2);

        // WHEN
        List<History<MenuCategory>> history = menuCategoryDAO.getHistory(cat1);

        // THEN
        assertEquals(3, history.size());

        // check create history
        History<MenuCategory> entry = history.get(0);
        assertEquals(Long.valueOf(1), entry.getChangeNumber());
        assertEquals(cat1, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(createTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check update history
        entry = history.get(1);
        assertEquals(Long.valueOf(2), entry.getChangeNumber());
        assertEquals(cat2, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(updateTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check delete history
        entry = history.get(2);
        assertEquals(Long.valueOf(3), entry.getChangeNumber());
        assertEquals(cat2, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(deleteTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertTrue(entry.isDeleted());
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjects() throws DAOException, ValidationException {
        // PREPARE
        // menu category 1
        MenuCategory menuCategory1 = new MenuCategory();
        menuCategory1.setName("cat1");

        menuCategoryDAO.create(menuCategory1);
        assertEquals(1, menuCategoryDAO.find(menuCategory1).size());

        // menu category 2
        MenuCategory menuCategory2 = new MenuCategory();
        menuCategory2.setName("cat2");

        menuCategoryDAO.create(menuCategory2);
        assertEquals(1, menuCategoryDAO.find(menuCategory2).size());

        // menu category 3
        MenuCategory menuCategory3 = new MenuCategory();
        menuCategory3.setName("cat3");

        menuCategoryDAO.create(menuCategory3);
        assertEquals(1, menuCategoryDAO.find(menuCategory3).size());

        // GIVEN
        MenuCategory menuCategoryId1 = MenuCategory.withIdentity(menuCategory1.getIdentity());
        MenuCategory menuCategoryId2 = MenuCategory.withIdentity(menuCategory2.getIdentity());
        MenuCategory menuCategoryId3 = MenuCategory.withIdentity(menuCategory3.getIdentity());
        List<MenuCategory> menuCategoryIds = Arrays.asList(menuCategoryId1, menuCategoryId2, menuCategoryId3);

        // WHEN
        List<MenuCategory> result = menuCategoryDAO.populate(menuCategoryIds);

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(menuCategory1));
        assertTrue(result.contains(menuCategory2));
        assertTrue(result.contains(menuCategory3));
    }

    @Test
    public void testPopulate_nullListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // WHEN
        List<MenuCategory> result = menuCategoryDAO.populate(null);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPopulate_emptyListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // WHEN
        List<MenuCategory> result = menuCategoryDAO.populate(Arrays.asList());

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithInvalidObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<MenuCategory> invalidMenuCategorys = Arrays.asList(new MenuCategory());

        // WHEN
        List<MenuCategory> result = menuCategoryDAO.populate(invalidMenuCategorys);
    }
}
