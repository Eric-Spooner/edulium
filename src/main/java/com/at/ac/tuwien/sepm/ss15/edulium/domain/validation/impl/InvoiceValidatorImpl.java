package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

class InvoiceValidatorImpl implements Validator<Invoice> {

    @Autowired
    private Validator<User> userValidator;

    /**
     * Validates the invoice object for the create action
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    @Override
    public void validateForCreate(Invoice invoice) throws ValidationException {
        checkForRequiredAttributesForCreateAndUpdate(invoice);
    }

    /**
     * Validates the invoice object for the update action
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    @Override
    public void validateForUpdate(Invoice invoice) throws ValidationException {
        checkForRequiredAttributesForCreateAndUpdate(invoice);
        validateIdentity(invoice);
    }

    /**
     * Validates the invoice object for the delete actions
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    @Override
    public void validateForDelete(Invoice invoice) throws ValidationException {
        if (invoice == null) {
            throw new ValidationException("Object must not be null");
        }

        validateIdentity(invoice);
    }

    /**
     * Validates if all parameters needed for identification are present
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    @Override
    public void validateIdentity(Invoice invoice) throws ValidationException {
        if (invoice.getIdentity() == null) {
            throw new ValidationException("The unique identity of the invoice must be " +
                    "present in order to be identified");
        }
    }

    private void checkForRequiredAttributesForCreateAndUpdate(Invoice invoice) throws ValidationException {
        if (invoice == null) {
            throw new ValidationException("Object must not be null");
        }

        if (invoice.getTime() == null) {
            throw  new ValidationException("Time cannot be null");
        }

        if (invoice.getGross() == null) {
            throw new ValidationException("Gross cannot be null");
        }

        if (invoice.getGross().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("The total gross amount cannot be negative");
        }

        userValidator.validateIdentity(invoice.getCreator());
    }
}
