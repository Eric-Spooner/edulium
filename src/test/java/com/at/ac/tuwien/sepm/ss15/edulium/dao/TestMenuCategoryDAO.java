package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;

/**
 * Unit Test for the MenuCategoryDAO
 */
public class TestMenuCategoryDAO {

    MenuCategoryDAO menuCategoryDAO;    //TODO spring wiring

    @Test
    void testCreate_shouldAddObject() throws DAOException {
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
        Assert.assertEquals(menuCategoryDAO.find(matcher), cat);
    }

    @Test(expected = DAOException.class)
    void testCreate_addingObjectWithoutNameShouldFail() throws DAOException {
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
    void testUpdate_shouldUpdateObject() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");

        menuCategoryDAO.create(cat);

        // WHEN
        cat.setName("newCat");
        menuCategoryDAO.update(cat);

        // THEN
        // check if category name was updated
        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(cat.getIdentity());
        Assert.assertEquals(menuCategoryDAO.find(matcher).get(0).getName(), "newCat");
    }

    @Test(expected = DAOException.class)
    void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");

        // WHEN
        cat.setName("newCat");
        menuCategoryDAO.update(cat);
    }

    @Test(expected = DAOException.class)
    void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");
        cat.setIdentity(Long.valueOf(99999));

        // WHEN
        cat.setName("newCat");
        menuCategoryDAO.update(cat);
    }

    @Test
    void testDelete_shouldDeleteObject() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setName("cat");
        menuCategoryDAO.create(cat);

        // WHEN
        menuCategoryDAO.delete(cat);

        // check if category was removed
        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(cat.getIdentity());

        Assert.assertEquals(menuCategoryDAO.find(matcher).size(), 0);
        Assert.assertEquals(menuCategoryDAO.getAll().size(), 0);
    }

    @Test(expected = DAOException.class)
    void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();

        // WHEN
        menuCategoryDAO.delete(cat);
    }

    @Test(expected = DAOException.class)
    void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException {
        // GIVEN
        MenuCategory cat = new MenuCategory();
        cat.setIdentity(Long.valueOf(99999));

        // WHEN
        menuCategoryDAO.delete(cat);
    }

    @Test
    void testFind_byIdentityShouldReturnObject() throws DAOException {
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
        MenuCategory matcher = new MenuCategory();

        matcher.setIdentity(cat1.getIdentity());
        Assert.assertEquals(menuCategoryDAO.find(matcher).get(0), cat1);

        matcher.setIdentity(cat2.getIdentity());
        Assert.assertEquals(menuCategoryDAO.find(matcher).get(0), cat2);

        matcher.setIdentity(cat3.getIdentity());
        Assert.assertEquals(menuCategoryDAO.find(matcher).get(0), cat3);
    }

    @Test
    void testFind_byNameShouldReturnObjects() throws DAOException {
        // GIVEN
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
        MenuCategory matcher = new MenuCategory();
        matcher.setName(cat1.getName());
        List<MenuCategory> objects = menuCategoryDAO.find(matcher);

        // THEN
        Assert.assertEquals(objects.size(), 2);
        Assert.assertTrue(objects.contains(cat1));
        Assert.assertTrue(objects.contains(cat2));

        // WHEN
        matcher.setName(cat3.getName());
        Assert.assertEquals(menuCategoryDAO.find(matcher).get(0), cat3);
    }

    @Test
    void testFind_shouldReturnEmptyList() throws DAOException {
        //WHEN
        MenuCategory matcher = new MenuCategory();
        matcher.setIdentity(Long.valueOf(1));
        Assert.assertEquals(menuCategoryDAO.find(matcher).size(), 0);
    }

    @Test
    void testGetAll_shouldReturnEmptyList() throws DAOException {
        //WHEN
        Assert.assertEquals(menuCategoryDAO.getAll().size(), 0);
    }

    @Test
    void testGetAll_shouldReturnObjects() throws DAOException {
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
