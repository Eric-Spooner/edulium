package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;

/**
 * Validator for the Table domain object
 */
public interface TableValidator {

    /**
     * validates the object for the create action
     * @param table object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForCreate(Table table) throws ValidationException;

    /**
     * validates the object for the update action
     * @param table object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForUpdate(Table table) throws ValidationException;

    /**
     * validates the object for the delete action
     * @param table object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForDelete(Table table) throws ValidationException;
}
