package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
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
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(getTargetObject(menuService), "menuCategoryDAO", menuCategoryDAO);
        ReflectionTestUtils.setField(getTargetObject(menuService), "menuEntryDAO", menuEntryDAO);
        ReflectionTestUtils.setField(getTargetObject(menuService), "menuDAO", menuDAO);

        ReflectionTestUtils.setField(getTargetObject(menuService), "menuEntryValidator", menuEntryValidator);
        ReflectionTestUtils.setField(getTargetObject(menuService), "menuCategoryValidator", menuCategoryValidator);
        ReflectionTestUtils.setField(getTargetObject(menuService), "menuValidator", menuValidator);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddMenuEntry_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.doThrow(new DAOException("")).when(menuEntryDAO).create(entry);

        // WHEN
        menuService.addMenuEntry(entry);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddMenuEntry_WithoutPermissionShouldNotAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.addMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO, never()).create(entry);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddMenuEntry_shouldAddEntry() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.addMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO).create(entry);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddMenuEntry_shouldNotAddInvalidMenuEntry() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.doThrow(new ValidationException("")).when(menuEntryValidator).validateForCreate(entry);

        // WHEN
        menuService.addMenuEntry(entry);
        Mockito.verify(menuEntryDAO, never()).create(entry);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateMenuEntry_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.doThrow(new DAOException("")).when(menuEntryDAO).update(entry);

        // WHEN
        menuService.updateMenuEntry(entry);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateMenuEntry_WithoutPermissionShouldNotUpdate() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.updateMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO, never()).update(entry);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateMenuEntry_shouldUpdateEntry() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.updateMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO).update(entry);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateMenuEntry_shouldNotUpdateInvalidMenuEntry() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.doThrow(new ValidationException("")).when(menuEntryValidator).validateForUpdate(entry);

        // WHEN
        menuService.updateMenuEntry(entry);
        Mockito.verify(menuEntryDAO, never()).update(entry);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveMenuEntry_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.doThrow(new DAOException("")).when(menuEntryDAO).delete(entry);

        // WHEN
        menuService.removeMenuEntry(entry);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testRemoveMenuEntry_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.removeMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO, never()).delete(entry);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveMenuEntry_shouldRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();

        // WHEN
        menuService.removeMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO).delete(entry);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveMenuEntry_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.doThrow(new ValidationException("")).when(menuEntryValidator).validateForDelete(entry);

        // WHEN
        menuService.removeMenuEntry(entry);

        // THEN
        Mockito.verify(menuEntryDAO, never()).delete(entry);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testFindMenuEntry_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        MenuEntry entry = new MenuEntry();
        Mockito.doThrow(new DAOException("")).when(menuEntryDAO).find(entry);

        // WHEN
        menuService.findMenuEntry(entry);
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

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetAllMenuEntries_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Mockito.doThrow(new DAOException("")).when(menuEntryDAO).getAll();

        // WHEN
        menuService.getAllMenuEntries();
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

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddMenuCategory_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.doThrow(new DAOException("")).when(menuCategoryDAO).create(category);

        // WHEN
        menuService.addMenuCategory(category);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddMenuCategory_WithoutPermissionShouldNotAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.addMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO, never()).create(category);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddMenuCategory_shouldAddCategory() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.addMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO).create(category);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddMenuCategory_shouldNotAddInvalidMenuCategory() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.doThrow(new ValidationException("")).when(menuCategoryValidator).validateForCreate(category);

        // WHEN
        menuService.addMenuCategory(category);
        Mockito.verify(menuCategoryDAO, never()).create(category);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateMenuCategory_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.doThrow(new DAOException("")).when(menuCategoryDAO).update(category);

        // WHEN
        menuService.updateMenuCategory(category);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateMenuCategory_WithoutPermissionShouldNotUpdate() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.updateMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO, never()).update(category);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateMenuCategory_shouldUpdateCategory() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.updateMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO).update(category);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateMenuCategory_shouldNotUpdateInvalidMenuCategory() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.doThrow(new ValidationException("")).when(menuCategoryValidator).validateForUpdate(category);

        // WHEN
        menuService.updateMenuCategory(category);
        Mockito.verify(menuCategoryDAO, never()).update(category);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveMenuCategory_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.doThrow(new DAOException("")).when(menuCategoryDAO).delete(category);

        // WHEN
        menuService.removeMenuCategory(category);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testRemoveMenuCategory_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.removeMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO, never()).delete(category);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveMenuCategory_shouldRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();

        // WHEN
        menuService.removeMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO).delete(category);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveMenuCategory_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.doThrow(new ValidationException("")).when(menuCategoryValidator).validateForDelete(category);

        // WHEN
        menuService.removeMenuCategory(category);

        // THEN
        Mockito.verify(menuCategoryDAO, never()).delete(category);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testFindMenuCategory_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        MenuCategory category = new MenuCategory();
        Mockito.doThrow(new DAOException("")).when(menuCategoryDAO).find(category);

        // WHEN
        menuService.findMenuCategory(category);
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

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetAllMenuCategories_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Mockito.doThrow(new DAOException("")).when(menuCategoryDAO).getAll();

        // WHEN
        menuService.getAllMenuCategories();
    }

    @Test
    public void testGetAllMenuCategories_shouldReturnEntries() throws DAOException, ServiceException {
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

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddMenu_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Menu menu = new Menu();
        Mockito.doThrow(new DAOException("")).when(menuDAO).create(menu);

        // WHEN
        menuService.addMenu(menu);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddMenu_WithoutPermissionShouldNotAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();

        // WHEN
        menuService.addMenu(menu);

        // THEN
        Mockito.verify(menuDAO, never()).create(menu);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddMenu_shouldAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();

        // WHEN
        menuService.addMenu(menu);

        // THEN
        Mockito.verify(menuDAO).create(menu);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddMenu_shouldNotAddInvalidMenu() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();
        Mockito.doThrow(new ValidationException("")).when(menuValidator).validateForCreate(menu);

        // WHEN
        menuService.addMenu(menu);
        Mockito.verify(menuDAO, never()).create(menu);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateMenu_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Menu menu = new Menu();
        Mockito.doThrow(new DAOException("")).when(menuDAO).update(menu);

        // WHEN
        menuService.updateMenu(menu);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateMenu_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();

        // WHEN
        menuService.updateMenu(menu);

        // THEN
        Mockito.verify(menuDAO, never()).update(menu);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateMenu_shouldUpdate() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();

        // WHEN
        menuService.updateMenu(menu);

        // THEN
        Mockito.verify(menuDAO).update(menu);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateMenu_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();
        Mockito.doThrow(new ValidationException("")).when(menuValidator).validateForUpdate(menu);

        // WHEN
        menuService.updateMenu(menu);

        // THEN
        Mockito.verify(menuDAO, never()).update(menu);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveMenu_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Menu menu = new Menu();
        Mockito.doThrow(new DAOException("")).when(menuDAO).delete(menu);

        // WHEN
        menuService.removeMenu(menu);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testRemoveMenu_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();

        // WHEN
        menuService.removeMenu(menu);

        // THEN
        Mockito.verify(menuDAO, never()).delete(menu);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveMenu_shouldRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();

        // WHEN
        menuService.removeMenu(menu);

        // THEN
        Mockito.verify(menuDAO).delete(menu);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveMenu_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Menu menu = new Menu();
        Mockito.doThrow(new ValidationException("")).when(menuValidator).validateForDelete(menu);

        // WHEN
        menuService.removeMenu(menu);

        // THEN
        Mockito.verify(menuDAO, never()).delete(menu);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testFindMenu_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Menu menu = new Menu();
        Mockito.doThrow(new DAOException("")).when(menuDAO).find(menu);

        // WHEN
        menuService.findMenu(menu);
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

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetAllMenues_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Mockito.doThrow(new DAOException("")).when(menuDAO).getAll();

        // WHEN
        menuService.getAllMenus();
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
