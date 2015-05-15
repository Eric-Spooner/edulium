package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

import java.math.BigDecimal;

public class InvoiceValidatorImpl implements Validator<Invoice> {

    /**
     * Validates the invoice object for the create action
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    @Override
    public void validateForCreate(Invoice invoice) throws ValidationException {
        if (invoice == null) {
            throw new ValidationException("Object must not be null");
        }

        if (invoice.getTime() == null || invoice.getGross() == null) {
            throw  new ValidationException("Time and gross must not be null");
        }

        if (invoice.getGross().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("The total gross amount cannot be negative");
        }

        if (invoice.getPaid() != null && invoice.getPaid().compareTo(BigDecimal.ZERO) != 0) {
            throw new ValidationException("The amount that was already paid can only " +
                    "be 0 upon creation");
        }

        if (invoice.getCreator() == null) {
            throw new ValidationException("Upon creation, the creator of the invoice " +
                    "must be provided");
        }
    }

    /**
     * Validates the invoice object for the update action
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    @Override
    public void validateForUpdate(Invoice invoice) throws ValidationException {
        if (invoice == null) {
            throw new ValidationException("Object must not be null");
        }

        if (invoice.getPaid() != null && invoice.getPaid().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("The amount that was already paid cannot be negative");
        }

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
}
