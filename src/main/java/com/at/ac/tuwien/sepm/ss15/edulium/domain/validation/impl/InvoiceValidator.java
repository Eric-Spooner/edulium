package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

import javax.annotation.Resource;
import java.math.BigDecimal;

class InvoiceValidator implements Validator<Invoice> {

    @Resource(name = "userValidator")
    private Validator<User> userValidator;

    @Resource(name = "orderValidator")
    private Validator<Order> orderValidator;

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
        validateIdentity(invoice);
    }

    /**
     * Validates if all parameters needed for identification are present
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    @Override
    public void validateIdentity(Invoice invoice) throws ValidationException {
        checkIfNull(invoice);
        if (invoice.getIdentity() == null) {
            throw new ValidationException("The unique identity of the invoice must be " +
                    "present in order to be identified");
        }
    }

    private void checkIfNull(Invoice invoice) throws ValidationException {
        if (invoice == null) {
            throw new ValidationException("Object must not be null");
        }
    }

    private void checkForRequiredAttributesForCreateAndUpdate(Invoice invoice) throws ValidationException {
        checkIfNull(invoice);

        if (invoice.getTime() == null) {
            throw  new ValidationException("Time cannot be null");
        }

        if (invoice.getGross() == null) {
            throw new ValidationException("Gross cannot be null");
        }

        if (invoice.getGross().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("The total gross amount cannot be negative");
        }

        if (invoice.getSignature() == null) {
            throw new ValidationException("Signature cannot be null");
        }

        if (invoice.getSignature().isEmpty()) {
            throw new ValidationException("Signature cannot be empty");
        }

        if (invoice.getOrders() == null) {
            throw new ValidationException("Orders cannot be null");
        }

        if (invoice.getOrders().isEmpty()) {
            throw new ValidationException("Orders must contain at least one order");
        }

        for (Order order : invoice.getOrders()) {
            orderValidator.validateIdentity(order);
        }

        userValidator.validateIdentity(invoice.getCreator());
    }
}
