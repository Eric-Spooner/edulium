package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * validator implementation for the Menu domain object
 */
class MenuValidator implements Validator<Menu> {
    @Autowired
    private Validator<MenuEntry> menuEntryValidator;

    @Override
    public void validateForCreate(Menu object) throws ValidationException {
        if(object == null) {
            throw new ValidationException("Menu must not be null");
        }
        checkForRequiredDataAttributesForCreateAndUpdate(object);
    }

    @Override
    public void validateForUpdate(Menu object) throws ValidationException {
        if (object == null) {
            throw new ValidationException("Menu must not be null");
        }
        validateIdentity(object);
        checkForRequiredDataAttributesForCreateAndUpdate(object);
    }

    @Override
    public void validateForDelete(Menu object) throws ValidationException {
        if (object == null) {
            throw new ValidationException("Menu must not be null");
        }
        validateIdentity(object);
    }

    @Override
    public void validateIdentity(Menu object) throws ValidationException {
        if (object == null) {
            throw new ValidationException("Menu must not be null");
        }
        if (object.getIdentity() == null) {
            throw new ValidationException("Identity must not be null");
        }
    }

    private void checkForRequiredDataAttributesForCreateAndUpdate(Menu object) throws ValidationException {
        if(object.getName() == null){
            throw new ValidationException("Menu name must not be null");
        }
        if(object.getName() == ""){
            throw new ValidationException("Menu name must not be empty");
        }
        if(object.getEntries() == null){
            throw new ValidationException("Menu entries not be null");
        }
        if(object.getEntries().size() == 0){
            throw new ValidationException("There should be at least one menu entry");
        }
        for(MenuEntry entry:object.getEntries()){
            menuEntryValidator.validateForUpdate(entry);
        }
    }
}
