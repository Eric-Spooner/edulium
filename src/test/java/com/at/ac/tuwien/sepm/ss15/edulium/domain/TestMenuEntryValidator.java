package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Unit Test for the MenuEntry validator
 */
public class TestMenuEntryValidator extends AbstractDomainTest {
    @Autowired
    private Validator<MenuEntry> menuEntryValidator;

    private MenuEntry createMenuEntry(String name, String desc, String cat,
                                      double price, double tax, boolean available) throws ValidationException, DAOException {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName(cat);
        menuCategory.setIdentity(0L);

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(tax));
        taxRate.setIdentity(0L);

        MenuEntry entry = new MenuEntry();
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        entry.setName(name);
        entry.setDescription(desc);
        entry.setPrice(BigDecimal.valueOf(price));
        entry.setAvailable(available);

        return entry;
    }

    @Test
    public void testValidateForCreate_shouldAccept() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = null;

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_withoutNameShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setName(null);

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_withoutDescriptionShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setDescription(null);

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_withEmptyNameShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setName("");

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_withoutPriceShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setPrice(null);

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_withoutCategoryShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setCategory(null);

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_withCategoryWithoutIdentityShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.getCategory().setIdentity(null);

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_withoutTaxRateShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setTaxRate(null);

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_withTaxRateWithoutIdentityShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.getTaxRate().setIdentity(null);

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_withoutAvailabilityShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setAvailable(null);

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_withNegativePriceShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", -20.0, 0.2, true);

        // WHEN
        menuEntryValidator.validateForCreate(entry);
    }

    @Test
    public void testValidateForUpdate_shouldAccept() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setIdentity(0L);
        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = null;

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withoutIdentityShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withoutNameShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setName(null);
        entry.setIdentity(0L);

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withoutDescriptionShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setDescription(null);
        entry.setIdentity(0L);

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withEmptyNameShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setName("");
        entry.setIdentity(0L);

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withoutPriceShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setPrice(null);
        entry.setIdentity(0L);

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withoutCategoryShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setCategory(null);
        entry.setIdentity(0L);

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withCategoryWithoutIdentityShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.getCategory().setIdentity(null);

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withoutTaxRateShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setTaxRate(null);
        entry.setIdentity(0L);

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withTaxRateWithoutIdentityShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.getTaxRate().setIdentity(null);

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withoutAvailabilityShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", 20.0, 0.2, true);
        entry.setAvailable(null);
        entry.setIdentity(0L);

        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_withNegativePriceShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", -20.0, 0.2, true);
        entry.setIdentity(0L);
        // WHEN
        menuEntryValidator.validateForUpdate(entry);
    }

    @Test
    public void testValidateForDelete_shouldAccept() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", -20.0, 0.2, true);
        entry.setIdentity(0L);
        // WHEN
        menuEntryValidator.validateForDelete(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = null;
        // WHEN
        menuEntryValidator.validateForDelete(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_withoutIdentityShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", -20.0, 0.2, true);
        // WHEN
        menuEntryValidator.validateForDelete(entry);
    }

    @Test
    public void testValidateIdentity_shouldAccept() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = new MenuEntry();
        entry.setIdentity(0L);
        // WHEN
        menuEntryValidator.validateIdentity(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = null;
        // WHEN
        menuEntryValidator.validateIdentity(entry);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_withoutIdentityShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        MenuEntry entry = createMenuEntry("entry", "desc", "cat", -20.0, 0.2, true);
        // WHEN
        menuEntryValidator.validateIdentity(entry);
    }
}
