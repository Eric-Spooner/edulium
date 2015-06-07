package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.OnetimeSale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

/**
 * validator implementation for the OnetimeSale domain object
 */
public class OnetimeSaleValidator implements Validator<OnetimeSale> {
    @Override
    public void validateForCreate(OnetimeSale onetimeSale) throws ValidationException {
        if (onetimeSale == null) {
            throw new ValidationException("onetimeSale must not be null");
        }

        validateIdentity(onetimeSale);
        checkForRequiredDataAttributesForCreateAndUpdate(onetimeSale);
    }

    @Override
    public void validateForUpdate(OnetimeSale onetimeSale) throws ValidationException {
        if (onetimeSale == null) {
            throw new ValidationException("onetimeSale must not be null");
        }

        validateIdentity(onetimeSale);
        checkForRequiredDataAttributesForCreateAndUpdate(onetimeSale);
    }

    @Override
    public void validateForDelete(OnetimeSale onetimeSale) throws ValidationException {
        if (onetimeSale == null) {
            throw new ValidationException("onetimeSale must not be null");
        }

        validateIdentity(onetimeSale);
    }

    @Override
    public void validateIdentity(OnetimeSale onetimeSale) throws ValidationException {
        if (onetimeSale == null) {
            throw new ValidationException("onetimeSale must not be null");
        }
        if (onetimeSale.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
    }


    private void checkForRequiredDataAttributesForCreateAndUpdate(OnetimeSale onetimeSale) throws ValidationException {
        if (onetimeSale.getName() == null) {
            throw new ValidationException("name must not be null");
        }
        if (onetimeSale.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }
        if (onetimeSale.getEntries() == null){
            throw new ValidationException("Menu entries not be null");
        }
        if (onetimeSale.getEntries().size() == 0){
            throw new ValidationException("There should be at least one menu entry");
        }
        if (onetimeSale.getFromTime() == null) {
            throw new ValidationException("FromTime must not be null");
        }
        if (onetimeSale.getToTime() == null) {
            throw new ValidationException("ToTime must not be null");
        }
        if (onetimeSale.getFromTime().compareTo(onetimeSale.getToTime()) > 0) {
            throw new ValidationException("FromTime must not be before toTime.");
        }
    }
}
