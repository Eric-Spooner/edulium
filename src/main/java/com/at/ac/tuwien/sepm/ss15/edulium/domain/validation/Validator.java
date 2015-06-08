package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation;

/**
 * Validator interface for domain objects
 */
public interface Validator<T> extends ImmutableValidator<T> {

    /**
     * validates the object for the create action
     * @param object object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForCreate(T object) throws ValidationException;

    /**
     * validates the object for the update action
     * @param object object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForUpdate(T object) throws ValidationException;

    /**
     * validates the object for the delete action
     * @param object object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForDelete(T object) throws ValidationException;

    /**
     * validates if all parameters needed for identification are set
     * @param object object to validate
     * @throws ValidationException if the identity of the object is not set
     */
    void validateIdentity(T object) throws ValidationException;
}
