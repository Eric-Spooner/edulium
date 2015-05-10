package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Unit test for InvoiceDAO
 */
public class TestInvoiceDAO extends AbstractDAOTest {
    @Autowired
    private InvoiceDAO invoiceDAO;

    @Test
    public void testCreate_shouldAddObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(new Date());
        invoice.setGross(15.6d);
        invoice.setCanceled(false);

        // WHEN
        invoiceDAO.create(invoice);

        // THEN
        // check if identity was set
        assertNotNull(invoice.getIdentity());

        Invoice matcher = new Invoice();
        matcher.setIdentity(invoice.getIdentity());

        List<Invoice> invoiceList = invoiceDAO.find(matcher);
        assertEquals(1, invoiceList.size());
        assertEquals(invoice, invoiceList.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_shouldFailWithNoGrossAmount() throws ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(new Date());

        // WHEN
        try {
            invoiceDAO.create(invoice);
        } catch (DAOException e) {
            fail("DAOException was thrown instead");
        } finally {
            assertNull(invoice.getIdentity());
        }
    }

    @Test(expected = ValidationException.class)
    public void testCreate_shouldFailWithNegativeGrossAmount() throws ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(new Date());
        invoice.setGross(-19.0d);

        // WHEN
        try {
            invoiceDAO.create(invoice);
        } catch (DAOException e) {
            fail("DAOException was thrown instead");
        } finally {
            assertNull(invoice.getIdentity());
        }
    }

    @Test(expected = ValidationException.class)
    public void testCreate_shouldFailIfInvoiceWasCancelled() throws ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(new Date());
        invoice.setGross(13.0d);
        invoice.setCanceled(true);

        // WHEN
        try {
            invoiceDAO.create(invoice);
        } catch (DAOException e) {
            fail("DAOException was thrown instead");
        } finally {
            assertNull(invoice.getIdentity());
        }
    }
}
