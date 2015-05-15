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
    }

    @Override
    public void validateForUpdate(Menu object) throws ValidationException {
    }

    @Override
    public void validateForDelete(Menu object) throws ValidationException {
    }

    @Override
    public void validateIdentity(Menu object) throws ValidationException {
    }
}
