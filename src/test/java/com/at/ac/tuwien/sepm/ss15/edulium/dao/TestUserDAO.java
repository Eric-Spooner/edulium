package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import static org.junit.Assert.*;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Unit Test for the UserDAO
 */
public class TestUserDAO extends AbstractDAOTest {
    @Autowired
    private DAO<User> userDAO;

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
        List<User> storedObjects = userDAO.find(User.withIdentity(user.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(user, storedObjects.get(0));
    }

    @Test(expected = DAOException.class)
    public void testCreate_addingTwoObjectsWithSameIdentityShouldFail() throws DAOException, ValidationException {
        // PREPARE
        User user1 = new User();
        user1.setIdentity("quantal");
        user1.setName("Quantal Quetzal");
        user1.setRole("bugfixer");

        try {
            userDAO.create(user1);
        } catch (DAOException e) {
            fail("DAOException should not occur while adding a new user with a non-existing identity");
        }

        // check if user is stored
        assertEquals(1, userDAO.find(user1).size());

        // GIVEN
        User user2 = new User();
        user2.setIdentity(user1.getIdentity());
        user2.setName("Trusty Tahr");
        user2.setRole("tester");

        // WHEN
        userDAO.create(user2);
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
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = null;

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
        // check if the user has been updated;
        List<User> storedObjects = userDAO.find(updatedUser);
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

        // search for a non-existing user identity
        try {
            do {
                user.setIdentity(buildRandomString(15));
            } while (!userDAO.find(user).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing user identity");
        }

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
        user.setIdentity("gutsy");
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

        // search for a non-existing user identity
        try {
            do {
                user.setIdentity(buildRandomString(15));
            } while (!userDAO.find(user).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing user identity");
        }

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
        User matcher1 = User.withIdentity("hardy"); // for user 1
        User matcher2 = User.withIdentity("intrepid"); // for user 2
        User matcher3 = User.withIdentity("precise"); // for user 3

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

        // search for a non-existing user identity
        try {
            do {
                matcher.setIdentity(buildRandomString(15));
            } while (!userDAO.find(matcher).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing user identity");
        }

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

    /**
     * Generates a random string which consists of characters from 'a' to 'z' with the given length.
     * @param length Length of the string
     * @return A random string which consists of characters 'a' to 'z'
     */
    private String buildRandomString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append((char)(random.nextInt('z' - 'a') + 'a'));
        }
        return stringBuilder.toString();
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        userDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();

        // WHEN
        userDAO.getHistory(user);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        User user = new User();

        // search for a non-existing user identity
        try {
            do {
                user.setIdentity(buildRandomString(15));
            } while (!userDAO.find(user).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing user identity");
        }

        // WHEN / THEN
        assertTrue(userDAO.getHistory(user).isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User testUser = getCurrentUser();

        // GIVEN
        // create data
        User user1 = new User();
        user1.setIdentity("identity");
        user1.setName("user");
        user1.setRole("user_role");
        LocalDateTime createTime = LocalDateTime.now();
        userDAO.create(user1);

        // update data
        User user2 = User.withIdentity(user1.getIdentity());
        user2.setName("update");
        user2.setRole("update_role");
        LocalDateTime updateTime = LocalDateTime.now();
        userDAO.update(user2);

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        userDAO.delete(user2);

        // WHEN
        List<History<User>> history = userDAO.getHistory(user1);

        // THEN
        assertEquals(3, history.size());

        // check create history
        History<User> entry = history.get(0);
        assertEquals(Long.valueOf(1), entry.getChangeNumber());
        assertEquals(user1, entry.getData());
        assertEquals(testUser, entry.getUser());
        assertTrue(Duration.between(createTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check update history
        entry = history.get(1);
        assertEquals(Long.valueOf(2), entry.getChangeNumber());
        assertEquals(user2, entry.getData());
        assertEquals(testUser, entry.getUser());
        assertTrue(Duration.between(updateTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check delete history
        entry = history.get(2);
        assertEquals(Long.valueOf(3), entry.getChangeNumber());
        assertEquals(user2, entry.getData());
        assertEquals(testUser, entry.getUser());
        assertTrue(Duration.between(deleteTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertTrue(entry.isDeleted());
    }
}
