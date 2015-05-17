package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

/**
 * implementation of the TableValidator
 */
public class TableValidator implements Validator<Table> {

    /**
     * validates the object for the create action
     * @param table object to validate
     * @throws com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException
     */
    @Override
    public void validateForCreate(Table table) throws ValidationException {
        checkForRequiredDataAttributesForCreateAndUpdate(table);
    }

    /**
     * validates the object for the update action
     * @param table object to validate
     * @throws ValidationException if number is null
     */
    @Override
    public void validateForUpdate(Table table) throws ValidationException {
        checkForRequiredDataAttributesForCreateAndUpdate(table);
    }

    /**
     * validates the object for the delete action
     * @param table object to validate
     * @throws ValidationException if number is null
     */
    @Override
    public void validateForDelete(Table table) throws ValidationException {
        if(table == null) {
            throw new ValidationException("table must not be null");
        }

        if(table.getNumber() == null) {
            throw new ValidationException("number must not be null");
        }

        if(table.getSection() == null) {
            throw new ValidationException("section must not be null");
        }

        if(table.getSection().getIdentity() == null) {
            throw new ValidationException("section identity must not be null");
        }

        if(table.getColumn() != null && table.getColumn() < 0) {
            throw new ValidationException("column must not be < 0");
        }

        if(table.getRow() != null && table.getRow() < 0) {
            throw new ValidationException("row must not be < 0");
        }

        if(table.getSeats() != null && table.getSeats() < 0) {
            throw new ValidationException("seats must not be < 0");
        }
    }

    /**
     * validates if the identity parameters are set
     * @param table object to validate
     * @throws ValidationException if the identity of the object is not set
     */
    @Override
    public void validateIdentity(Table table) throws ValidationException {
        if(table == null) {
            throw new ValidationException("object must not be null");
        }

        if(table.getNumber() == null) {
            throw new ValidationException("number must not be null");
        }

        if(table.getNumber() < 0) {
            throw new ValidationException("number must not be < 0");
        }

        if(table.getSection() == null) {
            throw new ValidationException("section must not be null");
        }

        if(table.getSection().getIdentity() == null) {
            throw new ValidationException("section identity must not be null");
        }
    }

    /**
     * validates if the data attribute parameters are set
     * @param table
     * @throws ValidationException
     */
    private void checkForRequiredDataAttributesForCreateAndUpdate(Table table) throws ValidationException {
        validateIdentity(table);

        if(table.getColumn() == null) {
            throw new ValidationException("column must not be null");
        }

        if(table.getColumn() < 0) {
            throw new ValidationException("column must not be < 0");
        }

        if(table.getRow() == null) {
            throw new ValidationException("row must not be null");
        }

        if(table.getRow() < 0) {
            throw new ValidationException("row must not be < 0");
        }

        if(table.getSeats() == null) {
            throw new ValidationException("seats must not be null");
        }

        if(table.getSeats() < 0) {
            throw new ValidationException("seats must not be < 0");
        }

        if(table.getUser() != null && table.getUser().getIdentity() == null) {
            throw new ValidationException("user identity must not be null");
        }
    }
}
