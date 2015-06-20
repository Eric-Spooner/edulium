package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Instalment;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ImmutableValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

import javax.annotation.Resource;
import java.math.BigDecimal;

class InstalmentValidator implements ImmutableValidator<Instalment> {

    @Resource(name = "invoiceValidator")
    private Validator<Invoice> invoiceValidator;

    /**
     * Validates the instalment object for the create action
     * @param object Instalment object to validate
     * @throws ValidationException Thrown if the validation fails
     */
    @Override
    public void validateForCreate(Instalment object) throws ValidationException {
        checkIfNull(object);

        if (object.getTime() == null) {
            throw new ValidationException("Time cannot be null");
        }

        if (object.getAmount() == null) {
            throw new ValidationException("Amount cannot be null");
        }

        if (object.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be bigger than 0");
        }

        if (object.getPaymentInfo() == null) {
            throw new ValidationException("Payment info must not be null");
        }

        if (object.getType() == null) {
            throw new ValidationException("Type cannot be null");
        }

        if (object.getType().equals("")) {
            throw new ValidationException("Type must be provided");
        }

        invoiceValidator.validateIdentity(object.getInvoice());
    }

    /**
     * Validates if all parameters needed for identification are present
     * @param object Instalment object to validate
     * @throws ValidationException Thrown if the validation fails
     */
    @Override
    public void validateIdentity(Instalment object) throws ValidationException {
        checkIfNull(object);
        if (object.getIdentity() == null) {
            throw new ValidationException("The unique identity of the instalment" +
                    "must be present in order to be identified");
        }
    }

    private void checkIfNull(Instalment object) throws ValidationException {
        if (object == null) {
            throw new ValidationException("Object must not be null");
        }
    }
}
