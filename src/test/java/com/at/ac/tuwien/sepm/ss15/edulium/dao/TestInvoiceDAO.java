package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Unit test for InvoiceDAO
 */
public class TestInvoiceDAO extends AbstractDAOTest {
    @Autowired
    private DAO<Invoice> invoiceDAO;

    @Test
    public void testCreate_shouldAddObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));

        // WHEN
        invoiceDAO.create(invoice);

        // THEN
        // check if identity was set
        Long identity = invoice.getIdentity();
        assertNotNull(identity);

        List<Invoice> invoiceList = invoiceDAO.find(Invoice.withIdentity(identity));
        assertEquals(1, invoiceList.size());
        assertEquals(invoice, invoiceList.get(0));
    }

    // TODO: Create new test class for InvoiceValidator and move it there
    @Test(expected = ValidationException.class)
    public void testCreate_shouldFailWithNoGrossAmount() throws ValidationException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());

        // WHEN
        try {
            invoiceDAO.create(invoice);
        } catch (DAOException e) {
            fail("DAOException was thrown instead");
        } finally {
            assertNull(invoice.getIdentity());
        }
    }

    // TODO: Create new test class for InvoiceValidator and move it there
    @Test(expected = ValidationException.class)
    public void testCreate_shouldFailWithNegativeGrossAmount() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("-19.0"));

        // WHEN
        try {
            invoiceDAO.create(invoice);
        } finally {
            assertNull(invoice.getIdentity());
        }
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("26.7"));
        invoiceDAO.create(invoice);

        // WHEN
        invoice.setPaid(new BigDecimal("28.0"));
        invoiceDAO.update(invoice);

        // THEN
        List<Invoice> invoiceList = invoiceDAO.find(invoice);
        assertEquals(1, invoiceList.size());
        assertEquals(invoice, invoiceList.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_shouldFailWhenObjectIsNull() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = null;

        // WHEN
        invoiceDAO.update(invoice);
    }

    @Test (expected = ValidationException.class)
    public void testUpdate_shouldFailWhenPaidAmountIsNegative() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("20.0"));
        invoiceDAO.create(invoice);

        // WHEN
        invoice.setPaid(new BigDecimal("-20.0"));
        invoiceDAO.update(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_shouldFailWhenObjectIdentityIsMissing() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setPaid(new BigDecimal("13.0"));

        //WHEN
        invoiceDAO.update(invoice);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("22.0"));
        invoiceDAO.create(invoice);

        // WHEN
        invoiceDAO.delete(invoice);

        // THEN
        Long identity = invoice.getIdentity();
        assertTrue(invoiceDAO.find(Invoice.withIdentity(identity)).isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_shouldFailWhenIdentityIsMissing() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();

        // WHEN
        invoiceDAO.delete(invoice);
    }

    @Test
    public void testFind_shouldFindObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice inv1 = new Invoice();
        Invoice inv2 = new Invoice();
        Invoice inv3 = new Invoice();
        Invoice matcher = new Invoice();
        List<Invoice> invoiceList;

        LocalDateTime now = LocalDateTime.now();
        inv1.setTime(now);
        inv1.setGross(new BigDecimal("11.0"));
        inv2.setTime(now);
        inv2.setGross(new BigDecimal("12.0"));
        inv3.setTime(now);
        inv3.setGross(new BigDecimal("13.0"));

        invoiceDAO.create(inv1);
        invoiceDAO.create(inv2);
        invoiceDAO.create(inv3);

        // WHEN
        matcher.setIdentity(inv1.getIdentity());
        invoiceList = invoiceDAO.find(matcher);
        assertNotNull(invoiceList);

        // THEN
        assertEquals(1, invoiceList.size());
        assertEquals(inv1, invoiceList.get(0));

        // WHEN
        matcher.setIdentity(inv2.getIdentity());
        invoiceList = invoiceDAO.find(matcher);

        // THEN
        assertEquals(1, invoiceList.size());
        assertEquals(inv2, invoiceList.get(0));

        // WHEN
        matcher.setIdentity(inv3.getIdentity());
        invoiceList = invoiceDAO.find(matcher);

        // THEN
        assertEquals(1, invoiceList.size());
        assertEquals(inv3, invoiceList.get(0));
    }

    @Test
    public void testFind_shouldReturnEmptyListWhenNoObjectIsStored() throws DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setIdentity(1l); // Arbitrary identity

        // WHEN
        List<Invoice> allInvoices = invoiceDAO.getAll();
        assertNotNull(allInvoices);
        assertTrue(allInvoices.isEmpty());

        // THEN
        List<Invoice> results = invoiceDAO.find(invoice);
        assertTrue(results.isEmpty());
    }

    @Test
    public void tesetGetAll_shouldReturnAllObjects() throws ValidationException, DAOException {
        // GIVEN
        Invoice inv1 = new Invoice();
        Invoice inv2 = new Invoice();
        Invoice inv3 = new Invoice();

        LocalDateTime now = LocalDateTime.now();
        inv1.setTime(now);
        inv1.setGross(new BigDecimal("11.0"));
        inv2.setTime(now);
        inv2.setGross(new BigDecimal("12.0"));
        inv3.setTime(now);
        inv3.setGross(new BigDecimal("13.0"));

        invoiceDAO.create(inv1);
        invoiceDAO.create(inv2);
        invoiceDAO.create(inv3);

        // WHEN
        List<Invoice> all = invoiceDAO.getAll();
        assertNotNull(all);

        // THEN
        assertEquals(3, all.size());
        assertTrue(all.contains(inv1));
        assertTrue(all.contains(inv2));
        assertTrue(all.contains(inv3));
    }

    @Test
    public void testGetAll_shouldReturnEmptyList() throws DAOException {
        // WHEN / THEN
        List<Invoice> all = invoiceDAO.getAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }
}
