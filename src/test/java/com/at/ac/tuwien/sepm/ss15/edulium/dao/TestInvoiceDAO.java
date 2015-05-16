package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.*;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
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
        invoice.setCreator(User.withIdentity("TestUser"));

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

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws ValidationException, DAOException {
        // WHEN/THEN
        invoiceDAO.create(null);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingInvalidObjectShouldFail() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setGross(new BigDecimal("5"));

        // WHEN/THEN
        invoiceDAO.create(invoice);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("26.7"));
        invoice.setCreator(User.withIdentity("TestUser"));
        invoiceDAO.create(invoice);

        // WHEN
        assertEquals(1, invoiceDAO.find(invoice).size());
        invoice.setPaid(Boolean.TRUE);
        invoiceDAO.update(invoice);

        // THEN
        List<Invoice> invoiceList = invoiceDAO.find(invoice);
        assertEquals(1, invoiceList.size());
        assertEquals(invoice, invoiceList.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws ValidationException, DAOException {
        // WHEN
        invoiceDAO.update(null);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws ValidationException, DAOException {
        // GIVEN
        Long identity = 1L;

        while (!invoiceDAO.find(Invoice.withIdentity(identity)).isEmpty()) {
            identity++;
        }

        // WHEN/THEN
        Invoice invoice = new Invoice();
        invoice.setIdentity(identity);
        invoice.setGross(new BigDecimal("2"));
        invoice.setTime(LocalDateTime.now());
        invoice.setCreator(User.withIdentity("TestUser"));
        invoice.setPaid(Boolean.TRUE);
        invoiceDAO.update(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setGross(new BigDecimal("20"));
        invoice.setTime(LocalDateTime.now());
        invoice.setPaid(Boolean.TRUE);
        invoice.setCreator(User.withIdentity("TestUser"));

        // WHEN/THEN
        invoiceDAO.update(invoice);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("22.0"));
        invoice.setCreator(User.withIdentity("TestUser"));
        invoiceDAO.create(invoice);

        // WHEN
        assertEquals(1, invoiceDAO.find(invoice).size());
        invoiceDAO.delete(invoice);

        // THEN
        Long identity = invoice.getIdentity();
        assertTrue(invoiceDAO.find(Invoice.withIdentity(identity)).isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws ValidationException, DAOException {
        // WHEN
        invoiceDAO.delete(null);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws ValidationException, DAOException {
        // GIVEN
        Long identity = 1L;

        while (!invoiceDAO.find(Invoice.withIdentity(identity)).isEmpty()) {
            identity++;
        }

        // WHEN/THEN
        invoiceDAO.delete(Invoice.withIdentity(identity));
    }

    @Test
    public void testFind_shouldFindObjectsByIdentity() throws ValidationException, DAOException {
        // GIVEN
        Invoice inv1 = new Invoice();
        Invoice inv2 = new Invoice();
        Invoice inv3 = new Invoice();
        List<Invoice> invoiceList;

        LocalDateTime now = LocalDateTime.now();
        inv1.setTime(now);
        inv1.setGross(new BigDecimal("11.0"));
        inv1.setCreator(User.withIdentity("TestUser1"));
        inv2.setTime(now);
        inv2.setGross(new BigDecimal("12.0"));
        inv2.setCreator(User.withIdentity("TestUser2"));
        inv3.setTime(now);
        inv3.setGross(new BigDecimal("13.0"));
        inv3.setCreator(User.withIdentity("TestUser3"));

        invoiceDAO.create(inv1);
        invoiceDAO.create(inv2);
        invoiceDAO.create(inv3);

        // WHEN
        invoiceList = invoiceDAO.find(Invoice.withIdentity(inv1.getIdentity()));

        // THEN
        assertEquals(1, invoiceList.size());
        assertEquals(inv1, invoiceList.get(0));

        // WHEN
        invoiceList = invoiceDAO.find(Invoice.withIdentity(inv2.getIdentity()));

        // THEN
        assertEquals(1, invoiceList.size());
        assertEquals(inv2, invoiceList.get(0));

        // WHEN
        invoiceList = invoiceDAO.find(Invoice.withIdentity(inv3.getIdentity()));

        // THEN
        assertEquals(1, invoiceList.size());
        assertEquals(inv3, invoiceList.get(0));
    }

    @Test
    public void testFind_shouldReturnEmptyListWhenNoObjectIsStored() throws DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setIdentity(1L); // Arbitrary identity

        // WHEN
        List results = invoiceDAO.find(invoice);

        // THEN
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFind_shouldFindObjectsWhenSearchingByCreator() throws ValidationException, DAOException {
        // GIVEN
        User creator = User.withIdentity("User");

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("9.5"));
        invoice.setCreator(creator);
        invoiceDAO.create(invoice);

        // WHEN
        Invoice matcher = new Invoice();
        matcher.setCreator(creator);
        List<Invoice> invoices = invoiceDAO.find(invoice);

        // THEN
        assertFalse(invoices.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnAllObjects() throws ValidationException, DAOException {
        // GIVEN
        Invoice inv1 = new Invoice();
        Invoice inv2 = new Invoice();
        Invoice inv3 = new Invoice();

        LocalDateTime now = LocalDateTime.now();
        inv1.setTime(now);
        inv1.setGross(new BigDecimal("11.0"));
        inv1.setCreator(User.withIdentity("TestUser1"));
        inv2.setTime(now);
        inv2.setGross(new BigDecimal("12.0"));
        inv2.setCreator(User.withIdentity("TestUser2"));
        inv3.setTime(now);
        inv3.setGross(new BigDecimal("13.0"));
        inv3.setCreator(User.withIdentity("TestUser3"));

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
    public void testGetHistory_shouldReturnObject() throws ValidationException, DAOException {
        User user = getCurrentUser();

        // GIVEN
        // Create
        Invoice invoiceA = new Invoice();
        LocalDateTime createTime = LocalDateTime.now();
        invoiceA.setGross(new BigDecimal("20.5"));
        invoiceA.setTime(createTime);
        invoiceA.setCreator(user);
        invoiceDAO.create(invoiceA);

        // Update
        Invoice invoiceB = Invoice.withIdentity(invoiceA.getIdentity());
        LocalDateTime updateTime = LocalDateTime.now();
        invoiceB.setGross(new BigDecimal("24"));
        invoiceDAO.update(invoiceB);

        // Delete
        LocalDateTime deleteTime = LocalDateTime.now();
        invoiceDAO.delete(invoiceB);

        // WHEN
        List<History<Invoice>> history = invoiceDAO.getHistory(invoiceA);

        // THEN
        assertEquals(3, history.size());
        History<Invoice> event;

        // Create history inspection
        event = history.get(0);
        assertEquals(Long.valueOf(1), event.getChangeNumber());
        assertEquals(invoiceA, event.getData());
        assertEquals(user, event.getUser());
        assertTrue(Duration.between(createTime, event.getTimeOfChange()).getSeconds() < 1);
        assertFalse(event.isDeleted());

        // Update history inspection
        event = history.get(1);
        assertEquals(Long.valueOf(2), event.getChangeNumber());
        assertEquals(invoiceB, event.getData());
        assertEquals(user, event.getUser());
        assertTrue(Duration.between(updateTime, event.getTimeOfChange()).getSeconds() < 1);
        assertFalse(event.isDeleted());

        // Delete history inspection
        event = history.get(3);
        assertEquals(Long.valueOf(3), event.getChangeNumber());
        assertEquals(invoiceB, event.getData());
        assertEquals(user, event.getUser());
        assertTrue(Duration.between(deleteTime, event.getTimeOfChange()).getSeconds() < 1);
        assertTrue(event.isDeleted());
    }

    @Test
    public void testGetHistory_nonPersistentDataShouldReturnEmptyList() throws ValidationException, DAOException {
        // GIVEN
        Long identity = 1L;

        while(!invoiceDAO.find(Invoice.withIdentity(identity)).isEmpty()) {
            identity++;
        }

        // WHEN/THEN
        assertTrue(invoiceDAO.getHistory(Invoice.withIdentity(identity)).isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_shouldFailWithNullObject() throws ValidationException, DAOException {
        invoiceDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_shouldFailWithoutIdentity() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();

        // WHEN
        invoiceDAO.getHistory(invoice);
    }
}
