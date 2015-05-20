package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.service.impl.MenuServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;

/**
 * Unit Test of the MenuService interface
 */
public class TestMenuService extends AbstractServiceTest {
    @Autowired
    private MenuService menuService;
    @Mock
    private DAO<Menu> menuDAO;
    @Mock
    private DAO<MenuCategory> menuCategoryDAO;
    @Mock
    private DAO<MenuEntry> menuEntryDAO;
    @Mock
    private Validator<MenuEntry> menuEntryValidator;
    @Mock
    private Validator<MenuCategory> menuCategoryValidator;
    @Mock
    private Validator<Menu> menuValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        MenuServiceImpl service = new MenuServiceImpl(menuEntryDAO, menuCategoryDAO, menuDAO);
        service.setValidators(menuEntryValidator, menuCategoryValidator, menuValidator);

        menuService = service;
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    @WithMockUser(roles={"ROLE_SERVICE"})
    public void testAddMenuEntry_WithoutPermissionShouldNotAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.addMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO, never()).create(entry);
    }

    @Test
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testAddMenuEntry_shouldAddEntry() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.addMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO).create(entry);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testAddMenuEntry_shouldNotAddInvalidMenuEntry() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.doThrow(new ValidationException("")).when(menuEntryValidator).validateForCreate(entry);

        // WHEN
        menuService.addMenuEntry(entry);
        Mockito.verify(menuEntryDAO, never()).create(entry);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    @WithMockUser(roles={"ROLE_SERVICE"})
    public void testUpdateMenuEntry_WithoutPermissionShouldNotUpdate() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.updateMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO, never()).update(entry);
    }

    @Test
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testUpdateMenuEntry_shouldUpdateEntry() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.updateMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO).update(entry);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testUpdateMenuEntry_shouldNotUpdateInvalidMenuEntry() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.doThrow(new ValidationException("")).when(menuEntryValidator).validateForUpdate(entry);

        // WHEN
        menuService.updateMenuEntry(entry);
        Mockito.verify(menuEntryDAO, never()).update(entry);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    @WithMockUser(roles={"ROLE_SERVICE"})
    public void testRemoveMenuEntry_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.removeMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO, never()).delete(entry);
    }

    @Test
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testRemoveMenuEntry_shouldRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.removeMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO).delete(entry);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testRemoveMenuEntry_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.doThrow(new ValidationException("")).when(menuEntryValidator).validateForDelete(entry);

        // WHEN
        menuService.removeMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO, never()).delete(entry);
    }

    @Test
    public void testFindMenuEntry_shouldReturnEntries() throws DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.when(menuEntryDAO.find(entry)).thenReturn(Arrays.asList(entry));

        // WHEN
        List<MenuEntry> entries = menuService.findMenuEntry(entry);

        // THEN
        assertEquals(1, entries.size());
        assertTrue(entries.contains(entry));
    }

    @Test
    public void testFindGetAllMenuEntries_shouldReturnEntries() throws DAOException, ServiceException {
        // PREPARE
        MenuEntry entry1 = new MenuEntry();
        MenuEntry entry2 = new MenuEntry();
        MenuEntry entry3 = new MenuEntry();
        Mockito.when(menuEntryDAO.getAll()).thenReturn(Arrays.asList(entry1, entry2, entry3));

        // WHEN
        List<MenuEntry> entries = menuService.getAllMenuEntries();

        // THEN
        assertEquals(3, entries.size());
        assertTrue(entries.contains(entry1));
        assertTrue(entries.contains(entry2));
        assertTrue(entries.contains(entry3));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    @WithMockUser(roles={"ROLE_SERVICE"})
    public void testAddMenuCategory_WithoutPermissionShouldNotAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.addMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO, never()).create(category);
    }

    @Test
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testAddMenuCategory_shouldAddCategory() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.addMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO).create(category);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testAddMenuCategory_shouldNotAddInvalidMenuCategory() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.doThrow(new ValidationException("")).when(menuCategoryValidator).validateForCreate(category);

        // WHEN
        menuService.addMenuCategory(category);
        Mockito.verify(menuCategoryDAO, never()).create(category);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    @WithMockUser(roles={"ROLE_SERVICE"})
    public void testUpdateMenuCategory_WithoutPermissionShouldNotUpdate() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.updateMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO, never()).update(category);
    }

    @Test
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testUpdateMenuCategory_shouldUpdateCategory() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.updateMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO).update(category);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testUpdateMenuCategory_shouldNotUpdateInvalidMenuCategory() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.doThrow(new ValidationException("")).when(menuCategoryValidator).validateForUpdate(category);

        // WHEN
        menuService.updateMenuCategory(category);
        Mockito.verify(menuCategoryDAO, never()).update(category);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    @WithMockUser(roles={"ROLE_SERVICE"})
    public void testRemoveMenuCategory_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.removeMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO, never()).delete(category);
    }

    @Test
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testRemoveMenuCategory_shouldRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.removeMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO).delete(category);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testRemoveMenuCategory_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.doThrow(new ValidationException("")).when(menuCategoryValidator).validateForDelete(category);

        // WHEN
        menuService.removeMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO, never()).delete(category);
    }

    @Test
    public void testFindMenuCategory_shouldReturnEntries() throws DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.when(menuCategoryDAO.find(category)).thenReturn(Arrays.asList(category));

        // WHEN
        List<MenuCategory> entries = menuService.findMenuCategory(category);

        // THEN
        assertEquals(1, entries.size());
        assertTrue(entries.contains(category));
    }

    @Test
    public void testFindGetAllMenuCategories_shouldReturnEntries() throws DAOException, ServiceException {
        // PREPARE
        MenuCategory category1 = new MenuCategory();
        MenuCategory category2 = new MenuCategory();
        MenuCategory category3 = new MenuCategory();
        Mockito.when(menuCategoryDAO.getAll()).thenReturn(Arrays.asList(category1, category2, category3));

        // WHEN
        List<MenuCategory> entries = menuService.getAllMenuCategories();

        // THEN
        assertEquals(3, entries.size());
        assertTrue(entries.contains(category1));
        assertTrue(entries.contains(category2));
        assertTrue(entries.contains(category3));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    @WithMockUser(roles={"ROLE_SERVICE"})
    public void testAddMenu_WithoutPermissionShouldNotAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();

        // WHEN
        menuService.addMenu(menu);

        // THEN
        Mockito.verify(menuDAO, never()).create(menu);
    }

    @Test
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testAddMenu_shouldAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();

        // WHEN
        menuService.addMenu(menu);

        // THEN
        Mockito.verify(menuDAO).create(menu);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testAddMenuy_shouldNotAddInvalidMenu() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();
        Mockito.doThrow(new ValidationException("")).when(menuValidator).validateForCreate(menu);

        // WHEN
        menuService.addMenu(menu);
        Mockito.verify(menuDAO, never()).create(menu);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    @WithMockUser(roles={"ROLE_SERVICE"})
    public void testRemoveMenu_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();

        // WHEN
        menuService.removeMenu(menu);

        // THEN
        Mockito.verify(menuDAO, never()).delete(menu);
    }

    @Test
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testRemoveMenu_shouldRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();

        // WHEN
        menuService.removeMenu(menu);

        // THEN
        Mockito.verify(menuDAO).delete(menu);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(roles={"ROLE_MANAGER"})
    public void testRemoveMenu_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();
        Mockito.doThrow(new ValidationException("")).when(menuValidator).validateForDelete(menu);

        // WHEN
        menuService.removeMenu(menu);

        // THEN
        Mockito.verify(menuDAO, never()).delete(menu);
    }

    @Test
    public void testFindMenu_shouldReturnEntries() throws DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();
        Mockito.when(menuDAO.find(menu)).thenReturn(Arrays.asList(menu));

        // WHEN
        List<Menu> entries = menuService.findMenu(menu);

        // THEN
        assertEquals(1, entries.size());
        assertTrue(entries.contains(menu));
    }

    @Test
    public void testFindGetAllMenus_shouldReturnEntries() throws DAOException, ServiceException {
        // PREPARE
        Menu menu1 = new Menu();
        Menu menu2 = new Menu();
        Menu menu3 = new Menu();
        Mockito.when(menuDAO.getAll()).thenReturn(Arrays.asList(menu1, menu2, menu3));

        // WHEN
        List<Menu> entries = menuService.getAllMenus();

        // THEN
        assertEquals(3, entries.size());
        assertTrue(entries.contains(menu1));
        assertTrue(entries.contains(menu2));
        assertTrue(entries.contains(menu3));
    }
}
