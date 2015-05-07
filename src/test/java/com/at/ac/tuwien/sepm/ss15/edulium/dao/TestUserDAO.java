package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Unit Test for the UserDAO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/Spring-DAO.xml")
@Transactional
public class TestUserDAO {
    @Autowired
    private UserDAO userDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("warty");
        user.setName("Warty Warthog");
        user.setRole("manager");

        // WHEN
        userDAO.create(user);

        // THEN
        // try to find the user and compare it
        User matcher = new User();
        matcher.setIdentity(user.getIdentity());

        List<User> storedObjects = userDAO.find(matcher);
        assertEquals(1, storedObjects.size());
        assertEquals(user, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setName("Jaunty Jackalope");
        user.setRole("tester");

        // WHEN
        userDAO.create(user);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutNameShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("jaunty");
        user.setRole("tester");

        // WHEN
        userDAO.create(user);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutRoleShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("jaunty");
        user.setName("Jaunty Jackalope");

        // WHEN
        userDAO.create(user);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingEmptyObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();

        // WHEN
        userDAO.create(user);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = null;

        // WHEN
        userDAO.create(user);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithEmptyIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("");
        user.setName("Breezy Badger");
        user.setRole("tester");

        // WHEN
        userDAO.create(user);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithEmptyNameShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("breezy");
        user.setName("");
        user.setRole("tester");

        // WHEN
        userDAO.create(user);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithEmptyRoleShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("breezy");
        user.setName("Breezy Badger");
        user.setRole("");

        // WHEN
        userDAO.create(user);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // PREPARE
        User user = new User();
        user.setIdentity("maverick");
        user.setName("Maverick Meerkat");
        user.setRole("manager");

        // check if user is stored
        userDAO.create(user);
        assertEquals(1, userDAO.find(user).size());

        // GIVEN
        User updatedUser = new User();
        updatedUser.setIdentity(user.getIdentity());
        updatedUser.setName("Dapper Drake");
        updatedUser.setRole("waiter");

        // WHEN
        userDAO.update(updatedUser);

        // THEN
        // check if the user has been updated
        List<User> storedObjects = userDAO.find(user);
        assertEquals(1, storedObjects.size());
        assertEquals(updatedUser, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setName("Oneiric Ocelot");
        user.setRole("thrower");

        // WHEN
        userDAO.update(user);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutNameShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("oneiric");
        user.setRole("thrower");

        // WHEN
        userDAO.update(user);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutRoleShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("oneiric");
        user.setName("Oneiric Ocelot");

        // WHEN
        userDAO.update(user);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingEmptyObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();

        // WHEN
        userDAO.update(user);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = null;

        // WHEN
        userDAO.update(user);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("zzzzzzzzzzzzzz");
        assertTrue(userDAO.find(user).isEmpty());

        user.setName("Feisty Fawn");
        user.setRole("developer");

        // WHEN
        userDAO.update(user);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // PREPARE
        final int numberOfUsersBefore = userDAO.getAll().size();

        User user = new User();
        user.setName("Gutsy Gibbon");
        user.setRole("garbageman");

        // check if user is stored
        userDAO.create(user);
        assertEquals(1, userDAO.find(user).size());
        assertEquals(numberOfUsersBefore + 1, userDAO.getAll().size());

        // GIVEN
        User userForDelete = new User();
        userForDelete.setIdentity(user.getIdentity());

        // WHEN
        userDAO.delete(userForDelete);

        // THEN
        // check if user was removed
        assertEquals(0, userDAO.find(user).size());
        assertEquals(numberOfUsersBefore, userDAO.getAll().size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();

        // WHEN
        userDAO.delete(user);
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = null;

        // WHEN
        userDAO.delete(user);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("zzzzzzzzzzzzzz");
        assertTrue(userDAO.find(user).isEmpty());

        // WHEN
        userDAO.delete(user);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // PREPARE
        // user 1
        User user1 = new User();
        user1.setIdentity("hardy");
        user1.setName("Hardy Heron");
        user1.setRole("manager");

        userDAO.create(user1);
        assertEquals(1, userDAO.find(user1).size());

        // user 2
        User user2 = new User();
        user2.setIdentity("intrepid");
        user2.setName("Intrepid Ibex");
        user2.setRole("manager");

        userDAO.create(user2);
        assertEquals(1, userDAO.find(user2).size());

        // user 3
        User user3 = new User();
        user3.setIdentity("precise");
        user3.setName("Intrepid Ibex");
        user3.setRole("developer");

        userDAO.create(user3);
        assertEquals(1, userDAO.find(user3).size());

        // GIVEN
        User matcher1 = new User(); // for user 1
        matcher1.setIdentity("hardy");

        User matcher2 = new User(); // for user 2
        matcher2.setIdentity("intrepid");

        User matcher3 = new User(); // for user 3
        matcher3.setIdentity("precise");

        // WHEN
        List<User> result1 = userDAO.find(matcher1);
        List<User> result2 = userDAO.find(matcher2);
        List<User> result3 = userDAO.find(matcher3);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(user1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(user2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(user3));
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // user 1
        User user1 = new User();
        user1.setIdentity("hardy");
        user1.setName("Hardy Heron");
        user1.setRole("manager");

        userDAO.create(user1);
        assertEquals(1, userDAO.find(user1).size());

        // user 2
        User user2 = new User();
        user2.setIdentity("intrepid");
        user2.setName("Intrepid Ibex");
        user2.setRole("manager");

        userDAO.create(user2);
        assertEquals(1, userDAO.find(user2).size());

        // user 3
        User user3 = new User();
        user3.setIdentity("precise");
        user3.setName("Intrepid Ibex");
        user3.setRole("developer");

        userDAO.create(user3);
        assertEquals(1, userDAO.find(user3).size());

        // GIVEN
        User matcher1 = new User(); // for user 1
        matcher1.setName("Hardy Heron");

        User matcher2 = new User(); // for user 2 and user 3
        matcher2.setName("Intrepid Ibex");

        // WHEN
        List<User> result1 = userDAO.find(matcher1);
        List<User> result2 = userDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(user1));

        assertEquals(2, result2.size());
        assertTrue(result2.contains(user2));
        assertTrue(result2.contains(user3));
    }

    @Test
    public void testFind_byRoleShouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // user 1
        User user1 = new User();
        user1.setIdentity("hardy");
        user1.setName("Hardy Heron");
        user1.setRole("manager");

        userDAO.create(user1);
        assertEquals(1, userDAO.find(user1).size());

        // user 2
        User user2 = new User();
        user2.setIdentity("intrepid");
        user2.setName("Intrepid Ibex");
        user2.setRole("manager");

        userDAO.create(user2);
        assertEquals(1, userDAO.find(user2).size());

        // user 3
        User user3 = new User();
        user3.setIdentity("precise");
        user3.setName("Intrepid Ibex");
        user3.setRole("developer");

        userDAO.create(user3);
        assertEquals(1, userDAO.find(user3).size());

        // GIVEN
        User matcher1 = new User(); // for user 1 and user 2
        matcher1.setRole("manager");

        User matcher2 = new User(); // for user 3
        matcher2.setRole("developer");

        // WHEN
        List<User> result1 = userDAO.find(matcher1);
        List<User> result2 = userDAO.find(matcher2);

        // THEN
        assertEquals(2, result1.size());
        assertTrue(result1.contains(user1));
        assertTrue(result1.contains(user2));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(user3));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        User matcher = new User();
        matcher.setIdentity("zzzzzzzzzzzzzz");
        assertTrue(userDAO.find(matcher).isEmpty());

        // WHEN
        List<User> result = userDAO.find(matcher);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        final int numberOfUsersBefore = userDAO.getAll().size();

        // user 1
        User user1 = new User();
        user1.setIdentity("hardy");
        user1.setName("Hardy Heron");
        user1.setRole("manager");

        userDAO.create(user1);
        assertEquals(1, userDAO.find(user1).size());

        // user 2
        User user2 = new User();
        user2.setIdentity("intrepid");
        user2.setName("Intrepid Ibex");
        user2.setRole("manager");

        userDAO.create(user2);
        assertEquals(1, userDAO.find(user2).size());

        // user 3
        User user3 = new User();
        user3.setIdentity("precise");
        user3.setName("Intrepid Ibex");
        user3.setRole("developer");

        userDAO.create(user3);
        assertEquals(1, userDAO.find(user3).size());

        // WHEN
        List<User> result = userDAO.getAll();

        // THEN
        assertEquals(numberOfUsersBefore + 3, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        assertTrue(result.contains(user3));
    }
}
