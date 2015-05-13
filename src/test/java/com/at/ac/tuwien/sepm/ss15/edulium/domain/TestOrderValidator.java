package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.LinkedList;

/**
 * Unit Test for the TaxRate validator
 */
public class TestOrderValidator extends AbstractDomainTest {
    @Autowired
    private Validator<Order> orderValidator;

    @Test
    public void testValidateForCreate_shouldAcceptMenu() throws ValidationException {
        // GIVEN

        // WHEN

    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutValueShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Order order = null;

        // WHEN
        orderValidator.validateForCreate(order);
    }


    @Test(expected = ValidationException.class)
         public void testValidateForCreate_OrderWithoutInfoShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setBrutto(0);
        order.setTax(0);
        order.setCanceled(false);
        order.setTime(new Timestamp(System.currentTimeMillis()));

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutOrderEntriesShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setBrutto(0);
        order.setTax(0);
        order.setCanceled(false);
        order.setTime(new Timestamp(System.currentTimeMillis()));
        order.setInfo("Info");

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutTableShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setMenueEntry(new MenuEntry());
        order.setBrutto(0);
        order.setTax(0);
        order.setCanceled(false);
        order.setTime(new Timestamp(System.currentTimeMillis()));
        order.setInfo("Info");

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
         public void testValidateForCreate_OrderWithoutBruttoShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setTax(0);
        order.setCanceled(false);
        order.setTime(new Timestamp(System.currentTimeMillis()));
        order.setInfo("Info");

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutTaxShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setBrutto(0);
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setCanceled(false);
        order.setTime(new Timestamp(System.currentTimeMillis()));
        order.setInfo("Info");

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutTimeShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setBrutto(0);
        order.setTax(0);
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setCanceled(false);
        order.setInfo("Info");

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithCanceledTrueShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setBrutto(0);
        order.setTax(0);
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setCanceled(true);
        order.setTime(new Timestamp(System.currentTimeMillis()));
        order.setInfo("Info");

        // WHEN
        orderValidator.validateForCreate(order);
    }


    @Test
    public void testValidateForUpdate_shouldAcceptOrder() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity((long)1);
        order.setBrutto(0);
        order.setTax(0);
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setCanceled(true);
        order.setTime(new Timestamp(System.currentTimeMillis()));
        order.setInfo("Info");



        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_OrderWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setBrutto(0);
        order.setTax(0);
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setCanceled(true);
        order.setTime(new Timestamp(System.currentTimeMillis()));
        order.setInfo("Info");


        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Order order = null;

        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test
    public void testValidateForDelete_shouldAcceptOrder() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity((long)1);

        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_OrderWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();

        // WHEN
        orderValidator.validateForDelete(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Order order = null;

        // WHEN
        orderValidator.validateForDelete(order);
    }

    @Test
    public void testValidateIdentity_shouldAcceptOrder() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity(0L);

        // WHEN
        orderValidator.validateIdentity(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_OrderWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();

        // WHEN
        orderValidator.validateIdentity(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Order order =  null;

        // WHEN
        orderValidator.validateIdentity(order);
    }
}
