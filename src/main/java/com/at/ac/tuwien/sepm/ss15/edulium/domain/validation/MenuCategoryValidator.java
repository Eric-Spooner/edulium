package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.MenuCategory;

/**
 * Validator for the MenuCategory domain object
 */
public interface MenuCategoryValidator {

    /**
     * validates the object for the create action
     * @param menuCategory object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForCreate(MenuCategory menuCategory) throws ValidationException;

    /**
     * validates the object for the update action
     * @param menuCategory object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForUpdate(MenuCategory menuCategory) throws ValidationException;

    /**
     * validates the object for the delete action
     * @param menuCategory object to validate
     * @throws ValidationException if the object is not valid
     */
    void validateForDelete(MenuCategory menuCategory) throws ValidationException;

    /**
     * validates if all parameters needed for identification are set
     * @param menuCategory object to validate
     * @throws ValidationException if the identity of the object is not set
     */
    void validateIdentity(MenuCategory menuCategory) throws ValidationException;
}
