package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

/**
 * Unit Test for the MenuCategoryDAO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/Spring-DAO.xml")
@Transactional
public class TestMenuCategoryDAO {
    @Autowired
    private MenuCategoryDAO menuCategoryDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");

        // WHEN
        menuCategoryDAO.create(cat);

        // THEN
        // check if identity is set
        Assert.assertNotNull(cat.getIdentity());

        // check retrieving object
        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(cat.getIdentity());

        List<MenuCategory> storedObjects = menuCategoryDAO.find(matcher);
        Assert.assertEquals(storedObjects.size(), 1);
        Assert.assertEquals(storedObjects.get(0), cat);
    }

    @Test(expected = DAOException.class)
    public void testCreate_addingObjectWithoutNameShouldFail() throws DAOException {
        // GIVEN
        MenuCategory category = new MenuCategory();

        // WHEN
        try {
            menuCategoryDAO.create(category);
        } finally {
            Assert.assertNull(category.getIdentity());
        }
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");
        menuCategoryDAO.create(cat);

        // check if cat is stored
        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(cat.getIdentity());
        Assert.assertEquals(menuCategoryDAO.find(matcher).get(0), cat);

        // WHEN
        cat.setName("newCat");
        menuCategoryDAO.update(cat);

        // THEN
        // check if category name was updated
        List<MenuCategory> storedObjects = menuCategoryDAO.find(matcher);
        Assert.assertEquals(storedObjects.size(), 1);
        Assert.assertEquals(storedObjects.get(0), cat);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");

        // WHEN
        menuCategoryDAO.update(cat);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        Long identity = (long) 1;
        cat.setIdentity(identity);
        cat.setName("cat");

        // check if no item with the id IDENTITY exists
        try {
            while (!menuCategoryDAO.find(cat).isEmpty()) {
                identity++;
                cat.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.assertTrue(false);
        }

        // WHEN
        menuCategoryDAO.update(cat);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");
        menuCategoryDAO.create(cat);

        // WHEN
        menuCategoryDAO.delete(cat);

        // check if category was removed
        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(cat.getIdentity());

        // THEN
        Assert.assertEquals(menuCategoryDAO.find(matcher).size(), 0);
        Assert.assertEquals(menuCategoryDAO.getAll().size(), 0);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();

        // WHEN
        menuCategoryDAO.delete(cat);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        Long identity = (long) 1;
        cat.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        try {
            while (!menuCategoryDAO.find(cat).isEmpty()) {
                identity++;
                cat.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.assertTrue(false);
        }

        // WHEN
        menuCategoryDAO.delete(cat);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException {
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
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), cat1);

        // WHEN
        matcher.setIdentity(cat2.getIdentity());
        objects = menuCategoryDAO.find(matcher);
        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), cat2);

        // WHEN
        matcher.setIdentity(cat3.getIdentity());
        objects = menuCategoryDAO.find(matcher);
        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), cat3);
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException {
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
        Assert.assertEquals(objects.size(), 2);
        Assert.assertTrue(objects.contains(cat1));
        Assert.assertTrue(objects.contains(cat2));

        // WHEN
        matcher.setName(cat3.getName());
        objects = menuCategoryDAO.find(matcher);

        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), cat3);
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        Long identity = (long) 1;
        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        try {
            while (!menuCategoryDAO.find(matcher).isEmpty()) {
                identity++;
                matcher.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.assertTrue(false);
        }

        // WHEN
        List<MenuCategory> storedObjects = menuCategoryDAO.find(matcher);

        // THEN
        Assert.assertEquals(storedObjects.size(), 0);
    }

    @Test
    public void testGetAll_shouldReturnEmptyList() throws DAOException {
        // WHEN / THEN
        Assert.assertEquals(menuCategoryDAO.getAll().size(), 0);
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException {
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
        Assert.assertEquals(objects.size(), 3);
        Assert.assertTrue(objects.contains(cat1));
        Assert.assertTrue(objects.contains(cat2));
        Assert.assertTrue(objects.contains(cat3));
    }
}
