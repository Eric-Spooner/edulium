package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

/**
 * validator implementation for the User domain object
 */
public class UserValidator implements Validator<User> {

    @Override
    public void validateForCreate(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("user must not be null");
        }

        validateIdentity(user);
        checkForRequiredDataAttributesForCreateAndUpdate(user);
    }

    @Override
    public void validateForUpdate(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("user must not be null");
        }

        validateIdentity(user);
        checkForRequiredDataAttributesForCreateAndUpdate(user);
    }

    @Override
    public void validateForDelete(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("user must not be null");
        }

        validateIdentity(user);
    }

    @Override
    public void validateIdentity(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("user must not be null");
        }
        if (user.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
        if (user.getIdentity().isEmpty()) {
            throw new ValidationException("identity must not be empty");
        }
    }

    private void checkForRequiredDataAttributesForCreateAndUpdate(User user) throws ValidationException {
        if (user.getName() == null) {
            throw new ValidationException("name must not be null");
        }
        if (user.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }
        if (user.getRole() == null) {
            throw new ValidationException("role must not be null");
        }
        if (user.getRole().isEmpty()) {
            throw new ValidationException("role must not be empty");
        }
    }
}
