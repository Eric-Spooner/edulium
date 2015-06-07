package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.IntermittentSale;
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
        if (intermittentSale.getEntries().size() == 0){
            throw new ValidationException("There should be at least one menu entry");
        }
        if (intermittentSale.getFromDayTime() == null) {
            throw new ValidationException("FromDayTime must not be null");
        }
        if (intermittentSale.getEnabled() == null) {
            throw new ValidationException("Enabled must not be null");
        }
        if (intermittentSale.getMonday() == null) {
            throw new ValidationException("Monday must not be null");
        }
        if (intermittentSale.getTuesday() == null) {
            throw new ValidationException("Tuesday must not be null");
        }
        if (intermittentSale.getWednesday() == null) {
            throw new ValidationException("Wednesday must not be null");
        }
        if (intermittentSale.getThursday() == null) {
            throw new ValidationException("Thursday must not be null");
        }
        if (intermittentSale.getFriday() == null) {
            throw new ValidationException("Friday must not be null");
        }
        if (intermittentSale.getSaturday() == null) {
            throw new ValidationException("Saturday must not be null");
        }
        if (intermittentSale.getSunday() == null) {
            throw new ValidationException("Sunday must not be null");
        }
    }
}
