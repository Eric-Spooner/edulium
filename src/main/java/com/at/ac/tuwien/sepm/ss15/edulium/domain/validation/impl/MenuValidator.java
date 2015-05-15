package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Menu;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

/**
 * validator implementation for the Menu domain object
 */
public class MenuValidator implements Validator<Menu> {
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
        }else if(object.getName() == ""){
            throw new ValidationException("Menu name must not be empty");
        }
        if(object.getEntries() == null){
            throw new ValidationException("Menu entries not be null");
        }else if(object.getEntries().size() == 0){
            throw new ValidationException("There should be at least one menu entry");
        }
    }
}
