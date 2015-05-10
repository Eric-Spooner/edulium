package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Unit Test for the TaxRate validator
 */
public class TestTaxRateValidator extends AbstractDomainTest {
    @Autowired
    private Validator<TaxRate> taxRateValidator;

    @Test
    public void testValidateForCreate_shouldAcceptTaxRate() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(20.25));

        // WHEN
        taxRateValidator.validateForCreate(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_taxRateWithoutValueShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateValidator.validateForCreate(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = null;

        // WHEN
        taxRateValidator.validateForCreate(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_taxRateWithNegativeValueShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(-1.0));

        // WHEN
        taxRateValidator.validateForCreate(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_taxRateWithValueLargerThan100ShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(101.0));

        // WHEN
        taxRateValidator.validateForCreate(taxRate);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptTaxRate() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setIdentity(0L);
        taxRate.setValue(BigDecimal.valueOf(20.25));

        // WHEN
        taxRateValidator.validateForUpdate(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_taxRateWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(20.25));

        // WHEN
        taxRateValidator.validateForUpdate(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_taxRateWithoutValueShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setIdentity(0L);

        // WHEN
        taxRateValidator.validateForUpdate(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = null;

        // WHEN
        taxRateValidator.validateForUpdate(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_taxRateWithNegativeValueShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setIdentity(0L);
        taxRate.setValue(BigDecimal.valueOf(-1.0));

        // WHEN
        taxRateValidator.validateForUpdate(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_taxRateWithValueLargerThan100ShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setIdentity(0L);
        taxRate.setValue(BigDecimal.valueOf(101.0));

        // WHEN
        taxRateValidator.validateForUpdate(taxRate);
    }

    @Test
    public void testValidateForDelete_shouldAcceptTaxRate() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setIdentity(0L);

        // WHEN
        taxRateValidator.validateForDelete(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_taxRateWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateValidator.validateForDelete(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = null;

        // WHEN
        taxRateValidator.validateForDelete(taxRate);
    }

    @Test
    public void testValidateForIdentity_shouldAcceptTaxRate() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setIdentity(0L);

        // WHEN
        taxRateValidator.validateIdentity(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForIdentity_taxRateWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateValidator.validateIdentity(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        TaxRate taxRate = null;

        // WHEN
        taxRateValidator.validateIdentity(taxRate);
    }
}
