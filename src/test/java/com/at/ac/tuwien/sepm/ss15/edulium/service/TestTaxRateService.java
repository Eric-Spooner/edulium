package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;

/**
 * Unit Test for the taxRateService
 */
public class TestTaxRateService extends AbstractServiceTest {
    @Autowired
    TaxRateService taxRateService;
    @Mock
    DAO<TaxRate> taxRateDAO;
    @Mock
    Validator<TaxRate> taxRateValidator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(getTargetObject(taxRateService), "taxRateDAO", taxRateDAO);
        ReflectionTestUtils.setField(getTargetObject(taxRateService), "taxRateValidator", taxRateValidator);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddTaxRate_shouldAdd() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateService.addTaxRate(taxRate);

        // THEN
        Mockito.verify(taxRateDAO).create(taxRate);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddTaxRate_withInvalidObjectShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        Mockito.doThrow(new ValidationException("")).when(taxRateValidator).validateForCreate(taxRate);

        // WHEN
        taxRateService.addTaxRate(taxRate);

        // THEN
        Mockito.verify(taxRateDAO, never()).create(taxRate);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testAddTaxRate_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        Mockito.doThrow(new DAOException("")).when(taxRateDAO).create(taxRate);

        // WHEN
        taxRateService.addTaxRate(taxRate);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testAddTaxRate_withoutPermissionShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateService.addTaxRate(taxRate);

        // THEN
        Mockito.verify(taxRateDAO, never()).create(taxRate);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateTaxRate_shouldUpdate() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateService.updateTaxRate(taxRate);

        // THEN
        Mockito.verify(taxRateDAO).update(taxRate);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateTaxRate_withInvalidObjectShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        Mockito.doThrow(new ValidationException("")).when(taxRateValidator).validateForUpdate(taxRate);

        // WHEN
        taxRateService.updateTaxRate(taxRate);

        // THEN
        Mockito.verify(taxRateDAO, never()).update(taxRate);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testUpdateTaxRate_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        Mockito.doThrow(new DAOException("")).when(taxRateDAO).update(taxRate);

        // WHEN
        taxRateService.updateTaxRate(taxRate);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testUpdateTaxRate_withoutPermissionShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateService.updateTaxRate(taxRate);

        // THEN
        Mockito.verify(taxRateDAO, never()).update(taxRate);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveTaxRate_shouldRemove() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateService.removeTaxRate(taxRate);

        // THEN
        Mockito.verify(taxRateDAO).delete(taxRate);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveTaxRate_withInvalidObjectShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        Mockito.doThrow(new ValidationException("")).when(taxRateValidator).validateForDelete(taxRate);

        // WHEN
        taxRateService.removeTaxRate(taxRate);

        // THEN
        Mockito.verify(taxRateDAO, never()).delete(taxRate);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testRemoveTaxRate_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        Mockito.doThrow(new DAOException("")).when(taxRateDAO).delete(taxRate);

        // WHEN
        taxRateService.removeTaxRate(taxRate);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testRemoveTaxRate_withoutPermissionShouldFail() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateService.removeTaxRate(taxRate);

        // THEN
        Mockito.verify(taxRateDAO, never()).delete(taxRate);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testFindTaxRate_shouldReturnObjects() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        Mockito.when(taxRateDAO.find(taxRate)).thenReturn(Arrays.asList(taxRate));

        // WHEN
        List<TaxRate> taxRates = taxRateService.findTaxRate(taxRate);

        // THEN
        assertEquals(1, taxRates.size());
        assertTrue(taxRates.contains(taxRate));

    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testFindTaxRate_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        Mockito.doThrow(new DAOException("")).when(taxRateDAO).find(taxRate);

        // WHEN
        taxRateService.findTaxRate(taxRate);
    }

    @Test
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetAllTaxRates_shouldReturnObjects() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate1 = new TaxRate();
        TaxRate taxRate2 = new TaxRate();
        TaxRate taxRate3 = new TaxRate();
        Mockito.when(taxRateDAO.getAll()).thenReturn(Arrays.asList(taxRate1, taxRate2, taxRate3));

        // WHEN
        List<TaxRate> taxRates = taxRateService.getAllTaxRates();

        // THEN
        assertEquals(3, taxRates.size());
        assertTrue(taxRates.contains(taxRate1));
        assertTrue(taxRates.contains(taxRate2));
        assertTrue(taxRates.contains(taxRate3));
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetAllTaxRates_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Mockito.doThrow(new DAOException("")).when(taxRateDAO).getAll();

        // WHEN
        taxRateService.getAllTaxRates();
    }

    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetTaxRateHistory_shouldReturn() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        History<TaxRate> history = new History<>();
        history.setData(taxRate);

        Mockito.when(taxRateDAO.getHistory(taxRate)).thenReturn(Arrays.asList(history));

        // WHEN
        List<History<TaxRate>> changes = taxRateService.getTaxRateHistory(taxRate);

        // THEN
        assertEquals(1, changes.size());
        assertTrue(changes.contains(history));
        Mockito.verify(taxRateDAO).getHistory(taxRate);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetTaxRateHistory_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        Mockito.doThrow(new DAOException("")).when(taxRateDAO).getHistory(taxRate);

        // WHEN
        taxRateService.getTaxRateHistory(taxRate);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetTaxRateHistory_onValidationExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        Mockito.doThrow(new ValidationException("")).when(taxRateValidator).validateIdentity(taxRate);

        // WHEN
        taxRateService.getTaxRateHistory(taxRate);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetTaxRateHistory_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateService.getTaxRateHistory(taxRate);

        // THEN
        Mockito.verify(taxRateDAO, never()).getHistory(taxRate);
    }
}
