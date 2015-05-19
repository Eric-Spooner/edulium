package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Invoice;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuEntry;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * validator implementation for the Order domain object
 */
class OrderValidator implements Validator<Order> {
    @Autowired
    private Validator<Table> tableValidator;
    @Autowired
    private Validator<MenuEntry> menuEntryValidator;
    @Autowired
    private Validator<Invoice> invoiceValidator;


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
        }
        if(order.getBrutto().equals(BigDecimal.valueOf(0))) {
            throw new ValidationException("Brutto must not be 0");
        }
        if(order.getTax() == null) {
            throw new ValidationException("Tax must not be null");
        }
        if(order.getTax().compareTo(BigDecimal.valueOf(0)) == -1 || order.getTax().compareTo(BigDecimal.valueOf(1)) == 1) {
            throw new ValidationException("Tax must be between 0 (included) and 1 (included)");
        }

        if(order.getTime() == null) {
            throw new ValidationException("Time must not be null");
        }
        menuEntryValidator.validateForUpdate(order.getMenuEntry());
        tableValidator.validateForUpdate(order.getTable());

    }
}
