package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

/**
 * validator implementation for the MenuCategory domain object
 */
public class MenuCategoryValidator implements Validator<MenuCategory> {
    private final int NAME_MAX_LENGTH = 100;
    /**
     * validates the object for the create action
     * @param menuCategory object to validate
     * @throws ValidationException if name is null or empty
     */
    @Override
    public void validateForCreate(MenuCategory menuCategory) throws ValidationException {
        if(menuCategory == null) {
            throw new ValidationException("object must not be null");
        }

        checkForRequiredDataAttributesForCreateAndUpdate(menuCategory);
    }

    /**
     * validates the object for the update action
     * @param menuCategory object to validate
     * @throws ValidationException if identity is null
     */
    @Override
    public void validateForUpdate(MenuCategory menuCategory) throws ValidationException {
        if(menuCategory == null) {
            throw new ValidationException("object must not be null");
        }

        validateIdentity(menuCategory);
        checkForRequiredDataAttributesForCreateAndUpdate(menuCategory);
    }

    /**
     * validates the object for the delete action
     * @param menuCategory object to validate
     * @throws ValidationException if identity is null
     */
    @Override
    public void validateForDelete(MenuCategory menuCategory) throws ValidationException {
        if(menuCategory == null) {
            throw new ValidationException("object must not be null");
        }
        validateIdentity(menuCategory);
    }

    /**
     * validates if the identity parameter is set
     * @param menuCategory object to validate
     * @throws ValidationException if the identity of the object is not set
     */
    @Override
    public void validateIdentity(MenuCategory menuCategory) throws ValidationException {
        if(menuCategory == null) {
            throw new ValidationException("object must not be null");
        }
        if(menuCategory.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
    }

    private void checkForRequiredDataAttributesForCreateAndUpdate(MenuCategory menuCategory) throws ValidationException {
        if(menuCategory.getName() == null) {
            throw new ValidationException("name must not be null");
        }
        if(menuCategory.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }
        if (menuCategory.getName().length() > NAME_MAX_LENGTH) {
            throw new ValidationException("name must not be longer than " + NAME_MAX_LENGTH + " characters");
        }
    }
}