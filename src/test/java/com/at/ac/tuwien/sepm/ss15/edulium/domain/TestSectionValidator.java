package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit Test for the Section validator
 */
public class TestSectionValidator extends AbstractDomainTest {
    @Autowired
    private Validator<Section> sectionValidator;

    @Test
    public void testValidateForCreate_shouldAcceptSection() throws ValidationException {
        // GIVEN
        Section section = new Section();
        section.setName("section");

        // WHEN
        sectionValidator.validateForCreate(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_sectionWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        Section section = new Section();

        // WHEN
        sectionValidator.validateForCreate(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_sectionWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        Section section = new Section();
        section.setName("");

        // WHEN
        sectionValidator.validateForCreate(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Section section = null;

        // WHEN
        sectionValidator.validateForCreate(section);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptSection() throws ValidationException {
        // GIVEN
        Section section = new Section();
        section.setIdentity(0L);
        section.setName("section");

        // WHEN
        sectionValidator.validateForUpdate(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_sectionWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Section section = new Section();
        section.setName("section");

        // WHEN
        sectionValidator.validateForUpdate(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_sectionWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        Section section = new Section();
        section.setIdentity(0L);

        // WHEN
        sectionValidator.validateForUpdate(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_sectionWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        Section section = new Section();
        section.setIdentity(0L);
        section.setName("");

        // WHEN
        sectionValidator.validateForUpdate(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Section section = null;

        // WHEN
        sectionValidator.validateForUpdate(section);
    }

    @Test
    public void testValidateForDelete_shouldAcceptSection() throws ValidationException {
        // GIVEN
        Section section = new Section();
        section.setIdentity(0L);

        // WHEN
        sectionValidator.validateForDelete(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_sectionWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Section section = new Section();

        // WHEN
        sectionValidator.validateForDelete(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Section section = null;

        // WHEN
        sectionValidator.validateForDelete(section);
    }

    @Test
    public void testValidateIdentity_shouldAcceptSection() throws ValidationException {
        // GIVEN
        Section section = new Section();
        section.setIdentity(0L);

        // WHEN
        sectionValidator.validateIdentity(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_sectionWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Section section = new Section();

        // WHEN
        sectionValidator.validateIdentity(section);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Section section = null;

        // WHEN
        sectionValidator.validateIdentity(section);
    }
}
