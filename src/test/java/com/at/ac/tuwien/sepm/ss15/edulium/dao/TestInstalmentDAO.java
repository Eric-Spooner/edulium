package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Instalment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestInstalmentDAO extends AbstractDAOTest {
    @Autowired
    private DAO<Instalment> instalmentDAO;
    @Autowired
    private DAO<Invoice> invoiceDAO;

    @Test
    public void testCreate_shouldAddObject() throws ValidationException, DAOException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // GIVEN
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        // WHEN
        instalmentDAO.create(instalment);

        // THEN
        // check whether identity was set or not
        Long identity = instalment.getIdentity();
        assertNotNull(identity);

        List<Instalment> instalmentList = instalmentDAO.find(Instalment.withIdentity(identity));
        assertEquals(1, instalmentList.size());
        assertEquals(instalment, instalmentList.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws ValidationException, DAOException {
        // WHEN/THEN
        instalmentDAO.create(null);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingInvalidObjectShouldFail() throws ValidationException, DAOException {
        // GIVEN
        Instalment instalment = new Instalment();
        instalment.setPaymentInfo("Payment info");

        // WHEN/THEN
        instalmentDAO.create(instalment);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws ValidationException, DAOException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // GIVEN
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");
        instalmentDAO.create(instalment);

        // WHEN
        assertEquals(1, instalmentDAO.find(instalment).size());
        instalment.setType("CREDIT_CARD");
        instalmentDAO.update(instalment);

        // THEN
        List<Instalment> instalmentList = instalmentDAO.find(instalment);
        assertEquals(1, instalmentList.size());
        assertEquals(instalment, instalmentList.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws ValidationException, DAOException {
        // WHEN/THEN
        instalmentDAO.update(null);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws ValidationException, DAOException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // GIVEN
        Long identity = 1L;

        try {
            while (!instalmentDAO.find(Instalment.withIdentity(identity)).isEmpty()) {
                identity++;
            }
        } catch (DAOException e) {
            fail();
        }

        // WHEN/THEN
        Instalment instalment = new Instalment();
        instalment.setIdentity(identity);
        instalment.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentDAO.update(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws ValidationException, DAOException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // GIVEN
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        // WHEN/THEN
        instalmentDAO.update(instalment);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws ValidationException, DAOException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // GIVEN
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");
        instalmentDAO.create(instalment);

        // WHEN
        assertEquals(1, instalmentDAO.find(instalment).size());
        instalmentDAO.delete(instalment);

        // THEN
        Long identity = instalment.getIdentity();
        assertTrue(instalmentDAO.find(Instalment.withIdentity(identity)).isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws ValidationException, DAOException {
        // WHEN/THEN
        instalmentDAO.delete(null);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws ValidationException, DAOException {
        // GIVEN
        Long identity = 1L;

        try {
            while (!instalmentDAO.find(Instalment.withIdentity(identity)).isEmpty()) {
                identity++;
            }
        } catch (DAOException e) {
            fail();
        }

        // WHEN/THEN
        instalmentDAO.delete(Instalment.withIdentity(identity));
    }

    @Test
    public void testFind_shouldFindObjectsByIdentity() throws ValidationException, DAOException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // GIVEN
        Instalment inst1 = new Instalment();
        inst1.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst1.setTime(LocalDateTime.now());
        inst1.setType("CASH");
        inst1.setAmount(new BigDecimal("22"));
        inst1.setPaymentInfo("Payment info");

        Instalment inst2 = new Instalment();
        inst2.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst2.setTime(LocalDateTime.now());
        inst2.setType("CREDIT_CARD");
        inst2.setAmount(new BigDecimal("19"));
        inst2.setPaymentInfo("Payment info");

        Instalment inst3 = new Instalment();
        inst3.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst3.setTime(LocalDateTime.now());
        inst3.setType("CASH");
        inst3.setAmount(new BigDecimal("13"));
        inst3.setPaymentInfo("Payment info");

        instalmentDAO.create(inst1);
        instalmentDAO.create(inst2);
        instalmentDAO.create(inst3);

        // WHEN
        List<Instalment> instalmentList = instalmentDAO.find(Instalment.withIdentity(inst1.getIdentity()));

        // THEN
        assertEquals(1, instalmentList.size());
        assertEquals(inst1, instalmentList.get(0));

        // WHEN
        instalmentList = instalmentDAO.find(Instalment.withIdentity(inst2.getIdentity()));

        // THEN
        assertEquals(1, instalmentList.size());
        assertEquals(inst2, instalmentList.get(0));

        // WHEN
        instalmentList = instalmentDAO.find(Instalment.withIdentity(inst3.getIdentity()));

        // THEN
        assertEquals(1, instalmentList.size());
        assertEquals(inst3, instalmentList.get(0));
    }

    @Test
    public void testFind_shouldReturnEmptyListWhenSearchingNull() throws DAOException {
        // WHEN/THEN
        List<Instalment> result = instalmentDAO.find(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFind_shouldReturnEmptyListWhenNoObjectIsStored() throws DAOException {
        // WHEN
        List results = instalmentDAO.find(Instalment.withIdentity(1L));

        // THEN
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFind_shouldFindObjectsWhenSearchingByInvoice() throws ValidationException, DAOException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // GIVEN
        Invoice invoiceId = Invoice.withIdentity(invoice.getIdentity());

        Instalment instalment = new Instalment();
        instalment.setInvoice(invoiceId);
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentDAO.create(instalment);

        // WHEN
        Instalment matcher = new Instalment();
        matcher.setInvoice(invoiceId);
        List<Instalment> instalmentList = instalmentDAO.find(matcher);

        // THEN
        assertEquals(1, instalmentList.size());
        assertEquals(instalment, instalmentList.get(0));
    }

    @Test
    public void testGetAll_shouldReturnAllObjects() throws ValidationException, DAOException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // GIVEN
        Instalment inst1 = new Instalment();
        inst1.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst1.setTime(LocalDateTime.now());
        inst1.setType("CASH");
        inst1.setAmount(new BigDecimal("22"));
        inst1.setPaymentInfo("Payment info");

        Instalment inst2 = new Instalment();
        inst2.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst2.setTime(LocalDateTime.now());
        inst2.setType("CREDIT_CARD");
        inst2.setAmount(new BigDecimal("19"));
        inst2.setPaymentInfo("Payment info");

        Instalment inst3 = new Instalment();
        inst3.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst3.setTime(LocalDateTime.now());
        inst3.setType("CASH");
        inst3.setAmount(new BigDecimal("13"));
        inst3.setPaymentInfo("Payment info");

        instalmentDAO.create(inst1);
        instalmentDAO.create(inst2);
        instalmentDAO.create(inst3);

        // WHEN
        List<Instalment> all = instalmentDAO.getAll();
        assertNotNull(all);

        // THEN
        assertEquals(3, all.size());
        assertTrue(all.contains(inst1));
        assertTrue(all.contains(inst2));
        assertTrue(all.contains(inst3));
    }

    @Test
    public void testGetHistory_shouldReturnObject() throws ValidationException, DAOException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // GIVEN
        // Create
        Instalment instalmentA = new Instalment();
        LocalDateTime createTime = LocalDateTime.now();
        instalmentA.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        instalmentA.setTime(createTime);
        instalmentA.setType("CASH");
        instalmentA.setAmount(new BigDecimal("22"));
        instalmentA.setPaymentInfo("Payment info");
        instalmentDAO.create(instalmentA);

        // Update
        Instalment instalmentB = Instalment.withIdentity(instalmentA.getIdentity());
        LocalDateTime updateTime = LocalDateTime.now();
        instalmentB.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        instalmentB.setTime(updateTime);
        instalmentB.setType("CASH");
        instalmentB.setAmount(new BigDecimal("20"));
        instalmentB.setPaymentInfo("Payment info");
        instalmentDAO.update(instalmentB);

        // Delete
        LocalDateTime deleteTime = LocalDateTime.now();
        instalmentDAO.delete(instalmentB);

        // WHEN
        List<History<Instalment>> history = instalmentDAO.getHistory(instalmentA);

        // THEN
        assertEquals(3, history.size());
        History<Instalment> event;

        // Create history inspection
        event = history.get(0);
        assertEquals(Long.valueOf(1), event.getChangeNumber());
        assertEquals(instalmentA, event.getData());
        assertEquals(getCurrentUser(), event.getUser());
        assertTrue(Duration.between(createTime, event.getTimeOfChange()).getSeconds() < 1);
        assertFalse(event.isDeleted());

        // Update history inspection
        event = history.get(1);
        assertEquals(Long.valueOf(2), event.getChangeNumber());
        assertEquals(instalmentB, event.getData());
        assertEquals(getCurrentUser(), event.getUser());
        assertTrue(Duration.between(updateTime, event.getTimeOfChange()).getSeconds() < 1);
        assertFalse(event.isDeleted());

        // Delete history inspection
        event = history.get(2);
        assertEquals(Long.valueOf(3), event.getChangeNumber());
        assertEquals(instalmentB, event.getData());
        assertEquals(getCurrentUser(), event.getUser());
        assertTrue(Duration.between(deleteTime, event.getTimeOfChange()).getSeconds() < 1);
        assertTrue(event.isDeleted());
    }

    @Test
    public void testGetHistory_nonPersistentDataShouldReturnEmptyList() throws ValidationException, DAOException {
        // GIVEN
        Long identity = 1L;

        try {
            while (!instalmentDAO.find(Instalment.withIdentity(identity)).isEmpty()) {
                identity++;
            }
        } catch (DAOException e) {
            fail();
        }

        // WHEN/THEN
        assertTrue(instalmentDAO.getHistory(Instalment.withIdentity(identity)).isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_shouldFailWithNullObject() throws ValidationException, DAOException {
        // WHEN/THEN
        instalmentDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_shouldFailWithoutIdentity() throws ValidationException, DAOException {
        // GIVEN
        Instalment instalment = new Instalment();

        // WHEN/THEN
        instalmentDAO.getHistory(instalment);
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjects() throws ValidationException, DAOException {
        // PREPARE
        // Invoice
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // Instalment 1
        Instalment inst1 = new Instalment();
        inst1.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst1.setTime(LocalDateTime.now());
        inst1.setType("CASH");
        inst1.setAmount(new BigDecimal("22"));
        inst1.setPaymentInfo("Payment info");

        instalmentDAO.create(inst1);
        assertEquals(1, instalmentDAO.find(inst1).size());

        // Instalment 2
        Instalment inst2 = new Instalment();
        inst2.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst2.setTime(LocalDateTime.now());
        inst2.setType("CREDIT_CARD");
        inst2.setAmount(new BigDecimal("19"));
        inst2.setPaymentInfo("Payment info");

        instalmentDAO.create(inst2);
        assertEquals(1, instalmentDAO.find(inst2).size());

        // Instalment 3
        Instalment inst3 = new Instalment();
        inst3.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst3.setTime(LocalDateTime.now());
        inst3.setType("CASH");
        inst3.setAmount(new BigDecimal("13"));
        inst3.setPaymentInfo("Payment info");

        instalmentDAO.create(inst3);
        assertEquals(1, instalmentDAO.find(inst3).size());

        // GIVEN
        Instalment instalmentId1 = Instalment.withIdentity(inst1.getIdentity());
        Instalment instalmentId2 = Instalment.withIdentity(inst2.getIdentity());
        Instalment instalmentId3 = Instalment.withIdentity(inst3.getIdentity());
        List<Instalment> instalmentIds = Arrays.asList(instalmentId1, instalmentId2, instalmentId3);

        // WHEN
        List<Instalment> result = instalmentDAO.populate(instalmentIds);

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(inst1));
        assertTrue(result.contains(inst2));
        assertTrue(result.contains(inst3));
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjectsOfDeletedObjects() throws ValidationException, DAOException {
        // PREPARE
        // Invoice
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // Instalment 1
        Instalment inst1 = new Instalment();
        inst1.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst1.setTime(LocalDateTime.now());
        inst1.setType("CASH");
        inst1.setAmount(new BigDecimal("22"));
        inst1.setPaymentInfo("Payment info");

        instalmentDAO.create(inst1);
        assertEquals(1, instalmentDAO.find(inst1).size());
        instalmentDAO.delete(inst1);
        assertEquals(0, instalmentDAO.find(inst1).size());

        // Instalment 2
        Instalment inst2 = new Instalment();
        inst2.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst2.setTime(LocalDateTime.now());
        inst2.setType("CREDIT_CARD");
        inst2.setAmount(new BigDecimal("19"));
        inst2.setPaymentInfo("Payment info");

        instalmentDAO.create(inst2);
        assertEquals(1, instalmentDAO.find(inst2).size());
        instalmentDAO.delete(inst2);
        assertEquals(0, instalmentDAO.find(inst2).size());

        // Instalment 3
        Instalment inst3 = new Instalment();
        inst3.setInvoice(Invoice.withIdentity(invoice.getIdentity()));
        inst3.setTime(LocalDateTime.now());
        inst3.setType("CASH");
        inst3.setAmount(new BigDecimal("13"));
        inst3.setPaymentInfo("Payment info");

        instalmentDAO.create(inst3);
        assertEquals(1, instalmentDAO.find(inst3).size());
        instalmentDAO.delete(inst3);
        assertEquals(0, instalmentDAO.find(inst3).size());

        // GIVEN
        Instalment instalmentId1 = Instalment.withIdentity(inst1.getIdentity());
        Instalment instalmentId2 = Instalment.withIdentity(inst2.getIdentity());
        Instalment instalmentId3 = Instalment.withIdentity(inst3.getIdentity());
        List<Instalment> instalmentIds = Arrays.asList(instalmentId1, instalmentId2, instalmentId3);

        // WHEN
        List<Instalment> result = instalmentDAO.populate(instalmentIds);

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(inst1));
        assertTrue(result.contains(inst2));
        assertTrue(result.contains(inst3));
    }

    @Test
    public void testPopulate_nullListShouldReturnEmptyObjects() throws ValidationException, DAOException {
        // WHEN
        List<Instalment> instalmentList = instalmentDAO.populate(null);

        // THEN
        assertTrue(instalmentList.isEmpty());
    }

    @Test
    public void testPopulate_emptyListShouldReturnEmptyObjects() throws ValidationException, DAOException {
        // WHEN
        List<Instalment> instalmentList = instalmentDAO.populate(Arrays.asList());

        // THEN
        assertTrue(instalmentList.isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithInvalidObjectsShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        List<Instalment> instalmentList = Arrays.asList(new Instalment());

        // WHEN/THEN
        List<Instalment> result = instalmentDAO.populate(instalmentList);
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithNullObjectsShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        List<Instalment> invalidInstalments = new ArrayList<>();
        invalidInstalments.add(null);

        // WHEN/THEN
        List<Instalment> result = instalmentDAO.populate(invalidInstalments);
    }
}
