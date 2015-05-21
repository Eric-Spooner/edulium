package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Unit Test for the TestOnetimeSaleValidator validator
 */
public class TestOnetimeSaleValidator extends AbstractDomainTest {
    @Autowired
    private Validator<OnetimeSale> onetimeSaleValidator;

    @Test
    public void testValidateForCreate_shouldAcceptOnetimeSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleValidator.validateForCreate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_onetimeSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleValidator.validateForCreate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));

        // WHEN
        onetimeSaleValidator.validateForCreate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("");

        // WHEN
        onetimeSaleValidator.validateForCreate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        onetimeSaleValidator.validateForCreate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_fromTimeAfterToTimeShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.parse("2015-05-06"));
        onetimeSale.setToTime(LocalDateTime.parse("2015-05-03"));

        // WHEN
        onetimeSaleValidator.validateForCreate(onetimeSale);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptOnetimeSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleValidator.validateForUpdate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_onetimeSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleValidator.validateForUpdate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));

        // WHEN
        onetimeSaleValidator.validateForUpdate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("");

        // WHEN
        onetimeSaleValidator.validateForUpdate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        onetimeSaleValidator.validateForUpdate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_fromTimeAfterToTimeShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.parse("2015-05-06"));
        onetimeSale.setToTime(LocalDateTime.parse("2015-05-03"));

        // WHEN
        onetimeSaleValidator.validateForUpdate(onetimeSale);
    }

    @Test
    public void testValidateForDelete_shouldAcceptOnetimeSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleValidator.validateForDelete(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_onetimeSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleValidator.validateForDelete(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));

        // WHEN
        onetimeSaleValidator.validateForDelete(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("");

        // WHEN
        onetimeSaleValidator.validateForDelete(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        onetimeSaleValidator.validateForDelete(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_fromTimeAfterToTimeShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.parse("2015-05-06"));
        onetimeSale.setToTime(LocalDateTime.parse("2015-05-03"));

        // WHEN
        onetimeSaleValidator.validateForDelete(onetimeSale);
    }

    @Test
    public void testValidateIdentity_shouldAcceptSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleValidator.validateIdentity(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        // WHEN
        onetimeSaleValidator.validateIdentity(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdntity_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));

        // WHEN
        onetimeSaleValidator.validateIdentity(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("");

        // WHEN
        onetimeSaleValidator.validateIdentity(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = null;

        // WHEN
        onetimeSaleValidator.validateIdentity(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_fromTimeAfterToTimeShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.parse("2015-05-06"));
        onetimeSale.setToTime(LocalDateTime.parse("2015-05-03"));

        // WHEN
        onetimeSaleValidator.validateIdentity(onetimeSale);
    }
}
