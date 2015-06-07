package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Instalment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class TestInstalmentDAO extends AbstractDAOTest {
    @Autowired
    private DAO<Instalment> instalmentDAO;

    @Test
    public void testCreate_shouldAddObject() throws ValidationException, DAOException {
        // GIVEN
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
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
        // GIVEN
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
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
        // GIVEN
        Long identity = 1L;

        while (!instalmentDAO.find(Instalment.withIdentity(identity)).isEmpty()) {
            identity++;
        }

        // WHEN/THEN
        Instalment instalment = new Instalment();
        instalment.setIdentity(identity);
        instalment.setInvoice(Invoice.withIdentity(1L));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentDAO.update(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws ValidationException, DAOException {
        // GIVEN
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        // WHEN/THEN
        instalmentDAO.update(instalment);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws ValidationException, DAOException {
        // GIVEN
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
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

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws ValidationException, DAOException {
        // GIVEN
        Long identity = 1L;

        while (!instalmentDAO.find(Instalment.withIdentity(identity)).isEmpty()) {
            identity++;
        }

        // WHEN/THEN
        instalmentDAO.delete(Instalment.withIdentity(identity));
    }

    @Test
    public void testFind_shouldFindObjectsByIdentity() throws ValidationException, DAOException {
        // GIVEN
        Instalment inst1 = new Instalment();
        inst1.setInvoice(Invoice.withIdentity(1L));
        inst1.setTime(LocalDateTime.now());
        inst1.setType("CASH");
        inst1.setAmount(new BigDecimal("22"));
        inst1.setPaymentInfo("Payment info");

        Instalment inst2 = new Instalment();
        inst2.setInvoice(Invoice.withIdentity(1L));
        inst2.setTime(LocalDateTime.now());
        inst2.setType("CREDIT_CARD");
        inst2.setAmount(new BigDecimal("19"));
        inst2.setPaymentInfo("Payment info");

        Instalment inst3 = new Instalment();
        inst3.setInvoice(Invoice.withIdentity(1L));
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
        instalmentDAO.find(null);
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
        // GIVEN

        Invoice invoice = Invoice.withIdentity(1L);

        Instalment instalment = new Instalment();
        instalment.setInvoice(invoice);
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentDAO.create(instalment);

        // WHEN
        Instalment matcher = new Instalment();
        matcher.setInvoice(invoice);
        List<Instalment> instalmentList = instalmentDAO.find(matcher);

        // THEN
        assertFalse(instalmentList.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnAllObjects() throws ValidationException, DAOException {
        // GIVEN
        Instalment inst1 = new Instalment();
        inst1.setInvoice(Invoice.withIdentity(1L));
        inst1.setTime(LocalDateTime.now());
        inst1.setType("CASH");
        inst1.setAmount(new BigDecimal("22"));
        inst1.setPaymentInfo("Payment info");

        Instalment inst2 = new Instalment();
        inst2.setInvoice(Invoice.withIdentity(1L));
        inst2.setTime(LocalDateTime.now());
        inst2.setType("CREDIT_CARD");
        inst2.setAmount(new BigDecimal("19"));
        inst2.setPaymentInfo("Payment info");

        Instalment inst3 = new Instalment();
        inst3.setInvoice(Invoice.withIdentity(1L));
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
}
