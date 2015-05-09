package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;

/**
 * Validator for the User domain object
 */
public interface UserValidator {
    /**
     * validates the object for the create action
     * @param user object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForCreate(User user) throws ValidationException;

    /**
     * validates the object for the update action
     * @param user object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForUpdate(User user) throws ValidationException;

    /**
     * validates the object for the delete action
     * @param user object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForDelete(User user) throws ValidationException;

    /**
     * validates if all parameters needed for identification are set
     * @param user object to validate
     * @throws ValidationException if the identity of the object is not set
     */
    void validateIdentity(User user) throws ValidationException;
}
