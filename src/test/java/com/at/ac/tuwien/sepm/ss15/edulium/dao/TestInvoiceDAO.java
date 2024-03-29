package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for InvoiceDAO
 */
public class TestInvoiceDAO extends AbstractDAOTest {
    @Autowired
    private DAO<Invoice> invoiceDAO;
    @Autowired
    private DAO<Order> orderDAO;
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private DAO<Table> tableDAO;
    @Autowired
    private DAO<MenuCategory> menuCategoryDAO;
    @Autowired
    private DAO<TaxRate> taxRateDAO;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private DAO<Section> sectionDAO;

    private Order order1, order2, order3;

    @Before
    public void setUp() throws DAOException, ValidationException {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName("cat");
        menuCategoryDAO.create(menuCategory);

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(0.5));
        taxRateDAO.create(taxRate);

        User user = User.withIdentity("usernamedaoinvoice");
        user.setRole("role");
        user.setName("name");
        userDAO.create(user);

        Section section = new Section();
        section.setName("Garden");
        sectionDAO.create(section);

        Table table = new Table();
        table.setColumn(4);
        table.setRow(3);
        table.setSeats(5);
        table.setNumber(1L);
        table.setSection(section);
        table.setUser(user);
        tableDAO.create(table);

        MenuEntry entry = new MenuEntry();
        entry.setAvailable(true);
        entry.setName("Entry");
        entry.setDescription("Desc");
        entry.setPrice(BigDecimal.valueOf(50.0));
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        menuEntryDAO.create(entry);

        order1 = new Order();
        order1.setTable(table);
        order1.setMenuEntry(entry);
        order1.setBrutto(BigDecimal.valueOf(11.0));
        order1.setTax(BigDecimal.valueOf(0.1));
        order1.setAdditionalInformation("additional info 1");
        order1.setTime(LocalDateTime.now());
        order1.setState(Order.State.QUEUED);
        orderDAO.create(order1);

        order2 = new Order();
        order2.setTable(table);
        order2.setMenuEntry(entry);
        order2.setBrutto(BigDecimal.valueOf(12.0));
        order2.setTax(BigDecimal.valueOf(0.2));
        order2.setAdditionalInformation("additional info 2");
        order2.setTime(LocalDateTime.now());
        order2.setState(Order.State.QUEUED);
        orderDAO.create(order2);

        order3 = new Order();
        order3.setTable(table);
        order3.setMenuEntry(entry);
        order3.setBrutto(BigDecimal.valueOf(13.0));
        order3.setTax(BigDecimal.valueOf(0.3));
        order3.setAdditionalInformation("additional info 3");
        order3.setTime(LocalDateTime.now());
        order3.setState(Order.State.QUEUED);
        orderDAO.create(order3);
    }

    @Test
    public void testCreate_shouldAddObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15.6"));
        invoice.setCreator(getCurrentUser());
        invoice.setOrders(Arrays.asList(order1, order2, order3));
        invoice.setSignature("signature");

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
        invoice.setCreator(getCurrentUser());
        invoice.setOrders(Arrays.asList(order1));
        invoice.setSignature("signature");
        invoiceDAO.create(invoice);

        // WHEN
        assertEquals(1, invoiceDAO.find(invoice).size());
        invoice.setGross(new BigDecimal("29"));
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
        Long identity = 10L;

        while (!invoiceDAO.find(Invoice.withIdentity(identity)).isEmpty()) {
            identity++;
        }

        // WHEN/THEN
        Invoice invoice = new Invoice();
        invoice.setIdentity(identity);
        invoice.setGross(new BigDecimal("2"));
        invoice.setTime(LocalDateTime.now());
        invoice.setCreator(getCurrentUser());
        invoice.setSignature("signature");
        invoice.setOrders(Arrays.asList(order1, order2, order3));
        invoiceDAO.update(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setGross(new BigDecimal("20"));
        invoice.setTime(LocalDateTime.now());
        invoice.setCreator(getCurrentUser());
        invoice.setSignature("signature");
        invoice.setOrders(Arrays.asList(order1, order2, order3));

        // WHEN/THEN
        invoiceDAO.update(invoice);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws ValidationException, DAOException {
        // GIVEN
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("22.0"));
        invoice.setCreator(getCurrentUser());
        invoice.setSignature("signature");
        invoice.setOrders(Arrays.asList(order1, order2, order3));
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
        Long identity = 10L;

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
        inv1.setCreator(getCurrentUser());
        inv1.setOrders(Arrays.asList(order1));
        inv1.setSignature("sig1");
        inv2.setTime(now);
        inv2.setGross(new BigDecimal("12.0"));
        inv2.setCreator(getCurrentUser());
        inv2.setOrders(Arrays.asList(order2));
        inv2.setSignature("sig2");
        inv3.setTime(now);
        inv3.setGross(new BigDecimal("13.0"));
        inv3.setCreator(getCurrentUser());
        inv3.setOrders(Arrays.asList(order3));
        inv3.setSignature("sig3");

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
    public void testFind_shouldReturnEmptyListWhenSearchingNull() throws DAOException {
        // WHEN/THEN
        invoiceDAO.find(null);
    }

    @Test
    public void testFind_shouldReturnEmptyListWhenNoObjectIsStored() throws DAOException {
        // WHEN
        List results = invoiceDAO.find(Invoice.withIdentity(100L));

        // THEN
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFind_shouldFindObjectsWhenSearchingByCreator() throws ValidationException, DAOException {
        // GIVEN
        User creator = getCurrentUser();

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("9.5"));
        invoice.setCreator(creator);
        invoice.setOrders(Arrays.asList(order1, order2, order3));
        invoice.setSignature("signature");

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
        int sizeBefore = invoiceDAO.getAll().size();

        Invoice inv1 = new Invoice();
        Invoice inv2 = new Invoice();
        Invoice inv3 = new Invoice();

        LocalDateTime now = LocalDateTime.now();
        inv1.setTime(now);
        inv1.setGross(new BigDecimal("11.0"));
        inv1.setCreator(getCurrentUser());
        inv1.setOrders(Arrays.asList(order1));
        inv1.setSignature("signature1");
        inv2.setTime(now);
        inv2.setGross(new BigDecimal("12.0"));
        inv2.setCreator(getCurrentUser());
        inv2.setOrders(Arrays.asList(order2));
        inv2.setSignature("signature2");
        inv3.setTime(now);
        inv3.setGross(new BigDecimal("13.0"));
        inv3.setCreator(getCurrentUser());
        inv3.setOrders(Arrays.asList(order3));
        inv3.setSignature("signature3");

        invoiceDAO.create(inv1);
        invoiceDAO.create(inv2);
        invoiceDAO.create(inv3);

        // WHEN
        List<Invoice> all = invoiceDAO.getAll();
        assertNotNull(all);

        // THEN
        assertEquals(sizeBefore + 3, all.size());
        assertTrue(all.contains(inv1));
        assertTrue(all.contains(inv2));
        assertTrue(all.contains(inv3));
    }

    @Ignore // @pg: getting orders in history is still missing
    @Test
    public void testGetHistory_shouldReturnObject() throws ValidationException, DAOException {

        // GIVEN
        // Create
        Invoice invoiceA = new Invoice();
        LocalDateTime createTime = LocalDateTime.now();
        invoiceA.setGross(new BigDecimal("20.5"));
        invoiceA.setTime(createTime);
        invoiceA.setCreator(getCurrentUser());
        invoiceA.setOrders(Arrays.asList(order1));
        invoiceA.setSignature("signatureA");
        invoiceDAO.create(invoiceA);

        // Update
        Invoice invoiceB = Invoice.withIdentity(invoiceA.getIdentity());
        LocalDateTime updateTime = LocalDateTime.now();
        invoiceB.setTime(createTime);
        invoiceB.setGross(new BigDecimal("24"));
        invoiceB.setCreator(getCurrentUser());
        invoiceB.setOrders(Arrays.asList(order2, order3));
        invoiceB.setSignature("signatureB");
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
        assertEquals(getCurrentUser(), event.getUser());
        assertTrue(Duration.between(createTime, event.getTimeOfChange()).getSeconds() < 1);
        assertFalse(event.isDeleted());

        // Update history inspection
        event = history.get(1);
        assertEquals(Long.valueOf(2), event.getChangeNumber());
        assertEquals(invoiceB, event.getData());
        assertEquals(getCurrentUser(), event.getUser());
        assertTrue(Duration.between(updateTime, event.getTimeOfChange()).getSeconds() < 1);
        assertFalse(event.isDeleted());

        // Delete history inspection
        event = history.get(2);
        assertEquals(Long.valueOf(3), event.getChangeNumber());
        assertEquals(invoiceB, event.getData());
        assertEquals(getCurrentUser(), event.getUser());
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

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjects() throws DAOException, ValidationException {
        // PREPARE
        // tax rate 1
        Invoice invoice1 = new Invoice();
        invoice1.setTime(LocalDateTime.now());
        invoice1.setGross(new BigDecimal("11.0"));
        invoice1.setCreator(getCurrentUser());
        invoice1.setOrders(Arrays.asList(order1));
        invoice1.setSignature("signature1");

        invoiceDAO.create(invoice1);
        assertEquals(1, invoiceDAO.find(invoice1).size());

        // tax rate 2
        Invoice invoice2 = new Invoice();
        invoice2.setTime(LocalDateTime.now());
        invoice2.setGross(new BigDecimal("12.0"));
        invoice2.setCreator(getCurrentUser());
        invoice2.setOrders(Arrays.asList(order2));
        invoice2.setSignature("signature2");

        invoiceDAO.create(invoice2);
        assertEquals(1, invoiceDAO.find(invoice2).size());

        // tax rate 3
        Invoice invoice3 = new Invoice();
        invoice3.setTime(LocalDateTime.now());
        invoice3.setGross(new BigDecimal("13.0"));
        invoice3.setCreator(getCurrentUser());
        invoice3.setOrders(Arrays.asList(order3));
        invoice3.setSignature("signature3");

        invoiceDAO.create(invoice3);
        assertEquals(1, invoiceDAO.find(invoice3).size());

        // GIVEN
        Invoice invoiceId1 = Invoice.withIdentity(invoice1.getIdentity());
        Invoice invoiceId2 = Invoice.withIdentity(invoice2.getIdentity());
        Invoice invoiceId3 = Invoice.withIdentity(invoice3.getIdentity());
        List<Invoice> invoiceIds = Arrays.asList(invoiceId1, invoiceId2, invoiceId3);

        // WHEN
        List<Invoice> result = invoiceDAO.populate(invoiceIds);

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(invoice1));
        assertTrue(result.contains(invoice2));
        assertTrue(result.contains(invoice3));
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjectsOfDeletedObjects() throws DAOException, ValidationException {
        // PREPARE
        // tax rate 1
        Invoice invoice1 = new Invoice();
        invoice1.setTime(LocalDateTime.now());
        invoice1.setGross(new BigDecimal("11.0"));
        invoice1.setCreator(getCurrentUser());
        invoice1.setOrders(Arrays.asList(order1));
        invoice1.setSignature("signature1");

        invoiceDAO.create(invoice1);
        assertEquals(1, invoiceDAO.find(invoice1).size());
        invoiceDAO.delete(invoice1);
        assertEquals(0, invoiceDAO.find(invoice1).size());

        // tax rate 2
        Invoice invoice2 = new Invoice();
        invoice2.setTime(LocalDateTime.now());
        invoice2.setGross(new BigDecimal("12.0"));
        invoice2.setCreator(getCurrentUser());
        invoice2.setOrders(Arrays.asList(order2));
        invoice2.setSignature("signature2");

        invoiceDAO.create(invoice2);
        assertEquals(1, invoiceDAO.find(invoice2).size());
        invoiceDAO.delete(invoice2);
        assertEquals(0, invoiceDAO.find(invoice2).size());

        // tax rate 3
        Invoice invoice3 = new Invoice();
        invoice3.setTime(LocalDateTime.now());
        invoice3.setGross(new BigDecimal("13.0"));
        invoice3.setCreator(getCurrentUser());
        invoice3.setOrders(Arrays.asList(order3));
        invoice3.setSignature("signature3");

        invoiceDAO.create(invoice3);
        assertEquals(1, invoiceDAO.find(invoice3).size());
        invoiceDAO.delete(invoice3);
        assertEquals(0, invoiceDAO.find(invoice3).size());

        // GIVEN
        Invoice invoiceId1 = Invoice.withIdentity(invoice1.getIdentity());
        Invoice invoiceId2 = Invoice.withIdentity(invoice2.getIdentity());
        Invoice invoiceId3 = Invoice.withIdentity(invoice3.getIdentity());
        List<Invoice> invoiceIds = Arrays.asList(invoiceId1, invoiceId2, invoiceId3);

        // WHEN
        List<Invoice> result = invoiceDAO.populate(invoiceIds);

        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(invoice1));
        assertTrue(result.contains(invoice2));
        assertTrue(result.contains(invoice3));
    }

    @Test
    public void testPopulate_nullListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // WHEN
        List<Invoice> result = invoiceDAO.populate(null);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPopulate_emptyListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // WHEN
        List<Invoice> result = invoiceDAO.populate(Arrays.asList());

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithInvalidObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<Invoice> invalidInvoices = Arrays.asList(new Invoice());

        // WHEN
        List<Invoice> result = invoiceDAO.populate(invalidInvoices);
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithNullObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<Invoice> invalidInvoices = new ArrayList<>();
        invalidInvoices.add(null);

        // WHEN
        List<Invoice> result = invoiceDAO.populate(invalidInvoices);
    }
}
