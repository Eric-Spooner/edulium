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
    private Validator<IntermittentSale> saleValidator;

    @Test
    public void testValidateForCreate_shouldAcceptIntermittentSale() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
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
        saleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_intermittentSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
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
        saleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        saleValidator.validateForCreate(intermittentSale);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptIntermittentSale() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
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
        saleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_intermittentSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
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
        saleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        saleValidator.validateForUpdate(intermittentSale);
    }

    @Test
    public void testValidateForDelete_shouldAcceptIntermittentSale() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
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
        saleValidator.validateForDelete(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_intermittentSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
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
        saleValidator.validateForDelete(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        saleValidator.validateForDelete(intermittentSale);
    }


    @Test
    public void testValidateIdentity_shouldAcceptSale() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(new Long(123));
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
        saleValidator.validateIdentity(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
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
        saleValidator.validateIdentity(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        saleValidator.validateIdentity(intermittentSale);
    }
}
