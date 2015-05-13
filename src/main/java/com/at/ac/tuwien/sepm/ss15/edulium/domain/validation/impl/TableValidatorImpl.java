package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.TableValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

/**
 * implementation of the TableValidator
 */
public class TableValidatorImpl implements TableValidator {

    /**
     * validates the object for the create action
     * @param table object to validate
     * @throws com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException
     */
    @Override
    public void validateForCreate(Table table) throws ValidationException {

    }

    /**
     * validates the object for the update action
     * @param table object to validate
     * @throws ValidationException if number is null
     */
    @Override
    public void validateForUpdate(Table table) throws ValidationException {
        if(table.getNumber() == null) {
            throw new ValidationException("number must not be null");
        }
    }

    /**
     * validates the object for the delete action
     * @param table object to validate
     * @throws ValidationException if number is null
     */
    @Override
    public void validateForDelete(Table table) throws ValidationException {
        if(table.getNumber() == null) {
            throw new ValidationException("number must not be null");
        }
    }
}
