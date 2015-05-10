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

    @Test
    public void testUpdate_shouldUpdateObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(new Date());
        invoice.setGross(26.7d);
        invoiceDAO.create(invoice);

        // Make sure the invoice was stored successfully
        assertNotNull(invoice.getIdentity()); // Check if identity was set
        Invoice matcher = new Invoice();
        matcher.setIdentity(invoice.getIdentity());
        assertEquals(invoice, invoiceDAO.find(matcher).get(0));

        // WHEN
        invoice.setPaid(28.0d);
        invoiceDAO.update(invoice);

        // THEN
        List<Invoice> invoiceList = invoiceDAO.find(invoice);
        assertEquals(1, invoiceList.size());
        assertEquals(invoice, invoiceList.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_shouldFailWhenObjectIsNull() throws ValidationException {
        // GIVEN
        Invoice invoice = null;

        // WHEN
        try {
            invoiceDAO.update(invoice);
        } catch (DAOException e) {
            fail("DAOException was thrown instead");
        }
    }

    @Test (expected = ValidationException.class)
    public void testUpdate_shouldFailWhenPaidAmountIsNegative() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(new Date());
        invoice.setGross(20.0d);
        invoice.setCanceled(false);
        invoiceDAO.create(invoice);

        // Make sure invoice was stored successfully
        assertNotNull(invoice.getIdentity()); // Check if identity was set
        Invoice matcher = new Invoice();
        matcher.setIdentity(invoice.getIdentity());
        List<Invoice> invoiceList = invoiceDAO.find(matcher);
        assertEquals(1, invoiceList.size());
        assertEquals(invoice, invoiceList.get(0));

        // WHEN
        invoice.setPaid(-20.0d);
        invoiceDAO.update(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_shouldFailWhenObjectIdentityIsMissing() throws ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setPaid(13.0d);

        //WHEN
        try {
            invoiceDAO.update(invoice);
        } catch (DAOException e) {
            fail("DAOException was thrown instead");
        }
    }

    @Test
    public void testDelete_shouldDeleteObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(new Date());
        invoice.setGross(22.0d);
        invoiceDAO.create(invoice);

        // Make sure invoice was stored successfully
        assertNotNull(invoice.getIdentity()); // Check if identity was set
        Invoice matcher = new Invoice();
        matcher.setIdentity(invoice.getIdentity());
        List<Invoice> invoiceList = invoiceDAO.find(matcher);
        assertEquals(1, invoiceList.size());
        assertEquals(invoice, invoiceList.get(0));

        // WHEN
        invoiceDAO.delete(invoice);

        // THEN
        assertTrue(invoiceDAO.find(matcher).isEmpty());
    }

    @Test
    public void testDelete_shouldFailWhenIdentityIsMissing() throws ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();

        // WHEN
        try {
            invoiceDAO.delete(invoice);
        } catch (DAOException e) {
            fail("DAOException was thrown instead");
        }
    }
}
