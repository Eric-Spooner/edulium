package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

import java.math.BigDecimal;

/**
 * validator implementation for the MenuEntry domain object
 */
class MenuEntryValidator implements Validator<MenuEntry> {
    @Override
    public void validateForCreate(MenuEntry menuEntry) throws ValidationException {
        if (menuEntry == null) {
            throw new ValidationException("menuEntry must not be null");
        }

        checkForRequiredDataAttributesForCreateAndUpdate(menuEntry);
    }

    @Override
    public void validateForUpdate(MenuEntry menuEntry) throws ValidationException {
        if (menuEntry == null) {
            throw new ValidationException("menuEntry must not be null");
        }

        validateIdentity(menuEntry);
        checkForRequiredDataAttributesForCreateAndUpdate(menuEntry);
    }

    @Override
    public void validateForDelete(MenuEntry menuEntry) throws ValidationException {
        if (menuEntry == null) {
            throw new ValidationException("menuEntry must not be null");
        }

        validateIdentity(menuEntry);
    }

    @Override
    public void validateIdentity(MenuEntry menuEntry) throws ValidationException {
        if (menuEntry == null) {
            throw new ValidationException("menuEntry must not be null");
        }
        if (menuEntry.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
    }

    private void checkForRequiredDataAttributesForCreateAndUpdate(MenuEntry menuEntry) throws ValidationException {
        if (menuEntry.getName() == null) {
            throw new ValidationException("value must not be null");
        }
        if (menuEntry.getPrice() == null) {
            throw new ValidationException("price must not be null");
        }
        if (menuEntry.getAvailable() == null) {
            throw new ValidationException("availability must not be null");
        }
        if (menuEntry.getDescription() == null) {
            throw new ValidationException("description must not be null");
        }
        if (menuEntry.getTaxRate() == null) {
            throw new ValidationException("taxRate must not be null");
        }
        if (menuEntry.getTaxRate().getIdentity() == null) {
            throw new ValidationException("taxRate identity must not be null");
        }
        if (menuEntry.getCategory() == null) {
            throw new ValidationException("category must not be null");
        }
        if (menuEntry.getCategory().getIdentity() == null) {
            throw new ValidationException("category identity must not be null");
        }
        if(menuEntry.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }
        if (menuEntry.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("value must be equal or greater than 0.0");
        }
    }
}
