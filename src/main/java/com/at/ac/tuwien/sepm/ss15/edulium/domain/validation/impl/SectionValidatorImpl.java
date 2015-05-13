package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.SectionValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

/**
 * implementation of the SectionValidator
 */
public class SectionValidatorImpl implements SectionValidator {

    /**
     * validates the object for the create action
     * @param section object to validate
     * @throws ValidationException if name is null or empty
     */
    @Override
    public void validateForCreate(Section section) throws ValidationException {
        if(section.getName() == null) {
            throw new ValidationException("name must not be null");
        }
        if(section.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }
    }

    /**
     * validates the object for the update action
     * @param section object to validate
     * @throws ValidationException if identity is null
     */
    @Override
    public void validateForUpdate(Section section) throws ValidationException {
        if(section.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
    }

    /**
     * validates the object for the delete action
     * @param section object to validate
     * @throws ValidationException if identity is null
     */
    @Override
    public void validateForDelete(Section section) throws ValidationException {
        if(section.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
    }
}