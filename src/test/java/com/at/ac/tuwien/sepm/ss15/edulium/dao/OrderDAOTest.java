package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import javafx.scene.control.Tab;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test for the OrderDAO
 */
public class OrderDAOTest extends AbstractDAOTest {
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

    public Section createSection(String name){
        Section section = new Section();
        section.setName(name);
        return section;
    }

    public Table createTable(Integer tableCol, Integer tableRow,
                             Long tableNumber, Section section) throws ValidationException, DAOException {
        User user = new User();
        user.setName("Test User");
        user.setRole("Role");
        userDAO.create(user);

        Table table = new Table();
        table.setColumn(4);
        table.setRow(3);
        table.setSeats(5);
        table.setNumber(1L);
        table.setSection_id(section.getIdentity());
        table.setUser_id(Long.valueOf(user.getIdentity()));
        return table;
    }

    private Order createOrder(BigDecimal brutto, String info, BigDecimal tax,  LocalDateTime time)
            throws ValidationException, DAOException {

        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName("cat");
        menuCategoryDAO.create(menuCategory);

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(20));
        taxRateDAO.create(taxRate);

        User user = new User();
        user.setName("Test User");
        user.setRole("Role");
        userDAO.create(user);

        Section section = new Section();
        section.setName("Garden");
        sectionDAO.create(section);

        Table table = new Table();
        table.setColumn(4);
        table.setRow(3);
        table.setSeats(5);
        table.setNumber(1L);
        table.setSection_id(section.getIdentity());
        table.setUser_id(Long.valueOf(user.getIdentity()));
        tableDAO.create(table);

        MenuEntry entry = new MenuEntry();
        entry.setAvailable(true);
        entry.setName("Entry");
        entry.setDescription("Desc");
        entry.setPrice(BigDecimal.valueOf(50.0));
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        menuEntryDAO.create(entry);

        Order order = new Order();
        order.setTable(table);
        order.setMenuEntry(entry);
        order.setBrutto(brutto);
        order.setTax(tax);
        order.setAdditionalInformation(info);
        order.setTime(time);

        return order;
    }


    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        Order order = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(50),
                LocalDateTime.now());
        // WHEN
        orderDAO.create(order);

        // THEN
        // try to find the user and compare it
        List<Order> storedObjects = orderDAO.find(Order.withIdentity(order.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(order, storedObjects.get(0));
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
        Order order= createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(50),
                LocalDateTime.now());
        orderDAO.create(order);

        // check if entry is stored
        assertEquals(1, orderDAO.find(Order.withIdentity(order.getIdentity())).size());

        // WHEN
        Order order2 = order = createOrder(BigDecimal.valueOf(1000),"Order Information 2",
                BigDecimal.valueOf(100), LocalDateTime.now());
        order2.setIdentity(order.getIdentity());
        orderDAO.update(order);

        // THEN
        // check if entry was updatedupdatedMenu
        List<Order> storedObjects = orderDAO.find(Order.withIdentity(order2.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(order2, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail()
            throws DAOException, ValidationException {
        // Given
        Order order  = createOrder(BigDecimal.valueOf(500),"Order Information",
                BigDecimal.valueOf(50), LocalDateTime.now());

        // When
        orderDAO.update(order);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException,
            ValidationException {
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
            //The test should try to update an object and should not fail while finding
            fail();
        }

        order  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(50),
                LocalDateTime.now());

        // WHEN
        orderDAO.update(order);
    }

    public void testUpdate_updatingTableShouldWork() throws DAOException,
            ValidationException {
        // GIVEN
        Order order  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(50),
                LocalDateTime.now());

        // WHEN
        orderDAO.create(order);

        Order order2  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(50),
                LocalDateTime.now());
        Section section = createSection("Hauptraum");
        Table table = createTable(5,6,5L,section);
        tableDAO.create(table);
        order2.setTable(table);
        order2.setIdentity(order.getIdentity());
        orderDAO.update(order2);

        // THEN
        assertEquals(order2, orderDAO.find(Order.withIdentity(order2.getIdentity())).get(0));
    }

    public void testUpdate_updatingTimeShouldWork() throws DAOException,
            ValidationException {
        // GIVEN
        Order order  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(50),
                LocalDateTime.now());

        // WHEN
        orderDAO.create(order);

        Order order2  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(50),
                LocalDateTime.now());

        // THEN
        assertEquals(order2, orderDAO.find(Order.withIdentity(order2.getIdentity())).get(0));
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // GIVEN
        Order order  = createOrder(BigDecimal.valueOf(500),"Order Information", BigDecimal.valueOf(50),
                LocalDateTime.now());

        orderDAO.create(order);
        assertEquals(1, orderDAO.find(Order.withIdentity(order.getIdentity())).size());

        // WHEN
        orderDAO.delete(order);

        // Then
        assertEquals(0, orderDAO.find(Order.withIdentity(order.getIdentity())).size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException,
            ValidationException {
        // GIVEN
        Order order = new Order();

        // WHEN
        orderDAO.delete(order);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException,
            ValidationException {
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
            //The test should not fail while finding, its about the deleting
            fail();
        }

        // WHEN
        orderDAO.delete(order);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // GIVEN
        Order order1 = createOrder(BigDecimal.valueOf(500),"Order Information1",
                BigDecimal.valueOf(50), LocalDateTime.now());
        orderDAO.create(order1);
        Order order2  = createOrder(BigDecimal.valueOf(500),"Order Information2",
                BigDecimal.valueOf(50), LocalDateTime.now());
        orderDAO.create(order2);
        Order order3  = createOrder(BigDecimal.valueOf(500),"Order Information3",
                BigDecimal.valueOf(50), LocalDateTime.now());
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
        Order order1  = createOrder(BigDecimal.valueOf(500),"Order Information1",
                BigDecimal.valueOf(50), LocalDateTime.now());
        orderDAO.create(order1);
        Order order2  = createOrder(BigDecimal.valueOf(1000),"Order Information2",
                BigDecimal.valueOf(50), LocalDateTime.now());
        orderDAO.create(order2);
        Order order3 = createOrder(BigDecimal.valueOf(500), "Order Information3",
                BigDecimal.valueOf(50), LocalDateTime.now());
        orderDAO.create(order3);


        // WHEN
        matcher.setAdditionalInformation(order1.getAdditionalInformation());
        List<Order> objects = orderDAO.find(matcher);

        // THEN
        assertEquals(2, objects.size());
        assertTrue(objects.contains(order1));
        assertTrue(objects.contains(order2));

        // WHEN
        matcher.setAdditionalInformation(order1.getAdditionalInformation());
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
    public void testGetHistory_notPersistentDataShouldReturnEmptyList()
            throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        Order matcher = new Order();
        matcher.setIdentity(identity);

        // generate identity which is not used by any persistent object
        while (!orderDAO.find(matcher).isEmpty()) {
            identity++;
            matcher.setIdentity(identity);
        }

        // WHEN THEN
        assertTrue(orderDAO.getHistory(matcher).isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN

        // create data
        Order order1  = createOrder(BigDecimal.valueOf(500),"Order Information",
                BigDecimal.valueOf(50), LocalDateTime.now());
        LocalDateTime createTime = LocalDateTime.now();
        orderDAO.create(order1);

        Order order2  = createOrder(BigDecimal.valueOf(600),"Order Information2",
                BigDecimal.valueOf(60), LocalDateTime.now());
        order2.setIdentity(order1.getIdentity());
        LocalDateTime updateTime = LocalDateTime.now();
        orderDAO.update(order2);

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        orderDAO.delete(order2);

        // WHEN
        List<History<Order>> history = orderDAO.getHistory(order2);

        // THEN
        //check sive of history
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
