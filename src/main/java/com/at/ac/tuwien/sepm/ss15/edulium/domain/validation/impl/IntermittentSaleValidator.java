package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.IntermittentSale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

/**
 * validator implementation for the IntermittentSale domain object
 */
public class IntermittentSaleValidator implements Validator<IntermittentSale> {
    @Override
    public void validateForCreate(IntermittentSale intermittentSale) throws ValidationException {
        if (intermittentSale == null) {
            throw new ValidationException("intermittentSale must not be null");
        }

        validateIdentity(intermittentSale);
        checkForRequiredDataAttributesForCreateAndUpdate(intermittentSale);
    }

    @Override
    public void validateForUpdate(IntermittentSale intermittentSale) throws ValidationException {
        if (intermittentSale == null) {
            throw new ValidationException("intermittentSale must not be null");
        }

        validateIdentity(intermittentSale);
        checkForRequiredDataAttributesForCreateAndUpdate(intermittentSale);
    }

    @Override
    public void validateForDelete(IntermittentSale intermittentSale) throws ValidationException {
        if (intermittentSale == null) {
            throw new ValidationException("intermittentSale must not be null");
        }

        validateIdentity(intermittentSale);
    }

    @Override
    public void validateIdentity(IntermittentSale intermittentSale) throws ValidationException {
        if (intermittentSale == null) {
            throw new ValidationException("intermittentSale must not be null");
        }
        if (intermittentSale.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
    }


    private void checkForRequiredDataAttributesForCreateAndUpdate(IntermittentSale intermittentSale) throws ValidationException {
        if (intermittentSale.getName() == null) {
            throw new ValidationException("name must not be null");
        }
        if (intermittentSale.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }
        if (intermittentSale.getEntries() == null){
            throw new ValidationException("Menu entries not be null");
        }
        if (intermittentSale.getEntries().isEmpty()){
            throw new ValidationException("There should be at least one menu entry");
        }
        if (intermittentSale.getFromDayTime() == null) {
            throw new ValidationException("FromDayTime must not be null");
        }
        if (intermittentSale.getEnabled() == null) {
            throw new ValidationException("Enabled must not be null");
        }
        if(intermittentSale.getDaysOfSale() == null) {
            throw new ValidationException("DaysOfSale must not be null");
        }
        if(intermittentSale.getDaysOfSale().isEmpty()) {
            throw new ValidationException("There should be at least one day of sale");
        }
        if (intermittentSale.getDuration() == null || intermittentSale.getDuration().isNegative()) {
            throw new ValidationException("Negative durations are not accepted.");
        }
    }
}