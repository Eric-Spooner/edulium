package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Instalment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestInstalmentDAO extends AbstractDAOTest {
    @Autowired
    private ImmutableDAO<Instalment> instalmentDAO;
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
        instalment.setInvoice(invoice);
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
    public void testFind_shouldFindObjectsByIdentity() throws ValidationException, DAOException {
        // PREPARE
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());

        invoiceDAO.create(invoice);

        // GIVEN
        Instalment inst1 = new Instalment();
        inst1.setInvoice(invoice);
        inst1.setTime(LocalDateTime.now());
        inst1.setType("CASH");
        inst1.setAmount(new BigDecimal("22"));
        inst1.setPaymentInfo("Payment info");

        Instalment inst2 = new Instalment();
        inst2.setInvoice(invoice);
        inst2.setTime(LocalDateTime.now());
        inst2.setType("CREDIT_CARD");
        inst2.setAmount(new BigDecimal("19"));
        inst2.setPaymentInfo("Payment info");

        Instalment inst3 = new Instalment();
        inst3.setInvoice(invoice);
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
        Instalment instalment = new Instalment();
        instalment.setInvoice(invoice);
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentDAO.create(instalment);

        // WHEN
        Invoice invoiceId = Invoice.withIdentity(invoice.getIdentity());
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
        inst1.setInvoice(invoice);
        inst1.setTime(LocalDateTime.now());
        inst1.setType("CASH");
        inst1.setAmount(new BigDecimal("22"));
        inst1.setPaymentInfo("Payment info");

        Instalment inst2 = new Instalment();
        inst2.setInvoice(invoice);
        inst2.setTime(LocalDateTime.now());
        inst2.setType("CREDIT_CARD");
        inst2.setAmount(new BigDecimal("19"));
        inst2.setPaymentInfo("Payment info");

        Instalment inst3 = new Instalment();
        inst3.setInvoice(invoice);
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
        inst1.setInvoice(invoice);
        inst1.setTime(LocalDateTime.now());
        inst1.setType("CASH");
        inst1.setAmount(new BigDecimal("22"));
        inst1.setPaymentInfo("Payment info");

        instalmentDAO.create(inst1);
        assertEquals(1, instalmentDAO.find(inst1).size());

        // Instalment 2
        Instalment inst2 = new Instalment();
        inst2.setInvoice(invoice);
        inst2.setTime(LocalDateTime.now());
        inst2.setType("CREDIT_CARD");
        inst2.setAmount(new BigDecimal("19"));
        inst2.setPaymentInfo("Payment info");

        instalmentDAO.create(inst2);
        assertEquals(1, instalmentDAO.find(inst2).size());

        // Instalment 3
        Instalment inst3 = new Instalment();
        inst3.setInvoice(invoice);
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
