package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.never;

/**
 * Unit Test of the UserService interface
 */
public class TestUserService extends AbstractServiceTest {
    @Autowired
    private UserService userService;
    @Mock
    private DAO<User> userDAO;
    @Mock
    private Validator<User> userValidator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(getTargetObject(userService), "userDAO", userDAO);
        ReflectionTestUtils.setField(getTargetObject(userService), "userValidator", userValidator);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddUser_shouldAddUser() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();

        // WHEN
        userService.addUser(user);

        // THEN
        Mockito.verify(userDAO).create(user);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddUser_invalidUserShouldNotCallDaoCreate() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();
        Mockito.doThrow(new ValidationException("invalid user test")).when(userValidator).validateForCreate(user);

        // WHEN
        try {
            userService.addUser(user);

            fail("A ValidationException should have been thrown");
        } catch (ValidationException e) {
        }

        // THEN
        Mockito.verify(userDAO, never()).create(user);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"NOROLE"})
    public void testAddUser_withoutPermissionShouldNotAddUser() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();

        // WHEN
        try {
            userService.addUser(user);

            fail("An AccessDeniedException should have been thrown");
        } catch (AccessDeniedException e) {
        }

        // THEN
        Mockito.verify(userDAO, never()).create(user);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddUser_shouldRethrowDAOExceptionAsServiceException() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();
        Mockito.doThrow(new DAOException("mocked dao exception")).when(userDAO).create(user);

        // WHEN
        userService.addUser(user);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateUser_shouldUpdateUser() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();

        // WHEN
        userService.updateUser(user);

        // THEN
        Mockito.verify(userDAO).update(user);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateUser_invalidUserShouldNotCallDaoUpdate() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();
        Mockito.doThrow(new ValidationException("invalid user test")).when(userValidator).validateForUpdate(user);

        // WHEN
        try {
            userService.updateUser(user);

            fail("A ValidationException should have been thrown");
        } catch (ValidationException e) {
        }

        // THEN
        Mockito.verify(userDAO, never()).update(user);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"NOROLE"})
    public void testUpdateUser_withoutPermissionShouldNotUpdateUser() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();

        // WHEN
        try {
            userService.updateUser(user);

            fail("An AccessDeniedException should have been thrown");
        } catch (AccessDeniedException e) {
        }

        // THEN
        Mockito.verify(userDAO, never()).update(user);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateUser_shouldRethrowDAOExceptionAsServiceException() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();
        Mockito.doThrow(new DAOException("mocked dao exception")).when(userDAO).update(user);

        // WHEN
        userService.updateUser(user);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testDeleteUser_shouldDeleteUser() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();

        // WHEN
        userService.deleteUser(user);

        // THEN
        Mockito.verify(userDAO).delete(user);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testDeleteUser_invalidUserShouldNotCallDaoDelete() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();
        Mockito.doThrow(new ValidationException("invalid user test")).when(userValidator).validateForDelete(user);

        // WHEN
        try {
            userService.deleteUser(user);

            fail("A ValidationException should have been thrown");
        } catch (ValidationException e) {
        }

        // THEN
        Mockito.verify(userDAO, never()).delete(user);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"NOROLE"})
    public void testDeleteUser_withoutPermissionShouldNotDeleteUser() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();

        // WHEN
        try {
            userService.deleteUser(user);

            fail("An AccessDeniedException should have been thrown");
        } catch (AccessDeniedException e) {
        }

        // THEN
        Mockito.verify(userDAO, never()).delete(user);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testDeleteUser_shouldRethrowDAOExceptionAsServiceException() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();
        Mockito.doThrow(new DAOException("mocked dao exception")).when(userDAO).delete(user);

        // WHEN
        userService.deleteUser(user);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testFindUsers_shouldFindUsers() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();
        Mockito.when(userDAO.find(user)).thenReturn(Arrays.asList(user));

        // WHEN
        List<User> result = userService.findUsers(user);

        // THEN
        Mockito.verify(userDAO).find(user);

        assertEquals(1, result.size());
        assertTrue(result.contains(user));
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"NOROLE"})
    public void testFindUsers_shouldFindUsersWithNoRole() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();
        Mockito.when(userDAO.find(user)).thenReturn(Arrays.asList(user));

        // WHEN
        List<User> result = userService.findUsers(user);

        // THEN
        Mockito.verify(userDAO).find(user);

        assertEquals(1, result.size());
        assertTrue(result.contains(user));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testFindUsers_shouldRethrowDAOExceptionAsServiceException() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user = new User();
        Mockito.doThrow(new DAOException("mocked dao exception")).when(userDAO).find(user);

        // WHEN
        userService.findUsers(user);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetAllUsers_shouldGetAllUsers() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user1 = new User();
        User user2 = new User();
        Mockito.when(userDAO.getAll()).thenReturn(Arrays.asList(user1, user2));

        // WHEN
        List<User> result = userService.getAllUsers();

        // THEN
        Mockito.verify(userDAO).getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"NOROLE"})
    public void testGetAllUsers_shouldGetAllUsersWithNoRole() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        User user1 = new User();
        User user2 = new User();
        Mockito.when(userDAO.getAll()).thenReturn(Arrays.asList(user1, user2));

        // WHEN
        List<User> result = userService.getAllUsers();

        // THEN
        Mockito.verify(userDAO).getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetAllUsers_shouldRethrowDAOExceptionAsServiceException() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Mockito.doThrow(new DAOException("mocked dao exception")).when(userDAO).getAll();

        // WHEN
        userService.getAllUsers();
    }
}
