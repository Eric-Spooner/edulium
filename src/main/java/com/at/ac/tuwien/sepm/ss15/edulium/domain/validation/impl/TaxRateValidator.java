package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

import java.math.BigDecimal;

/**
 * validator implementation for the TaxRate domain object
 */
class TaxRateValidator implements Validator<TaxRate> {
    @Override
    public void validateForCreate(TaxRate taxRate) throws ValidationException {
        if (taxRate == null) {
            throw new ValidationException("taxRate must not be null");
        }

        checkForRequiredDataAttributesForCreateAndUpdate(taxRate);
    }

    @Override
    public void validateForUpdate(TaxRate taxRate) throws ValidationException {
        if (taxRate == null) {
            throw new ValidationException("taxRate must not be null");
        }

        validateIdentity(taxRate);
        checkForRequiredDataAttributesForCreateAndUpdate(taxRate);
    }

    @Override
    public void validateForDelete(TaxRate taxRate) throws ValidationException {
        if (taxRate == null) {
            throw new ValidationException("taxRate must not be null");
        }

        validateIdentity(taxRate);
    }

    @Override
    public void validateIdentity(TaxRate taxRate) throws ValidationException {
        if (taxRate == null) {
            throw new ValidationException("taxRate must not be null");
        }
        if (taxRate.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
    }

    private void checkForRequiredDataAttributesForCreateAndUpdate(TaxRate taxRate) throws ValidationException {
        if (taxRate.getValue() == null) {
            throw new ValidationException("value must not be null");
        }
        if (taxRate.getValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("value must be equal or greater than 0.0");
        }
        if (taxRate.getValue().compareTo(BigDecimal.ONE) > 0) {
            throw new ValidationException("value must be equal or smaller than 1.0");
        }
    }
}
