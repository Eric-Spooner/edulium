package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TestInvoiceService extends AbstractServiceTest {
    @Autowired
    private InvoiceService invoiceService;
    @Mock
    private DAO<Invoice> invoiceDAO;
    @Mock
    private Validator<Invoice> invoiceValidator;

    private User creator1;
    private User creator2;
    private User creator3;

    @Before
    public void setUp() throws Exception {
        // creator 1
        creator1 = new User();
        creator1.setIdentity("A");
        creator1.setName("Bob");
        creator1.setRole("ROLE1");

        // creator 2
        creator2 = new User();
        creator2.setIdentity("B");
        creator2.setName("Alice");
        creator2.setRole("ROLE2");

        // creator 3
        creator3 = new User();
        creator3.setIdentity("C");
        creator3.setName("Joe");
        creator3.setRole("ROLE3");

        // init mocks
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(getTargetObject(invoiceService), "invoiceDAO", invoiceDAO);
        ReflectionTestUtils.setField(getTargetObject(invoiceService), "invoiceValidator", invoiceValidator);
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testAddInvoice_shouldAddInvoice() throws ServiceException, ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);

        // WHEN
        invoiceService.addInvoice(invoice);

        // THEN
        Mockito.verify(invoiceDAO).create(invoice);
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testAddInvoice_shouldAddInvoiceWithoutUser() throws ServiceException, ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));

        // WHEN
        invoiceService.addInvoice(invoice);

        // THEN
        Mockito.verify(invoiceDAO).create(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testAddInvoice_shouldFailWithEmptyObject() throws ServiceException, ValidationException {
        // PREPARE
        Invoice invoice = new Invoice();
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForCreate(invoice);

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testAddInvoice_shouldFailWithNullObject() throws ServiceException, ValidationException {
        // PREPARE
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForCreate(null);

        // WHEN/THEN
        invoiceService.addInvoice(null);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testAddInvoice_shouldFailWithIncompleteObject() throws ServiceException, ValidationException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForCreate(invoice);

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testAddInvoice_shouldFailWithNegativeGrossAmount() throws ServiceException, ValidationException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("-15.6"));
        invoice.setCreator(creator1);
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForCreate(invoice);

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testAddInvoice_shouldFailWithNoTime() throws ServiceException, ValidationException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForCreate(invoice);

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testAddInvoice_shouldFailWithNullGrossAmount() throws ServiceException, ValidationException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setCreator(creator1);
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForCreate(invoice);

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "cookuser", roles = {"COOK"})
    public void testAddInvoice_shouldFailWithoutPermission() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testUpdateInvoice_shouldUpdateInvoice() throws ServiceException, ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);

        // check if invoice was created
        Mockito.verify(invoiceDAO).create(invoice);
        // after the invoice is being created, an identity must be assigned
        invoice.setIdentity(1L);

        // update object
        invoice.setGross(new BigDecimal("22"));

        // WHEN
        invoiceService.updateInvoice(invoice);

        // THEN
        // check if invoice was updated
        Mockito.verify(invoiceDAO).update(invoice);
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testUpdateInvoice_shouldUpdateInvoiceWithoutUser() throws ServiceException, ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoiceService.addInvoice(invoice);

        // check if invoice was created
        Mockito.verify(invoiceDAO).create(invoice);
        // after the invoice is being created, an identity must be assigned
        invoice.setIdentity(1L);

        // update object
        invoice.setGross(new BigDecimal("22"));

        // WHEN
        invoiceService.updateInvoice(invoice);

        // THEN
        // check if invoice was updated
        Mockito.verify(invoiceDAO).update(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testUpdateInvoice_shouldFailWithoutIdentity() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);

        // update object
        invoice.setIdentity(null);
        invoice.setGross(new BigDecimal("29"));

        // invoiceValidator should throw a ValidationException
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForUpdate(invoice);

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testUpdateInvoice_shouldFailWithoutTime() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);
        // after the invoice is being created, an identity must be assigned
        invoice.setIdentity(1L);

        // update object
        invoice.setTime(null);
        invoice.setGross(new BigDecimal("29"));

        // invoiceValidator should throw a ValidationException
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForUpdate(invoice);

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testUpdateInvoice_shouldFailWithoutGrossAmount() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);
        // after the invoice is being created, an identity must be assigned
        invoice.setIdentity(1L);

        // update object
        invoice.setGross(null);

        // invoiceValidator should throw a ValidationException
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForUpdate(invoice);

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testUpdateInvoice_shouldFailWithNegativeGrossAmount() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);
        // after the invoice is being created, an identity must be assigned
        invoice.setIdentity(1L);

        // update object
        invoice.setGross(new BigDecimal("-29"));

        // invoiceValidator should throw a ValidationException
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForUpdate(invoice);

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testUpdateInvoice_shouldFailWithEmptyObject() throws ServiceException, ValidationException {
        // PREPARE
        Invoice invoice = new Invoice();
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForUpdate(invoice);

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testUpdateInvoice_shouldFailWithNullObject() throws ServiceException, ValidationException {
        // PREPARE
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForUpdate(null);

        // WHEN/THEN
        invoiceService.updateInvoice(null);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testUpdateInvoice_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);

        Long identity = 1L;

        try {
            while (!invoiceService.findInvoices(Invoice.withIdentity(identity)).isEmpty()) {
                identity++;
            }
        } catch(ServiceException e) {
            fail();
        }

        invoice.setIdentity(identity);

        // invoiceDAO should throw exception
        Mockito.doThrow(new DAOException("")).when(invoiceDAO).update(invoice);

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "cookuser", roles = {"COOK"})
    public void testUpdateInvoice_shouldFailWithoutPermission() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testDeleteInvoice_shouldDeleteInvoice() throws ServiceException, ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);
        // after the invoice is being created, an identity must be assigned
        invoice.setIdentity(1L);

        // WHEN
        invoiceService.deleteInvoice(Invoice.withIdentity(invoice.getIdentity()));

        // THEN
        Mockito.verify(invoiceDAO).delete(invoice);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testDeleteInvoice_shouldFailWithNullObject() throws ServiceException, ValidationException {
        // PREPARE
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForDelete(null);

        // WHEN/THEN
        invoiceService.deleteInvoice(null);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testDeleteInvoice_shouldFailWithoutIdentity() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);

        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateForDelete(invoice);

        // WHEN/THEN
        invoiceService.deleteInvoice(invoice);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testDeleteInvoice_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        // after the invoice is being created, an identity must be assigned
        invoice.setIdentity(1L);

        Long identity = 1L;

        try {
            while (!invoiceService.findInvoices(Invoice.withIdentity(identity)).isEmpty()) {
                identity++;
            }
        } catch(ServiceException e) {
            fail();
        }

        invoice.setIdentity(identity);

        // invoiceDAO should throw DAOException
        Mockito.doThrow(new DAOException("")).when(invoiceDAO).delete(invoice);

        // WHEN/THEN
        invoiceService.deleteInvoice(invoice);
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testFindInvoices_shouldFindInvoicesByIdentity() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        // Invoice 1
        Invoice inv1 = new Invoice();
        inv1.setTime(LocalDateTime.now());
        inv1.setGross(new BigDecimal("51.6"));
        inv1.setCreator(creator1);

        // Invoice 2
        Invoice inv2 = new Invoice();
        inv2.setTime(LocalDateTime.now());
        inv2.setGross(new BigDecimal("30"));
        inv2.setCreator(creator2);

        // invoice 3
        Invoice inv3 = new Invoice();
        inv3.setTime(LocalDateTime.now());
        inv3.setGross(new BigDecimal("1.6"));
        inv3.setCreator(creator3);

        // store invoices
        invoiceService.addInvoice(inv1);
        // after the invoice is being created, an identity must be assigned
        inv1.setIdentity(1L);

        invoiceService.addInvoice(inv2);
        // after the invoice is being created, an identity must be assigned
        inv2.setIdentity(2L);

        invoiceService.addInvoice(inv3);
        // after the invoice is being created, an identity must be assigned
        inv3.setIdentity(3L);

        Mockito.when(invoiceDAO.find(Invoice.withIdentity(inv1.getIdentity())))
                .thenReturn(Collections.singletonList(inv1));
        Mockito.when(invoiceDAO.find(Invoice.withIdentity(inv2.getIdentity())))
                .thenReturn(Collections.singletonList(inv2));
        Mockito.when(invoiceDAO.find(Invoice.withIdentity(inv3.getIdentity())))
                .thenReturn(Collections.singletonList(inv3));

        // WHEN
        List<Invoice> invoices = invoiceService.findInvoices(Invoice.withIdentity(inv1.getIdentity()));
        // THEN
        Mockito.verify(invoiceDAO).find(Invoice.withIdentity(inv1.getIdentity()));
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv1));

        // WHEN
        invoices = invoiceService.findInvoices(Invoice.withIdentity(inv2.getIdentity()));
        // THEN
        Mockito.verify(invoiceDAO).find(Invoice.withIdentity(inv2.getIdentity()));
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv2));

        // WHEN
        invoices = invoiceService.findInvoices(Invoice.withIdentity(inv3.getIdentity()));
        // THEN
        Mockito.verify(invoiceDAO).find(Invoice.withIdentity(inv3.getIdentity()));
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv3));
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testFindInvoices_shouldFindInvoicesByCreator() throws ServiceException, ValidationException, DAOException {
        // GIVEN
        // Invoice 1
        Invoice inv1 = new Invoice();
        inv1.setTime(LocalDateTime.now());
        inv1.setGross(new BigDecimal("51.6"));
        inv1.setCreator(creator1);

        // Invoice 2
        Invoice inv2 = new Invoice();
        inv2.setTime(LocalDateTime.now());
        inv2.setGross(new BigDecimal("30"));
        inv2.setCreator(creator2);

        // invoice 3
        Invoice inv3 = new Invoice();
        inv3.setTime(LocalDateTime.now());
        inv3.setGross(new BigDecimal("1.6"));
        inv3.setCreator(creator3);

        // store invoices
        invoiceService.addInvoice(inv1);
        // after the invoice is being created, an identity must be assigned
        inv1.setIdentity(1L);

        invoiceService.addInvoice(inv2);
        // after the invoice is being created, an identity must be assigned
        inv2.setIdentity(2L);

        invoiceService.addInvoice(inv3);
        // after the invoice is being created, an identity must be assigned
        inv3.setIdentity(3L);

        // Matcher 1
        Invoice m1 = new Invoice();
        m1.setCreator(creator1);
        // Matcher 2
        Invoice m2 = new Invoice();
        m2.setCreator(creator2);
        // Matcher 3
        Invoice m3 = new Invoice();
        m3.setCreator(creator3);

        Mockito.when(invoiceDAO.find(m1)).thenReturn(Collections.singletonList(inv1));
        Mockito.when(invoiceDAO.find(m2)).thenReturn(Collections.singletonList(inv2));
        Mockito.when(invoiceDAO.find(m3)).thenReturn(Collections.singletonList(inv3));

        // WHEN
        List<Invoice> invoices = invoiceService.findInvoices(m1);
        // THEN
        Mockito.verify(invoiceDAO).find(m1);
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv1));

        // WHEN
        invoices = invoiceService.findInvoices(m2);
        // THEN
        Mockito.verify(invoiceDAO).find(m2);
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv2));

        // WHEN
        invoices = invoiceService.findInvoices(m3);
        // THEN
        Mockito.verify(invoiceDAO).find(m3);
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv3));
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testFindInvoices_shouldReturnEmptyListWhenSearchingNull() throws ServiceException, DAOException {
        // PREPARE
        Mockito.when(invoiceDAO.find(null)).thenReturn(new ArrayList<>());

        // WHEN/THEN
        List<Invoice> invoices = invoiceService.findInvoices(null);
        Mockito.verify(invoiceDAO).find(null);
        assertTrue(invoices.isEmpty());
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testFindInvoices_shouldReturnEmptyListWhenNoObjectIsStored() throws ServiceException, DAOException {
        // WHEN/THEN
        Long identity = 1L;

        while(!invoiceService.findInvoices(Invoice.withIdentity(identity)).isEmpty()) {
            identity++;
        }

        Mockito.when(invoiceDAO.find(Invoice.withIdentity(identity))).thenReturn(new ArrayList<>());

        List<Invoice> invoices = invoiceService.findInvoices(Invoice.withIdentity(identity));
        Mockito.verify(invoiceDAO).find(Invoice.withIdentity(identity));
        assertTrue(invoices.isEmpty());
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testGetAllInvoices_shouldReturnAllInvoices() throws ServiceException, ValidationException, DAOException {
        // GIVEN
        // Invoice 1
        Invoice inv1 = new Invoice();
        inv1.setTime(LocalDateTime.now());
        inv1.setGross(new BigDecimal("51.6"));
        inv1.setCreator(creator1);

        // Invoice 2
        Invoice inv2 = new Invoice();
        inv2.setTime(LocalDateTime.now());
        inv2.setGross(new BigDecimal("30"));
        inv2.setCreator(creator2);

        // invoice 3
        Invoice inv3 = new Invoice();
        inv3.setTime(LocalDateTime.now());
        inv3.setGross(new BigDecimal("1.6"));
        inv3.setCreator(creator3);

        // store invoices
        invoiceService.addInvoice(inv1);
        // after the invoice is being created, an identity must be assigned
        inv1.setIdentity(1L);

        invoiceService.addInvoice(inv2);
        // after the invoice is being created, an identity must be assigned
        inv2.setIdentity(2L);

        invoiceService.addInvoice(inv3);
        // after the invoice is being created, an identity must be assigned
        inv3.setIdentity(3L);

        Mockito.when(invoiceDAO.getAll()).thenReturn(Arrays.asList(inv1, inv2, inv3));

        // WHEN
        List<Invoice> invoices = invoiceService.getAllInvoices();

        // THEN
        Mockito.verify(invoiceDAO).getAll();
        assertEquals(3, invoices.size());
        assertTrue(invoices.contains(inv1));
        assertTrue(invoices.contains(inv2));
        assertTrue(invoices.contains(inv3));
    }

    @Test
    @WithMockUser(username = "servicetester", roles = {"SERVICE"})
    public void testGetHistory_shouldReturnHistory() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Invoice res = new Invoice();
        History<Invoice> history = new History<>();
        history.setData(res);

        Mockito.when(invoiceDAO.getHistory(res)).thenReturn(Collections.singletonList(history));

        // WHEN
        List<History<Invoice>> changes = invoiceService.getInvoiceHistory(res);

        // THEN
        assertEquals(1, changes.size());
        assertTrue(changes.contains(history));
        Mockito.verify(invoiceDAO).getHistory(res);
    }

    @Test(expected = ServiceException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetReservationHistory_onDAOExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Invoice res = new Invoice();
        Mockito.doThrow(new DAOException("")).when(invoiceDAO).getHistory(res);

        // WHEN
        invoiceService.getInvoiceHistory(res);
    }

    @Test(expected = ValidationException.class)
    @WithMockUser(username = "servicetester", roles={"MANAGER"})
    public void testGetReservationHistory_onValidationExceptionShouldThrow() throws ServiceException, ValidationException, DAOException {
        // PREPARE
        Invoice res = new Invoice();
        Mockito.doThrow(new ValidationException("")).when(invoiceValidator).validateIdentity(res);

        // WHEN
        invoiceService.getInvoiceHistory(res);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "servicetester", roles={"SERVICE"})
    public void testGetReservationHistory_WithoutPermissionShouldFail() throws ValidationException, DAOException, ServiceException {
        // PREPARE
        Invoice res = new Invoice();

        // WHEN
        invoiceService.getInvoiceHistory(res);
    }
}
