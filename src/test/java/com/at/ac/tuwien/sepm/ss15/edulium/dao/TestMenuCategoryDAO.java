package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Unit Test for the MenuCategoryDAO
 */
public class TestMenuCategoryDAO extends AbstractDAOTest {
    @Autowired
    private MenuCategoryDAO menuCategoryDAO;

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
        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(cat.getIdentity());

        List<MenuCategory> storedObjects = menuCategoryDAO.find(matcher);
        assertEquals(1, storedObjects.size());
        assertEquals(cat, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutNameShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory category = new MenuCategory();

        // WHEN
        try {
            menuCategoryDAO.create(category);
        } finally {
            assertNull(category.getIdentity());
        }
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithEmptyNameShouldFail() throws DAOException, ValidationException {
        // GIVEN
        MenuCategory category = new MenuCategory();
        category.setName("");

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
        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(cat.getIdentity());
        assertEquals(cat, menuCategoryDAO.find(matcher).get(0));

        // WHEN
        cat.setName("newCat");
        menuCategoryDAO.update(cat);

        // THEN
        // check if category name was updated
        List<MenuCategory> storedObjects = menuCategoryDAO.find(matcher);
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

        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(cat.getIdentity());

        // check if cat created
        List<MenuCategory> objects = menuCategoryDAO.find(matcher);
        assertEquals(1, objects.size());
        assertEquals(cat, objects.get(0));

        // WHEN
        menuCategoryDAO.delete(cat);

        // THEN
        // check if category was removed
        assertTrue(menuCategoryDAO.find(matcher).isEmpty());
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
        MenuCategory matcher = new MenuCategory();
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
        matcher.setIdentity(cat1.getIdentity());
        List<MenuCategory> objects = menuCategoryDAO.find(matcher);
        // THEN
        assertEquals(1, objects.size());
        assertEquals(cat1, objects.get(0));

        // WHEN
        matcher.setIdentity(cat2.getIdentity());
        objects = menuCategoryDAO.find(matcher);
        // THEN
        assertEquals(1, objects.size());
        assertEquals(cat2, objects.get(0));

        // WHEN
        matcher.setIdentity(cat3.getIdentity());
        objects = menuCategoryDAO.find(matcher);
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
}
