package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

import java.math.BigDecimal;

/**
 * validator implementation for the Order domain object
 */
class OrderValidator implements Validator<Order> {
    @Override
    public void validateForCreate(Order order) throws ValidationException {
        if(order == null) {
            throw new ValidationException("order must not be null");
        }

        checkForRequiredDataAttributesForCreateAndUpdate(order);
    }

    @Override
    public void validateForUpdate(Order order) throws ValidationException {
        if(order == null) {
            throw new ValidationException("order must not be null");
        }
        validateIdentity(order);
        checkForRequiredDataAttributesForCreateAndUpdate(order);
    }

    @Override
    public void validateForDelete(Order order) throws ValidationException {
        if(order == null) {
            throw new ValidationException("order must not be null");
        }
        validateIdentity(order);
    }

    @Override
    public void validateIdentity(Order order) throws ValidationException {
        if(order == null) {
            throw new ValidationException("order must not be null");
        }
        if(order.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
    }


    private void checkForRequiredDataAttributesForCreateAndUpdate(Order order) throws ValidationException {
        if(order.getBrutto() == null) {
            throw new ValidationException("Brutto must not be null");
        }else if(order.getBrutto().equals(BigDecimal.valueOf(0))) {
            throw new ValidationException("Brutto must not be 0");
        }
        if(order.getTax() == null) {
            throw new ValidationException("Tax must not be null");
        }else if(order.getTax().equals(BigDecimal.valueOf(0))) {
            throw new ValidationException("Tax must not be null");
        }
        if(order.getMenuEntry() == null) {
            throw new ValidationException("Menu Entry must not be null");
        }else if(order.getMenuEntry().getIdentity() == null) {
            throw new ValidationException("Menu Entry must have an identity");
        }
        if(order.getTable() == null) {
            throw new ValidationException("Table must not be null");
        }else if(order.getTable().getSection() == null) {
            throw new ValidationException("Table must have an section");
        }
        if(order.getTime() == null) {
            throw new ValidationException("Time must not be null");
        }
    }
}
