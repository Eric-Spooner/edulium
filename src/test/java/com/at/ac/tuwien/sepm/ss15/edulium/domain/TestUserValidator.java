package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit Test for the User validator
 */
public class TestUserValidator extends AbstractDomainTest {
    @Autowired
    private Validator<User> userValidator;

    @Test
    public void testValidateForCreate_shouldAcceptUser() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("warty");
        user.setName("Warty Warthog");
        user.setRole("manager");

        // WHEN
        userValidator.validateForCreate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_userWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setName("Jaunty Jackalope");
        user.setRole("tester");

        // WHEN
        userValidator.validateForCreate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_userWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("jaunty");
        user.setRole("tester");

        // WHEN
        userValidator.validateForCreate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_userWithoutRoleShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("jaunty");
        user.setName("Jaunty Jackalope");

        // WHEN
        userValidator.validateForCreate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_userWithEmptyIdentityShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("");
        user.setName("Jaunty Jackalope");
        user.setRole("tester");

        // WHEN
        userValidator.validateForCreate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_userWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("jaunty");
        user.setName("");
        user.setRole("tester");

        // WHEN
        userValidator.validateForCreate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_userWithEmptyRoleShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("jaunty");
        user.setName("Jaunty Jackalope");
        user.setRole("");

        // WHEN
        userValidator.validateForCreate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        User user = null;

        // WHEN
        userValidator.validateForCreate(user);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptUser() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("warty");
        user.setName("Warty Warthog");
        user.setRole("manager");

        // WHEN
        userValidator.validateForUpdate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_userWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setName("Jaunty Jackalope");
        user.setRole("tester");

        // WHEN
        userValidator.validateForUpdate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_userWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("jaunty");
        user.setRole("tester");

        // WHEN
        userValidator.validateForUpdate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_userWithoutRoleShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("jaunty");
        user.setName("Jaunty Jackalope");

        // WHEN
        userValidator.validateForUpdate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_userWithEmptyIdentityShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("");
        user.setName("Jaunty Jackalope");
        user.setRole("tester");

        // WHEN
        userValidator.validateForUpdate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_userWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("jaunty");
        user.setName("");
        user.setRole("tester");

        // WHEN
        userValidator.validateForUpdate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_userWithEmptyRoleShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("jaunty");
        user.setName("Jaunty Jackalope");
        user.setRole("");

        // WHEN
        userValidator.validateForUpdate(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        User user = null;

        // WHEN
        userValidator.validateForUpdate(user);
    }

    @Test
    public void testValidateForDelete_shouldAcceptUser() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("hardy");

        // WHEN
        userValidator.validateForDelete(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_userWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();

        // WHEN
        userValidator.validateForDelete(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_userWithEmptyIdentityShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("");

        // WHEN
        userValidator.validateForDelete(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        User user = null;

        // WHEN
        userValidator.validateForDelete(user);
    }

    @Test
    public void testValidateIdentity_shouldAcceptUser() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("hardy");

        // WHEN
        userValidator.validateIdentity(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_userWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();

        // WHEN
        userValidator.validateIdentity(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_userWithEmptyIdentityShouldThrow() throws ValidationException {
        // GIVEN
        User user = new User();
        user.setIdentity("");

        // WHEN
        userValidator.validateIdentity(user);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        User user = null;

        // WHEN
        userValidator.validateIdentity(user);
    }
}
