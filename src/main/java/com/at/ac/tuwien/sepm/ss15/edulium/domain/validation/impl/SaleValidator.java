package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Sale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

/**
 * validator implementation for the Sale domain object
 */
public class SaleValidator implements Validator<Sale> {

    @Override
    public void validateForCreate(Sale sale) throws ValidationException {
        if (sale == null) {
            throw new ValidationException("sale must not be null");
        }

        validateIdentity(sale);
        checkForRequiredDataAttributesForCreateAndUpdate(sale);
    }

    @Override
    public void validateForUpdate(Sale sale) throws ValidationException {
        if (sale == null) {
            throw new ValidationException("sale must not be null");
        }

        validateIdentity(sale);
        checkForRequiredDataAttributesForCreateAndUpdate(sale);
    }

    @Override
    public void validateForDelete(Sale sale) throws ValidationException {
        if (sale == null) {
            throw new ValidationException("sale must not be null");
        }

        validateIdentity(sale);
    }

    @Override
    public void validateIdentity(Sale sale) throws ValidationException {
        if (sale == null) {
            throw new ValidationException("sale must not be null");
        }
        if (sale.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
    }

    private void checkForRequiredDataAttributesForCreateAndUpdate(Sale sale) throws ValidationException {
        if (sale.getName() == null) {
            throw new ValidationException("name must not be null");
        }
        if (sale.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }
    }
}
