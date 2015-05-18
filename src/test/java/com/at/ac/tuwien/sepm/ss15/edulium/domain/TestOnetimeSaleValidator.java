package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Unit Test for the TestOnetimeSaleValidator validator
 */
//TODO: complete
public class TestOnetimeSaleValidator extends AbstractDomainTest {
    @Autowired
    private Validator<OnetimeSale> saleValidator;

    @Test
    public void testValidateForCreate_shouldAcceptOnetimeSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        saleValidator.validateForCreate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_onetimeSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        saleValidator.validateForCreate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        saleValidator.validateForCreate(onetimeSale);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptOnetimeSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        saleValidator.validateForUpdate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_onetimeSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        saleValidator.validateForUpdate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        saleValidator.validateForUpdate(onetimeSale);
    }

    @Test
    public void testValidateForDelete_shouldAcceptOnetimeSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        saleValidator.validateForDelete(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_onetimeSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        saleValidator.validateForDelete(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        saleValidator.validateForDelete(onetimeSale);
    }


    @Test
    public void testValidateIdentity_shouldAcceptSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        saleValidator.validateIdentity(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        saleValidator.validateIdentity(onetimeSale);
    }


    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        saleValidator.validateIdentity(onetimeSale);
    }
}
