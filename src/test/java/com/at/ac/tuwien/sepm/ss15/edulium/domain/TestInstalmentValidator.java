package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ImmutableValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestInstalmentValidator extends AbstractDomainTest {

    @Autowired
    ImmutableValidator<Instalment> instalmentValidator;

    @Test
    public void testValidateForCreate_shouldAcceptInstalment() throws ValidationException{
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentValidator.validateForCreate(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullInstalment() throws ValidationException {
        instalmentValidator.validateForCreate(null);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullInvoice() throws ValidationException {
        Instalment instalment = new Instalment();
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentValidator.validateForCreate(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithMissingInvoiceIdentity() throws ValidationException {
        Instalment instalment = new Instalment();
        instalment.setInvoice(new Invoice());
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentValidator.validateForCreate(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullAmount() throws ValidationException {
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setPaymentInfo("Payment info");

        instalmentValidator.validateForCreate(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNegativeAmount() throws ValidationException {
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("-22"));
        instalment.setPaymentInfo("Payment info");

        instalmentValidator.validateForCreate(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithZeroAmount() throws ValidationException {
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("0"));
        instalment.setPaymentInfo("Payment info");

        instalmentValidator.validateForCreate(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullType() throws ValidationException {
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
        instalment.setTime(LocalDateTime.now());
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentValidator.validateForCreate(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithMissingType() throws ValidationException {
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentValidator.validateForCreate(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullTime() throws ValidationException {
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));
        instalment.setPaymentInfo("Payment info");

        instalmentValidator.validateForCreate(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_shouldFailWithNullPaymentInfo() throws ValidationException {
        Instalment instalment = new Instalment();
        instalment.setInvoice(Invoice.withIdentity(1L));
        instalment.setTime(LocalDateTime.now());
        instalment.setType("CASH");
        instalment.setAmount(new BigDecimal("22"));

        instalmentValidator.validateForCreate(instalment);
    }

    @Test
    public void testValidateIdentity_shouldAcceptInstalment() throws ValidationException {
        Instalment instalment = new Instalment();
        instalment.setIdentity(1L);

        instalmentValidator.validateIdentity(instalment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_shouldFailWithNullInstalment() throws ValidationException {
        instalmentValidator.validateIdentity(null);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_shouldFailWithNullIdentity() throws ValidationException {
        Instalment instalment = new Instalment();

        instalmentValidator.validateIdentity(instalment);
    }
}
