package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Order;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

import javax.xml.bind.ValidationEvent;

/**
 * Created by - on 13.05.2015.
 */
public class OrderValidator implements Validator<Order> {
    @Override
    public void validateForCreate(Order object) throws ValidationException {

    }

    @Override
    public void validateForUpdate(Order object) throws ValidationException {

    }

    @Override
    public void validateForDelete(Order object) throws ValidationException {

    }

    @Override
    public void validateIdentity(Order object) throws ValidationException {

    }
}
