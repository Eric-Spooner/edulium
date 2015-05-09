package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Unit Test for the OrderDAO
 */
public class TestOrderDAO extends AbstractDAOTest {
    @Autowired
    private OrderDAO orderDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity("Order identity");
        order.setBrutto(500);
        order.setCanceled(false);
        order.setInfo("Order Info");
        order.setTax(20);
        order.setTime(new Timestamp(System.currentTimeMillis()));

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
        Order order1 = new Order();
        order1.setIdentity("order1");
        order1.setBrutto(500);
        order1.setCanceled(false);
        order1.setInfo("Order Info 1");
        order1.setTax(20);
        order1.setTime(new Timestamp(System.currentTimeMillis()));

        try {
            orderDAO.create(order1);
        } catch (DAOException e) {
            fail("DAOException should not occur while adding a new user with a non-existing identity");
        }

        // check if user is stored
        assertEquals(1, orderDAO.find(order1).size());

        // GIVEN
        Order order2 = new Order();
        order2.setIdentity(order1.getIdentity());
        order2.setBrutto(600);
        order2.setCanceled(false);
        order2.setInfo("Order Info 2");
        order2.setTax(30);
        order2.setTime(new Timestamp(System.currentTimeMillis()));

        // WHEN
        orderDAO.create(order2);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();
        order.setBrutto(500);
        order.setCanceled(false);
        order.setInfo("Order Info");
        order.setTax(20);
        order.setTime(new Timestamp(System.currentTimeMillis()));

        // WHEN
        orderDAO.create(order);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutBruttoShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity("Order Identity");
        order.setCanceled(false);
        order.setInfo("Order Info");
        order.setTax(20);
        order.setTime(new Timestamp(System.currentTimeMillis()));

        // WHEN
        orderDAO.create(order);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutTaxShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity("Order Identity");
        order.setCanceled(false);
        order.setInfo("Order Info");
        order.setBrutto(20);
        order.setTime(new Timestamp(System.currentTimeMillis()));

        // WHEN
        orderDAO.create(order);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutInfoShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity("Order Identity");
        order.setCanceled(false);
        order.setBrutto(200);
        order.setTax(20);
        order.setTime(new Timestamp(System.currentTimeMillis()));

        // WHEN
        orderDAO.create(order);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutTimeShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity("Order Identity");
        order.setCanceled(false);
        order.setInfo("Order Info");
        order.setBrutto(20);
        order.setTax(20);

        // WHEN
        orderDAO.create(order);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutCanceledShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity("Order Identity");
        order.setInfo("Order Info");
        order.setBrutto(20);
        order.setTax(20);
        order.setTime(new Timestamp(System.currentTimeMillis()));

        // WHEN
        orderDAO.create(order);
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


    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithEmptyIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();
        order.setBrutto(500);
        order.setCanceled(false);
        order.setInfo("Order Info");
        order.setIdentity("");
        order.setTax(20);
        order.setTime(new Timestamp(System.currentTimeMillis()));

        // WHEN
        orderDAO.create(order);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithEmptyInfoShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity("Order Identity");
        order.setCanceled(false);
        order.setBrutto(200);
        order.setTax(20);
        order.setInfo("");
        order.setTime(new Timestamp(System.currentTimeMillis()));

        // WHEN
        orderDAO.create(order);
    }
}
