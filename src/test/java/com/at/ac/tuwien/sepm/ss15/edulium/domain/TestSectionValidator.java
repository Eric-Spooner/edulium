package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit Test for the MenuCategory validator
 */
public class TestSectionValidator extends AbstractDomainTest {
    @Autowired
    private Validator<MenuCategory> menuCategoryValidator;

    @Test
    public void testValidateForCreate_shouldAcceptMenuCategory() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName("cat");

        // WHEN
        menuCategoryValidator.validateForCreate(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_menuCategoryWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();

        // WHEN
        menuCategoryValidator.validateForCreate(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_menuCategoryWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName("");

        // WHEN
        menuCategoryValidator.validateForCreate(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = null;

        // WHEN
        menuCategoryValidator.validateForCreate(menuCategory);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptMenuCategory() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setIdentity(0L);
        menuCategory.setName("cat");

        // WHEN
        menuCategoryValidator.validateForUpdate(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_menuCategoryWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName("cat");

        // WHEN
        menuCategoryValidator.validateForUpdate(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_menuCategoryWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setIdentity(0L);

        // WHEN
        menuCategoryValidator.validateForUpdate(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_menuCategoryWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setIdentity(0L);
        menuCategory.setName("");

        // WHEN
        menuCategoryValidator.validateForUpdate(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = null;

        // WHEN
        menuCategoryValidator.validateForUpdate(menuCategory);
    }

    @Test
    public void testValidateForDelete_shouldAcceptMenuCategory() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setIdentity(0L);

        // WHEN
        menuCategoryValidator.validateForDelete(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_menuCategoryWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();

        // WHEN
        menuCategoryValidator.validateForDelete(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = null;

        // WHEN
        menuCategoryValidator.validateForDelete(menuCategory);
    }

    @Test
    public void testValidateIdentity_shouldAcceptMenuCategory() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setIdentity(0L);

        // WHEN
        menuCategoryValidator.validateIdentity(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_menuCategoryWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = new MenuCategory();

        // WHEN
        menuCategoryValidator.validateIdentity(menuCategory);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        MenuCategory menuCategory = null;

        // WHEN
        menuCategoryValidator.validateIdentity(menuCategory);
    }
}
