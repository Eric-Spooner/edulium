package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Test cases for the invoice validator
 */
public class TestInvoiceValidator extends AbstractDomainTest {

    @Autowired
    Validator<Invoice> invoiceValidator;

    @Test
    public void testValidateForCreate_shouldAcceptInvoice() throws ValidationException {
        User creator = User.withIdentity("testUser");

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("23"));
        invoice.setCreator(creator);
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullOrders() throws ValidationException {
        User creator = User.withIdentity("testUser");

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("23"));
        invoice.setCreator(creator);
        invoice.setSignature("signature");

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithEmptyOrders() throws ValidationException {
        User creator = User.withIdentity("testUser");

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("23"));
        invoice.setCreator(creator);
        invoice.setOrders(new ArrayList<>());
        invoice.setSignature("signature");

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullCreator() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("23"));
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithMissingCreatorIdentity() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("23"));
        invoice.setCreator(new User());
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullInvoice() throws ValidationException {
        invoiceValidator.validateForCreate(null);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithoutSignature() throws ValidationException {
        User creator = User.withIdentity("TestUser");

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("20"));
        invoice.setIdentity(1L);
        invoice.setCreator(creator);
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithEmptySignature() throws ValidationException {
        User creator = User.withIdentity("TestUser");

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("20"));
        invoice.setIdentity(1L);
        invoice.setCreator(creator);
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("");

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullTime() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setGross(new BigDecimal("23"));
        invoice.setCreator(User.withIdentity("TestUser"));
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullGrossAmount() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setCreator(User.withIdentity("TestUser"));
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNegativeGrossAmount() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("-22"));
        invoice.setCreator(User.withIdentity("TestUser"));
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForCreate(invoice);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptInvoice() throws ValidationException {
        User creator = User.withIdentity("TestUser");

        Invoice invoice = new Invoice();
        invoice.setIdentity(1L);
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15"));
        invoice.setCreator(creator);
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithNullOrders() throws ValidationException {
        User creator = User.withIdentity("TestUser");

        Invoice invoice = new Invoice();
        invoice.setIdentity(1L);
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15"));
        invoice.setCreator(creator);
        invoice.setSignature("signature");

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithEmptyOrders() throws ValidationException {
        User creator = User.withIdentity("TestUser");

        Invoice invoice = new Invoice();
        invoice.setIdentity(1L);
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15"));
        invoice.setCreator(creator);
        invoice.setOrders(new ArrayList<>());
        invoice.setSignature("signature");

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithNullCreator() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setGross(new BigDecimal("20"));
        invoice.setTime(LocalDateTime.now());
        invoice.setIdentity(1L);
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithNullTime() throws ValidationException {

        User creator = User.withIdentity("TestUser");
        Invoice invoice = new Invoice();
        invoice.setGross(new BigDecimal("20"));
        invoice.setIdentity(1L);
        invoice.setCreator(creator);
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithNullGrossAmount() throws ValidationException {
        User creator = User.withIdentity("TestUser");

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setIdentity(1L);
        invoice.setCreator(creator);
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithNegativeGrossAmount() throws ValidationException {
        User creator = User.withIdentity("TestUser");

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("-20"));
        invoice.setIdentity(1L);
        invoice.setCreator(creator);
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("signature");

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithMissingUserIdentity() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setIdentity(1L);
        invoice.setGross(new BigDecimal("19"));
        invoice.setCreator(new User());
        invoice.setSignature("signature");

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithMissingSignature() throws ValidationException {
        User creator = User.withIdentity("TestUser");

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("20"));
        invoice.setIdentity(1L);
        invoice.setCreator(creator);
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithEmptySignature() throws ValidationException {
        User creator = User.withIdentity("TestUser");

        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("20"));
        invoice.setIdentity(1L);
        invoice.setCreator(creator);
        invoice.setOrders(Arrays.asList(Order.withIdentity(10L)));
        invoice.setSignature("");

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithNoInvoice() throws ValidationException {
        invoiceValidator.validateForUpdate(null);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithNullIdentity() throws ValidationException {
        Invoice invoice = new Invoice();

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test
    public void testValidateForDelete_shouldAcceptInvoice() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setIdentity(1L);

        invoiceValidator.validateForDelete(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_shouldFailWithNullInvoice() throws ValidationException {
        invoiceValidator.validateForDelete(null);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_shouldFailWithNullIdentity() throws ValidationException {
        Invoice invoice = new Invoice();

        invoiceValidator.validateForDelete(invoice);
    }
}
