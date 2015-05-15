package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for the invoice validator
 */
public class TestInvoiceValidator extends AbstractDomainTest {

    @Autowired
    Validator<Invoice> invoiceValidator;

    @Test
    public void testValidateForCreate_shouldAcceptInvoice() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("23"));
        invoice.setCreator(new User());

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullCreator() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("23"));

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullInvoice() throws ValidationException {
        Invoice invoice = null;

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullGrossAmount() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNegativeGrossAmount() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("-22"));

        invoiceValidator.validateForCreate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithPaidAmount() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("15"));
        invoice.addPaid(new BigDecimal("16"));

        invoiceValidator.validateForCreate(invoice);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptInvoice() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setIdentity(1L);
        invoice.addPaid(new BigDecimal("12"));

        Installment in1 = new Installment();
        List<Installment> inList = new ArrayList<>();
        inList.add(in1);

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithNoInvoice() throws ValidationException {
        Invoice invoice = null;

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithNullIdentity() throws ValidationException {
        Invoice invoice = new Invoice();

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWithNegativePaidAmount() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setIdentity(1L);
        invoice.addPaid(new BigDecimal("-19"));

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
        Invoice invoice = null;

        invoiceValidator.validateForDelete(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_shouldFailWithNullIdentity() throws ValidationException {
        Invoice invoice = new Invoice();

        invoiceValidator.validateForDelete(invoice);
    }
}
