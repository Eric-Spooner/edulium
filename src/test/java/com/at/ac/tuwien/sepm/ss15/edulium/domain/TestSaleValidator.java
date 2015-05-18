package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit Test for the Sale validator
 */
public class TestSaleValidator extends AbstractDomainTest {
    @Autowired
    private Validator<Sale> saleValidator;

    @Test
    public void testValidateForCreate_shouldAcceptSale() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));
        sale.setName("Super Sale");

        // WHEN
        saleValidator.validateForCreate(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setName("Super Sale");

        // WHEN
        saleValidator.validateForCreate(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));

        // WHEN
        saleValidator.validateForCreate(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));
        sale.setName("");

        // WHEN
        saleValidator.validateForCreate(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = null;

        // WHEN
        saleValidator.validateForCreate(sale);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptSale() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));
        sale.setName("Super Sale");

        // WHEN
        saleValidator.validateForUpdate(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setName("Super Sale");

        // WHEN
        saleValidator.validateForUpdate(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));

        // WHEN
        saleValidator.validateForUpdate(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));
        sale.setName("");

        // WHEN
        saleValidator.validateForUpdate(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = null;

        // WHEN
        saleValidator.validateForUpdate(sale);
    }

    @Test
    public void testValidateForDelete_shouldAcceptSale() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));
        sale.setName("Super Sale");

        // WHEN
        saleValidator.validateForDelete(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_saleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setName("Super Sale");

        // WHEN
        saleValidator.validateForDelete(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));

        // WHEN
        saleValidator.validateForDelete(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));
        sale.setName("");

        // WHEN
        saleValidator.validateForDelete(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = null;

        // WHEN
        saleValidator.validateForDelete(sale);
    }


    @Test
    public void testValidateIdentity_shouldAcceptSale() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));
        sale.setName("Super Sale");

        // WHEN
        saleValidator.validateIdentity(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setName("Super Sale");

        // WHEN
        saleValidator.validateIdentity(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));

        // WHEN
        saleValidator.validateIdentity(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(123));
        sale.setName("");

        // WHEN
        saleValidator.validateIdentity(sale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Sale sale = null;

        // WHEN
        saleValidator.validateIdentity(sale);
    }
}
