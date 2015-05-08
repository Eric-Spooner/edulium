package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.UserValidator;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

/**
 * implementation of the UserValidator
 */
public class UserValidatorImpl implements UserValidator {
    private final int IDENTITY_MAX_LENGTH = 25;
    private final int NAME_MAX_LENGTH = 100;
    private final int ROLE_MAX_LENGTH = 100;

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
        if (user.getIdentity().length() > IDENTITY_MAX_LENGTH) {
            throw new ValidationException("identity must not be longer than " + IDENTITY_MAX_LENGTH + " characters");
        }
    }

    private void checkForRequiredDataAttributesForCreateAndUpdate(User user) throws ValidationException {
        if (user.getName() == null) {
            throw new ValidationException("name must not be null");
        }
        if (user.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }
        if (user.getName().length() > NAME_MAX_LENGTH) {
            throw new ValidationException("name must not be longer than " + NAME_MAX_LENGTH + " characters");
        }

        if (user.getRole() == null) {
            throw new ValidationException("role must not be null");
        }
        if (user.getRole().isEmpty()) {
            throw new ValidationException("role must not be empty");
        }
        if (user.getRole().length() > ROLE_MAX_LENGTH) {
            throw new ValidationException("role must not be longer than " + ROLE_MAX_LENGTH + " characters");
        }
    }
}
