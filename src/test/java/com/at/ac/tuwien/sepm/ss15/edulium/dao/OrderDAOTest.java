package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.impl.DBMenuEntryDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.impl.DBOrderDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Unit Test for the OrderDAO
 */
public class OrderDAOTest extends AbstractDAOTest {
    @Autowired
    private DBOrderDAO orderDAO;
    @Autowired
    private DBMenuEntryDAO menuEntryDAO;
    @Autowired
    private TableDAO tableDAO;
    @Autowired
    private DAO<MenuCategory> menuCategoryDAO;
    @Autowired
    private DAO<TaxRate> taxRateDAO;

    private MenuEntry createMenuEntry(String name, String desc, String cat,
                                      double price, double tax, boolean available) throws ValidationException, DAOException {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName(cat);

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(tax));

        menuCategoryDAO.create(menuCategory);
        taxRateDAO.create(taxRate);

        MenuEntry entry = new MenuEntry();
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        entry.setName(name);
        entry.setDescription(desc);
        entry.setPrice(BigDecimal.valueOf(price));
        entry.setAvailable(available);

        return entry;
    }

    private Table createTable(Long sectionID, Long userID, Integer seats,
                              Integer column, Integer row) throws ValidationException, DAOException{
        Table table = new Table();
        table.setSection_id(sectionID);
        table.setUser_id(userID);
        table.setSeats(seats);
        table.setColumn(column);
        table.setRow(row);
        return table;
    }

    private Order createOrder(Double brutto, Boolean canceled, String info,
                                  Double tax, Table table, MenuEntry entry, Timestamp time) throws ValidationException, DAOException {
        tableDAO.create(table);
        menuEntryDAO.create(entry);

        Order order = new Order();
        order.setTable(table);
        order.setMenueEntry(entry);
        order.setBrutto(brutto);
        order.setCanceled(canceled);
        order.setInfo(info);
        order.setTax(tax);
        order.setTime(time);

        return order;
    }


    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        Order order = createOrder(500.0,false,"new Order",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));

        // WHEN
        orderDAO.create(order);

        // THEN
        // try to find the user and compare it
        Order matcher = new Order();
        matcher.setIdentity(order.getIdentity());

        List<Order> storedObjects = orderDAO.find(matcher);
        assertEquals(1, storedObjects.size());
        assertEquals(order, storedObjects.get(0));
    }

    @Test(expected = DAOException.class)
    public void testCreate_addingTwoObjectsWithSameIdentityShouldFail() throws DAOException, ValidationException {
        // PREPARE
        Order order = createOrder(500.0,false,"new Order",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));

        try {
            orderDAO.create(order);
        } catch (DAOException e) {
            fail("DAOException should not occur while adding a new user with a non-existing identity");
        }

        // check if user is stored
        assertEquals(1, orderDAO.find(order).size());

        // GIVEN
        Order order2 = createOrder(500.0,false,"new Order2",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        order2.setIdentity(order.getIdentity());

        // WHEN
        orderDAO.create(order2);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingEmptyObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();

        // WHEN
        orderDAO.create(order);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = null;

        // WHEN
        orderDAO.create(order);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        //  GIVEN
        Order order = createOrder(500.0,false,"new Order",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        orderDAO.create(order);

        // check if entry is stored
        assertEquals(order, orderDAO.find(Order.withIdentity(order.getIdentity())).get(0));

        // WHEN
        Order order2 = createOrder(500.0,false,"new Order2",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        order2.setIdentity(order.getIdentity());
        orderDAO.update(order);

        // THEN
        // check if entry was updatedupdatedMenu
        List<Order> storedObjects = orderDAO.find(Order.withIdentity(order2.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(order2, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        //Given
        Order order = createOrder(500.0,false,"new Order",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));

        //When
        orderDAO.update(order);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        Order order = createOrder(500.0,false,"new Order",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        order.setIdentity(identity);

        // generate identity which is not used by any persistent object
        try {
            while (!orderDAO.find(order).isEmpty()) {
                identity++;
                order.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            fail();
        }

        // WHEN
        orderDAO.update(order);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // GIVEN
        Order order = createOrder(500.0,false,"new Order",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));

        orderDAO.create(order);

        // check if entry created
        List<Order> objects = orderDAO.find(Order.withIdentity(order.getIdentity()));
        assertEquals(1, objects.size());
        assertEquals(order, objects.get(0));

        // WHEN
        orderDAO.delete(order);

        // THEN
        // check if entry was removed
        assertTrue(orderDAO.find(Order.withIdentity(order.getIdentity())).isEmpty());
        assertTrue(orderDAO.getAll().isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();

        // WHEN
        orderDAO.delete(order);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        Order order = new Order();
        order.setIdentity(identity);

        // generate identity which is not used by any persistent object
        try {
            while (!orderDAO.find(order).isEmpty()) {
                identity++;
                order.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            fail();
        }

        // WHEN
        orderDAO.delete(order);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // GIVEN
        Order order1 = createOrder(500.0,false,"new Order1",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        orderDAO.create(order1);
        Order order2 = createOrder(500.0,false,"new Order2",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        orderDAO.create(order2);
        Order order3 = createOrder(500.0,false,"new Order3",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        orderDAO.create(order3);

        // WHEN
        List<Order> objects = orderDAO.find(Order.withIdentity(order1.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(order1, objects.get(0));

        // WHEN
        List<Order> objects2 = orderDAO.find(Order.withIdentity(order2.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(order1, objects.get(0));

        // WHEN
        List<Order> objects3 = orderDAO.find(Order.withIdentity(order3.getIdentity()));
        // THEN
        assertEquals(1, objects.size());
        assertEquals(order1, objects.get(0));
    }

    @Test
    public void testFind_byInfoShouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        Order matcher = new Order();
        Order order1 = createOrder(500.0,false,"new Order",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        orderDAO.create(order1);
        Order order2 = createOrder(500.0,false,"new Order",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        orderDAO.create(order2);
        Order order3 = createOrder(500.0,false,"new Order2",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        orderDAO.create(order3);


        // WHEN
        matcher.setInfo(order1.getInfo());
        List<Order> objects = orderDAO.find(matcher);

        // THEN
        assertEquals(2, objects.size());
        assertTrue(objects.contains(order1));
        assertTrue(objects.contains(order2));

        // WHEN
        matcher.setInfo(order1.getInfo());
        objects = orderDAO.find(matcher);

        // THEN
        assertEquals(1, objects.size());
        assertEquals(order3, objects.get(0));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        Long identity = (long) 1;
        Order matcher = new Order();
        matcher.setIdentity(identity);

        // generate identity which is not used by any persistent object
        while (!orderDAO.find(matcher).isEmpty()) {
            identity++;
            matcher.setIdentity(identity);
        }

        // WHEN
        List<Order> storedObjects = orderDAO.find(matcher);

        // THEN
        assertTrue(storedObjects.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnEmptyList() throws DAOException {
        // WHEN / THEN
        assertTrue(orderDAO.getAll().isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        Order order1 = createOrder(500.0,false,"new Order1",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        orderDAO.create(order1);
        Order order2 = createOrder(500.0,false,"new Order2",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        orderDAO.create(order2);
        Order order3 = createOrder(500.0,false,"new Order3",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        orderDAO.create(order3);
        // WHEN
        List<Order> objects = orderDAO.getAll();
        // THEN
        assertEquals(3, objects.size());
        assertTrue(objects.contains(order1));
        assertTrue(objects.contains(order2));
        assertTrue(objects.contains(order3));
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        //WHEN THEN
        orderDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order entry = new Order();

        // WHEN
        orderDAO.getHistory(entry);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        Order matcher = new Order();
        matcher.setIdentity(identity);

        // generate identity which is not used by any persistent object
        while (!orderDAO.find(matcher).isEmpty()) {
            identity++;
            matcher.setIdentity(identity);
        }

        //WHEN THEN
        assertTrue(orderDAO.getHistory(matcher).isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN
        // create data
        Order order1 = createOrder(500.0,false,"new Order1",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        LocalDateTime createTime = LocalDateTime.now();
        orderDAO.create(order1);

        Order order2 = createOrder(500.0,false,"new Order1",50.0, createTable((long)1,(long)1,5,2,2), createMenuEntry("entry1", "desc", "cat", 20, 0.2, true)
                , new Timestamp(System.currentTimeMillis()));
        order2.setIdentity(order1.getIdentity());
        LocalDateTime updateTime = LocalDateTime.now();
        orderDAO.update(order2);
        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        orderDAO.delete(order2);

        // WHEN
        List<History<Order>> history = orderDAO.getHistory(order2);

        // THEN
        assertEquals(3, history.size());

        // check create history
        History<Order> historyEntry = history.get(0);
        assertEquals(Long.valueOf(1), historyEntry.getChangeNumber());
        assertEquals(order1, historyEntry.getData());
        assertEquals(user, historyEntry.getUser());
        assertTrue(Duration.between(createTime, historyEntry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(historyEntry.isDeleted());

        // check update history
        historyEntry = history.get(1);
        assertEquals(Long.valueOf(2), historyEntry.getChangeNumber());
        assertEquals(order2, historyEntry.getData());
        assertEquals(user, historyEntry.getUser());
        assertTrue(Duration.between(updateTime, historyEntry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(historyEntry.isDeleted());

        // check delete history
        historyEntry = history.get(2);
        assertEquals(Long.valueOf(3), historyEntry.getChangeNumber());
        assertEquals(order2, historyEntry.getData());
        assertEquals(user, historyEntry.getUser());
        assertTrue(Duration.between(deleteTime, historyEntry.getTimeOfChange()).getSeconds() < 1);
        assertTrue(historyEntry.isDeleted());
    }

}
