package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * Unit Test of the SaleService interface
 */
public class TestSaleService extends AbstractServiceTest {
    @Autowired
    private SaleService saleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private TaxRateService taxRateService;
    @Mock
    private DAO<IntermittentSale> intermittentSaleDAO;
    @Mock
    private Validator<IntermittentSale> intermittentSaleValidator;
    @Mock
    private DAO<OnetimeSale> onetimeSaleDAO;
    @Mock
    private Validator<OnetimeSale> onetimeSaleValidator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(getTargetObject(saleService), "intermittentSaleDAO", intermittentSaleDAO);
        ReflectionTestUtils.setField(getTargetObject(saleService), "intermittentSaleValidator", intermittentSaleValidator);
        ReflectionTestUtils.setField(getTargetObject(saleService), "onetimeSaleDAO", onetimeSaleDAO);
        ReflectionTestUtils.setField(getTargetObject(saleService), "onetimeSaleValidator", onetimeSaleValidator);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddIntermittentSale_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        Mockito.doThrow(new DAOException("")).when(intermittentSaleDAO).create(intermittentSale);

        // WHEN
        saleService.addIntermittentSale(intermittentSale);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddIntermittentSale_WithoutPermissionShouldNotAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();

        // WHEN
        saleService.addIntermittentSale(intermittentSale);

        // THEN
        Mockito.verify(intermittentSaleDAO, never()).create(intermittentSale);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddIntermittentSale_shouldAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();

        // WHEN
        saleService.addIntermittentSale(intermittentSale);

        // THEN
        Mockito.verify(intermittentSaleDAO).create(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddIntermittentSale_shouldNotAddInvalidIntermittentSale() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        Mockito.doThrow(new ValidationException("")).when(intermittentSaleValidator).validateForCreate(intermittentSale);

        // WHEN
        saleService.addIntermittentSale(intermittentSale);
        Mockito.verify(intermittentSaleDAO, never()).create(intermittentSale);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateIntermittentSale_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        Mockito.doThrow(new DAOException("")).when(intermittentSaleDAO).update(intermittentSale);

        // WHEN
        saleService.updateIntermittentSale(intermittentSale);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateIntermittentSale_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();

        // WHEN
        saleService.updateIntermittentSale(intermittentSale);

        // THEN
        Mockito.verify(intermittentSaleDAO, never()).update(intermittentSale);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateIntermittentSale_shouldUpdate() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();

        // WHEN
        saleService.updateIntermittentSale(intermittentSale);

        // THEN
        Mockito.verify(intermittentSaleDAO).update(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateIntermittentSale_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        Mockito.doThrow(new ValidationException("")).when(intermittentSaleValidator).validateForUpdate(intermittentSale);

        // WHEN
        saleService.updateIntermittentSale(intermittentSale);

        // THEN
        Mockito.verify(intermittentSaleDAO, never()).update(intermittentSale);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveIntermittentSale_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        Mockito.doThrow(new DAOException("")).when(intermittentSaleDAO).delete(intermittentSale);

        // WHEN
        saleService.removeIntermittentSale(intermittentSale);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testRemoveIntermittentSale_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();

        // WHEN
        saleService.removeIntermittentSale(intermittentSale);

        // THEN
        Mockito.verify(intermittentSaleDAO, never()).delete(intermittentSale);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveIntermittentSale_shouldRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();

        // WHEN
        saleService.removeIntermittentSale(intermittentSale);

        // THEN
        Mockito.verify(intermittentSaleDAO).delete(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveIntermittentSale_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        Mockito.doThrow(new ValidationException("")).when(intermittentSaleValidator).validateForDelete(intermittentSale);

        // WHEN
        saleService.removeIntermittentSale(intermittentSale);

        // THEN
        Mockito.verify(intermittentSaleDAO, never()).delete(intermittentSale);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testFindIntermittentSale_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        Mockito.doThrow(new DAOException("")).when(intermittentSaleDAO).find(intermittentSale);

        // WHEN
        saleService.findIntermittentSale(intermittentSale);
    }

    @Test
    public void testFindIntermittentSale_shouldReturnEntries() throws DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        Mockito.when(intermittentSaleDAO.find(intermittentSale)).thenReturn(Arrays.asList(intermittentSale));

        // WHEN
        List<IntermittentSale> entries = saleService.findIntermittentSale(intermittentSale);

        // THEN
        assertEquals(1, entries.size());
        assertTrue(entries.contains(intermittentSale));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetAllIntermittentSalees_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Mockito.doThrow(new DAOException("")).when(intermittentSaleDAO).getAll();

        // WHEN
        saleService.getAllIntermittentSales();
    }

    @Test
    public void testFindGetAllIntermittentSales_shouldReturnEntries() throws DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale1 = new IntermittentSale();
        IntermittentSale intermittentSale2 = new IntermittentSale();
        IntermittentSale intermittentSale3 = new IntermittentSale();
        Mockito.when(intermittentSaleDAO.getAll()).thenReturn(Arrays.asList(intermittentSale1, intermittentSale2, intermittentSale3));

        // WHEN
        List<IntermittentSale> entries = saleService.getAllIntermittentSales();

        // THEN
        assertEquals(3, entries.size());
        assertTrue(entries.contains(intermittentSale1));
        assertTrue(entries.contains(intermittentSale2));
        assertTrue(entries.contains(intermittentSale3));
    }

    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetIntermittentSaleHistory_shouldReturn() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        History<IntermittentSale> history = new History<>();
        history.setData(intermittentSale);

        Mockito.when(intermittentSaleDAO.getHistory(intermittentSale)).thenReturn(Arrays.asList(history));

        // WHEN
        List<History<IntermittentSale>> changes = saleService.getIntermittentSaleHistory(intermittentSale);

        // THEN
        assertEquals(1, changes.size());
        assertTrue(changes.contains(history));
        Mockito.verify(intermittentSaleDAO).getHistory(intermittentSale);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetIntermittentSaleHistory_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        Mockito.doThrow(new DAOException("")).when(intermittentSaleDAO).getHistory(intermittentSale);

        // WHEN
        saleService.getIntermittentSaleHistory(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetIntermittentSaleHistory_onValidationExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();
        Mockito.doThrow(new ValidationException("")).when(intermittentSaleValidator).validateIdentity(intermittentSale);

        // WHEN
        saleService.getIntermittentSaleHistory(intermittentSale);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetIntermittentSaleHistory_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        IntermittentSale intermittentSale = new IntermittentSale();

        // WHEN
        saleService.getIntermittentSaleHistory(intermittentSale);

        // THEN
        Mockito.verify(intermittentSaleDAO, never()).getHistory(intermittentSale);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddOnetimeSale_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        Mockito.doThrow(new DAOException("")).when(onetimeSaleDAO).create(onetimeSale);

        // WHEN
        saleService.addOnetimeSale(onetimeSale);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddOnetimeSale_WithoutPermissionShouldNotAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();

        // WHEN
        saleService.addOnetimeSale(onetimeSale);

        // THEN
        Mockito.verify(onetimeSaleDAO, never()).create(onetimeSale);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddOnetimeSale_shouldAdd() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();

        // WHEN
        saleService.addOnetimeSale(onetimeSale);

        // THEN
        Mockito.verify(onetimeSaleDAO).create(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddOnetimeSale_shouldNotAddInvalidOnetimeSale() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        Mockito.doThrow(new ValidationException("")).when(onetimeSaleValidator).validateForCreate(onetimeSale);

        // WHEN
        saleService.addOnetimeSale(onetimeSale);
        Mockito.verify(onetimeSaleDAO, never()).create(onetimeSale);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateOnetimeSale_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        Mockito.doThrow(new DAOException("")).when(onetimeSaleDAO).update(onetimeSale);

        // WHEN
        saleService.updateOnetimeSale(onetimeSale);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateOnetimeSale_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();

        // WHEN
        saleService.updateOnetimeSale(onetimeSale);

        // THEN
        Mockito.verify(onetimeSaleDAO, never()).update(onetimeSale);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateOnetimeSale_shouldUpdate() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();

        // WHEN
        saleService.updateOnetimeSale(onetimeSale);

        // THEN
        Mockito.verify(onetimeSaleDAO).update(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateOnetimeSale_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        Mockito.doThrow(new ValidationException("")).when(onetimeSaleValidator).validateForUpdate(onetimeSale);

        // WHEN
        saleService.updateOnetimeSale(onetimeSale);

        // THEN
        Mockito.verify(onetimeSaleDAO, never()).update(onetimeSale);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveOnetimeSale_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        Mockito.doThrow(new DAOException("")).when(onetimeSaleDAO).delete(onetimeSale);

        // WHEN
        saleService.removeOnetimeSale(onetimeSale);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testRemoveOnetimeSale_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();

        // WHEN
        saleService.removeOnetimeSale(onetimeSale);

        // THEN
        Mockito.verify(onetimeSaleDAO, never()).delete(onetimeSale);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveOnetimeSale_shouldRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();

        // WHEN
        saleService.removeOnetimeSale(onetimeSale);

        // THEN
        Mockito.verify(onetimeSaleDAO).delete(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveOnetimeSale_ObjectWithInvalidIdentityShouldNotRemove() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        Mockito.doThrow(new ValidationException("")).when(onetimeSaleValidator).validateForDelete(onetimeSale);

        // WHEN
        saleService.removeOnetimeSale(onetimeSale);

        // THEN
        Mockito.verify(onetimeSaleDAO, never()).delete(onetimeSale);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testFindOnetimeSale_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        Mockito.doThrow(new DAOException("")).when(onetimeSaleDAO).find(onetimeSale);

        // WHEN
        saleService.findOnetimeSale(onetimeSale);
    }

    @Test
    public void testFindOnetimeSale_shouldReturnEntries() throws DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        Mockito.when(onetimeSaleDAO.find(onetimeSale)).thenReturn(Arrays.asList(onetimeSale));

        // WHEN
        List<OnetimeSale> entries = saleService.findOnetimeSale(onetimeSale);

        // THEN
        assertEquals(1, entries.size());
        assertTrue(entries.contains(onetimeSale));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetAllOnetimeSalees_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Mockito.doThrow(new DAOException("")).when(onetimeSaleDAO).getAll();

        // WHEN
        saleService.getAllOnetimeSales();
    }

    @Test
    public void testFindGetAllOnetimeSales_shouldReturnEntries() throws DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale1 = new OnetimeSale();
        OnetimeSale onetimeSale2 = new OnetimeSale();
        OnetimeSale onetimeSale3 = new OnetimeSale();
        Mockito.when(onetimeSaleDAO.getAll()).thenReturn(Arrays.asList(onetimeSale1, onetimeSale2, onetimeSale3));

        // WHEN
        List<OnetimeSale> entries = saleService.getAllOnetimeSales();

        // THEN
        assertEquals(3, entries.size());
        assertTrue(entries.contains(onetimeSale1));
        assertTrue(entries.contains(onetimeSale2));
        assertTrue(entries.contains(onetimeSale3));
    }

    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetOnetimeSaleHistory_shouldReturn() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        History<OnetimeSale> history = new History<>();
        history.setData(onetimeSale);

        Mockito.when(onetimeSaleDAO.getHistory(onetimeSale)).thenReturn(Arrays.asList(history));

        // WHEN
        List<History<OnetimeSale>> changes = saleService.getOnetimeSaleHistory(onetimeSale);

        // THEN
        assertEquals(1, changes.size());
        assertTrue(changes.contains(history));
        Mockito.verify(onetimeSaleDAO).getHistory(onetimeSale);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetOnetimeSaleHistory_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        Mockito.doThrow(new DAOException("")).when(onetimeSaleDAO).getHistory(onetimeSale);

        // WHEN
        saleService.getOnetimeSaleHistory(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetOnetimeSaleHistory_onValidationExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();
        Mockito.doThrow(new ValidationException("")).when(onetimeSaleValidator).validateIdentity(onetimeSale);

        // WHEN
        saleService.getOnetimeSaleHistory(onetimeSale);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetOnetimeSaleHistory_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        OnetimeSale onetimeSale = new OnetimeSale();

        // WHEN
        saleService.getOnetimeSaleHistory(onetimeSale);

        // THEN
        Mockito.verify(onetimeSaleDAO, never()).getHistory(onetimeSale);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testCreate_shouldAddRemoveAndReaddObject() throws DAOException, ValidationException, ServiceException {
        //Create entries
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName("Cat");
        menuService.addMenuCategory(menuCategory);

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(0.5));
        taxRateService.addTaxRate(taxRate);

        MenuEntry entry = new MenuEntry();
        entry.setIdentity(new Long(232));
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        entry.setName("MenuEntry");
        entry.setDescription("Mmmmm");
        entry.setPrice(BigDecimal.valueOf(500));
        entry.setAvailable(true);
        menuService.addMenuEntry(entry);

        List<MenuEntry> entries = new ArrayList<>();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());


        //Create onetimeSale
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());
        onetimeSale.setEntries(entries);

        saleService.addOnetimeSale(onetimeSale);
        saleService.removeOnetimeSale(onetimeSale);
        saleService.addOnetimeSale(onetimeSale);

        Mockito.verify(onetimeSaleDAO).delete(onetimeSale);
        Mockito.verify(onetimeSaleDAO, times(2)).create(onetimeSale);
    }
}