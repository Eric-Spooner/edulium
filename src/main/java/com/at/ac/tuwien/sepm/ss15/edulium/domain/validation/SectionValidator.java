package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;

/**
 * Validator for the Section domain object
 */
public interface SectionValidator {

    /**
     * validates the object for the create action
     * @param section object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForCreate(Section section) throws ValidationException;

    /**
     * validates the object for the update action
     * @param section object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForUpdate(Section section) throws ValidationException;

    /**
     * validates the object for the delete action
     * @param section object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForDelete(Section section) throws ValidationException;
}
