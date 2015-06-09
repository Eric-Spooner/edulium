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

    private User creator;

    @Before
    public void setUp() throws ValidationException, DAOException {
        creator = new User();
        creator.setIdentity("A");
        creator.setName("Bob");
        creator.setRole("ROLE");

        userDAO.create(creator);
    }

    @After
    public void tearDown() throws ValidationException, DAOException {
        userDAO.delete(creator);
    }

    @Test
    public void testAddInvoice_shouldAddInvoice() throws ServiceException, ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(creator);

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
        invoice.setCreator(creator);

        // WHEN/THEN
        invoiceService.addInvoice(invoice);
    }
}
