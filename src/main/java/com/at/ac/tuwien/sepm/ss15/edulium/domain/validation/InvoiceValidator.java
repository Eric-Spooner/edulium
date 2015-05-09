package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;

/**
 * Validator for the Invoice domain object
 */
public interface InvoiceValidator {

    /**
     * Validates the invoice object for the create action
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    void validateForCreate(Invoice invoice) throws ValidationException;

    /**
     * Validates the invoice object for the update action
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    void validateForUpdate(Invoice invoice) throws ValidationException;

    /**
     * Validates the invoice object for the delete actions
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    void validateForDelete(Invoice invoice) throws ValidationException;

    /**
     * Validates if all parameters needed for identification are present
     * @param invoice Object to be validated
     * @throws ValidationException Thrown if the validation fails
     */
    void validateIdentity(Invoice invoice) throws ValidationException;
}
