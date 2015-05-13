package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Unit Test for the TaxRate validator
 */
public class TestOrderValidator extends AbstractDomainTest {
    @Autowired
    private Validator<Order> orderValidator;

    @Test
    public void testValidateForCreate_shouldAcceptOrder() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("");

        // WHEN
        orderValidator.validateForCreate(order);
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
         public void testValidateForCreate_OrderWithoutAdditionalInfoShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutOrderEntriesShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutTableShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setMenueEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
         public void testValidateForCreate_OrderWithoutBruttoShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutTaxShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutTimeShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setAdditionalInformation("Info");
        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptOrder() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity((long) 1);
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");



        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_OrderWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setMenueEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");


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
