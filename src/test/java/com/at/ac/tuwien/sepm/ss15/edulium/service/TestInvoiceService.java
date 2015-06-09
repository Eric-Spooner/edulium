package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestInvoiceService extends AbstractServiceTest {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private DAO<User> userDAO;

    private User creator1;
    private User creator2;
    private User creator3;

    @Before
    public void setUp() throws ValidationException, DAOException {
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

        userDAO.create(creator1);
        userDAO.create(creator2);
        userDAO.create(creator3);
    }

    @After
    public void tearDown() throws ValidationException, DAOException {
        userDAO.delete(creator1);
        userDAO.delete(creator2);
        userDAO.delete(creator3);
    }

    @Test
    public void testAddInvoice_shouldAddInvoice() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);

        // WHEN
        invoiceService.addInvoice(invoice);

        // THEN
        // check if identity was set
        assertNotNull(invoice.getIdentity());

        // check retrieving object
        List<Invoice> invoices = invoiceService.findInvoices(Invoice.withIdentity(invoice.getIdentity()));
        assertEquals(1, invoices.size());
        assertEquals(invoice, invoices.get(0));
    }

    @Test
    public void testAddInvoice_shouldAddInvoiceWithoutUser() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));

        // WHEN
        invoiceService.addInvoice(invoice);

        // THEN
        // check if identity was set
        assertNotNull(invoice.getIdentity());

        // check retrieving object
        List<Invoice> invoices = invoiceService.findInvoices(Invoice.withIdentity(invoice.getIdentity()));
        assertEquals(1, invoices.size());
        assertEquals(invoice, invoices.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testAddInvoice_shouldFailWithEmptyObject() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testAddInvoice_shouldFailWithNullObject() throws ServiceException, ValidationException {
        // WHEN/THEN
        invoiceService.addInvoice(null);
    }

    @Test(expected = ValidationException.class)
    public void testAddInvoice_shouldFailWithIncompleteObject() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testAddInvoice_shouldFailWithNegativeGrossAmount() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("-15.6"));
        invoice.setCreator(creator1);

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testAddInvoice_shouldFailWithNoTime() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setGross(new BigDecimal("-15.6"));
        invoice.setCreator(creator1);

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testAddInvoice_shouldFailWithNullGrossAmount() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setCreator(creator1);

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }

    @Test
    public void testUpdateInvoice_shouldUpdateInvoice() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);

        // update object
        invoice.setGross(new BigDecimal("22"));

        // WHEN
        invoiceService.updateInvoice(invoice);

        // THEN
        // check if entry was updated
        List<Invoice> invoices = invoiceService.findInvoices(Invoice.withIdentity(invoice.getIdentity()));
        assertEquals(1, invoices.size());
        assertEquals(invoice, invoices.get(0));
    }

    @Test
    public void testUpdateInvoice_shouldUpdateInvoiceWithoutUser() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoiceService.addInvoice(invoice);

        // update object
        invoice.setGross(new BigDecimal("22"));

        // WHEN
        invoiceService.updateInvoice(invoice);

        // THEN
        // check if entry was updated
        List<Invoice> invoices = invoiceService.findInvoices(Invoice.withIdentity(invoice.getIdentity()));
        assertEquals(1, invoices.size());
        assertEquals(invoice, invoices.get(0));
    }

    @Test(expected = ValidationException.class)
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

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testUpdateInvoice_shouldFailWithoutTime() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);

        // update object
        invoice.setTime(null);
        invoice.setGross(new BigDecimal("29"));

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testUpdateInvoice_shouldFailWithoutGrossAmount() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);

        // update object
        invoice.setGross(null);

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testUpdateInvoice_shouldFailWithNegativeGrossAmount() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);

        // update object
        invoice.setGross(new BigDecimal("-29"));

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testUpdateInvoice_shouldFailWithEmptyObject() throws ServiceException, ValidationException {
        // WHEN/THEN
        invoiceService.updateInvoice(new Invoice());
    }

    @Test(expected = ValidationException.class)
    public void testUpdateInvoice_shouldFailWithNullObject() throws ServiceException, ValidationException {
        // WHEN/THEN
        invoiceService.updateInvoice(null);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateInvoice_updatingNotPersistentObjectShouldFail() throws ServiceException, ValidationException {
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

        // WHEN/THEN
        invoiceService.updateInvoice(invoice);
    }

    @Test
    public void testDeleteInvoice_shouldDeleteInvoice() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);
        invoiceService.addInvoice(invoice);

        // WHEN
        invoiceService.deleteInvoice(Invoice.withIdentity(invoice.getIdentity()));

        // THEN
        List<Invoice> invoices = invoiceService.findInvoices(Invoice.withIdentity(invoice.getIdentity()));
        assertEquals(0, invoices.size());
    }

    @Test(expected = ValidationException.class)
    public void testDeleteInvoice_shouldFailWithNullObject() throws ServiceException, ValidationException {
        // WHEN/THEN
        invoiceService.deleteInvoice(null);
    }

    @Test(expected = ValidationException.class)
    public void testDeleteInvoice_shouldFailWithoutIdentity() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator1);

        // WHEN/THEN
        invoiceService.deleteInvoice(invoice);
    }

    @Test(expected = ServiceException.class)
    public void testDeleteInvoice_deletingNotPersistentObjectShouldFail() throws ServiceException, ValidationException {
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

        // WHEN/THEN
        invoiceService.deleteInvoice(invoice);
    }

    @Test
    public void testFindInvoices_shouldFindInvoicesByIdentity() throws ServiceException, ValidationException {
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
        invoiceService.addInvoice(inv2);
        invoiceService.addInvoice(inv3);

        // WHEN
        List<Invoice> invoices = invoiceService.findInvoices(Invoice.withIdentity(inv1.getIdentity()));
        // THEN
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv1));

        // WHEN
        invoices = invoiceService.findInvoices(Invoice.withIdentity(inv2.getIdentity()));
        // THEN
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv2));

        // WHEN
        invoices = invoiceService.findInvoices(Invoice.withIdentity(inv3.getIdentity()));
        // THEN
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv3));
    }

    @Test
    public void testFindInvoices_shouldFindInvoicesByCreator() throws ServiceException, ValidationException {
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
        invoiceService.addInvoice(inv2);
        invoiceService.addInvoice(inv3);

        // WHEN
        Invoice matcher = new Invoice();
        matcher.setCreator(User.withIdentity(creator1.getIdentity()));
        List<Invoice> invoices = invoiceService.findInvoices(matcher);
        // THEN
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv1));

        // WHEN
        matcher.setCreator(User.withIdentity(creator2.getIdentity()));
        invoices = invoiceService.findInvoices(matcher);
        // THEN
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv2));

        // WHEN
        matcher.setCreator(User.withIdentity(creator3.getIdentity()));
        invoices = invoiceService.findInvoices(matcher);
        // THEN
        assertEquals(1, invoices.size());
        assertTrue(invoices.contains(inv3));
    }

    @Test
    public void testFindInvoices_shouldReturnEmptyListWhenSearchingNull() throws ServiceException {
        // WHEN/THEN
        List<Invoice> invoices = invoiceService.findInvoices(null);
        assertTrue(invoices.isEmpty());
    }

    @Test
    public void testFindInvoices_shouldReturnEmptyListWhenNoObjectIsStored() throws ServiceException {
        // WHEN/THEN
        Long identity = 1L;

        while(!invoiceService.findInvoices(Invoice.withIdentity(identity)).isEmpty()) {
            identity++;
        }

        List<Invoice> invoices = invoiceService.findInvoices(Invoice.withIdentity(identity));
        assertTrue(invoices.isEmpty());
    }
}
