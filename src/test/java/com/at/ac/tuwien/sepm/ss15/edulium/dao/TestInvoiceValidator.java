package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Installment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for the invoice validator
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:Spring-Domain.xml")
public class TestInvoiceValidator {

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
    public void testValidateForCreate_shouldFailWithProvidedInstallments() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setGross(new BigDecimal("23"));
        invoice.setCreator(new User());

        Installment in1 = new Installment();
        Installment in2 = new Installment();
        List<Installment> inList = new ArrayList<Installment>();
        inList.add(in1);
        inList.add(in2);
        invoice.setInstallments(inList);

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
        invoice.setPaid(new BigDecimal("16"));

        invoiceValidator.validateForCreate(invoice);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptInvoice() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setIdentity(1L);
        invoice.setPaid(new BigDecimal("12"));

        Installment in1 = new Installment();
        List<Installment> inList = new ArrayList<Installment>();
        inList.add(in1);
        invoice.setInstallments(inList);

        invoiceValidator.validateForUpdate(invoice);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_shouldFailWhenInstallmentsAreMissing() throws ValidationException {
        Invoice invoice = new Invoice();
        invoice.setIdentity(1L);
        invoice.setPaid(new BigDecimal("12"));

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
        invoice.setPaid(new BigDecimal("-19"));

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
