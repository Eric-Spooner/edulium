package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

/**
 * validator implementation for the MenuCategory domain object
 */
public class MenuCategoryValidator implements Validator<MenuCategory> {

    @Override
    public void validateForCreate(MenuCategory menuCategory) throws ValidationException {
        if(menuCategory == null) {
            throw new ValidationException("object must not be null");
        }

        checkForRequiredDataAttributesForCreateAndUpdate(menuCategory);
    }

    @Override
    public void validateForUpdate(MenuCategory menuCategory) throws ValidationException {
        if(menuCategory == null) {
            throw new ValidationException("object must not be null");
        }

        validateIdentity(menuCategory);
        checkForRequiredDataAttributesForCreateAndUpdate(menuCategory);
    }

    @Override
    public void validateForDelete(MenuCategory menuCategory) throws ValidationException {
        if(menuCategory == null) {
            throw new ValidationException("object must not be null");
        }
        validateIdentity(menuCategory);
    }

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
    }
}
