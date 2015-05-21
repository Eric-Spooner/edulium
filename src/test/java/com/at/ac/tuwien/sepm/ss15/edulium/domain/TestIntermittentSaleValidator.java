package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Unit Test for the TestIntermittentSaleValidator validator
 */
public class TestIntermittentSaleValidator extends AbstractDomainTest {
    @Autowired
    private Validator<IntermittentSale> intermittentSaleValidator;

    @Test
    public void testValidateForCreate_shouldAcceptIntermittentSale() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_intermittentSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setName("");

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_intermittentSaleWithNegativeDurationShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(-120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptIntermittentSale() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);

        // WHEN
        intermittentSaleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_intermittentSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);

        // WHEN
        intermittentSaleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));

        // WHEN
        intermittentSaleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setName("");

        // WHEN
        intermittentSaleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_intermittentSaleWithNegativeDurationShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(-120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);

        // WHEN
        intermittentSaleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        intermittentSaleValidator.validateForUpdate(intermittentSale);
    }

    @Test
    public void testValidateForDelete_shouldAcceptIntermittentSale() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);

        // WHEN
        intermittentSaleValidator.validateForDelete(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_intermittentSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);

        // WHEN
        intermittentSaleValidator.validateForDelete(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));

        // WHEN
        intermittentSaleValidator.validateForDelete(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setName("");

        // WHEN
        intermittentSaleValidator.validateForDelete(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        intermittentSaleValidator.validateForDelete(intermittentSale);
    }


    @Test
    public void testValidateIdentity_shouldAcceptSale() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);


        // WHEN
        intermittentSaleValidator.validateIdentity(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);


        // WHEN
        intermittentSaleValidator.validateIdentity(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));

        // WHEN
        intermittentSaleValidator.validateIdentity(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentitysaleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setName("");

        // WHEN
        intermittentSaleValidator.validateIdentity(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithNegativeDurationShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setName("Sale");
        intermittentSale.setIdentity(new Long(123));
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(-120);
        intermittentSale.setFromDayTime(LocalDateTime.now());
        intermittentSale.setMonday(true);
        intermittentSale.setTuesday(true);
        intermittentSale.setWednesday(true);
        intermittentSale.setThursday(true);
        intermittentSale.setFriday(false);
        intermittentSale.setSaturday(false);
        intermittentSale.setSunday(false);

        // WHEN
        intermittentSaleValidator.validateIdentity(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        intermittentSaleValidator.validateIdentity(intermittentSale);
    }
}
