package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * validator implementation for the MenuEntry domain object
 */
class MenuEntryValidator implements Validator<MenuEntry> {
    @Resource(name = "menuCategoryValidator")
    private Validator<MenuCategory> menuCategoryValidator;
    @Resource(name = "taxRateValidator")
    private Validator<TaxRate> taxRateValidator;

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
        if (menuEntry.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }
        if (menuEntry.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("value must be equal or greater than 0.0");
        }

        menuCategoryValidator.validateIdentity(menuEntry.getCategory());
        taxRateValidator.validateIdentity(menuEntry.getTaxRate());
    }
}
