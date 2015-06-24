package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * Unit Test for the TaxRate validator
 */
public class TestMenuValidator extends AbstractDomainTest {
    @Autowired
    private Validator<Menu> menuValidator;

    private MenuEntry createMenuEntry(String name, String desc, String cat, double price, double tax, boolean available)
            throws ValidationException {
        MenuCategory menuCategory = MenuCategory.withIdentity(3);
        menuCategory.setName(cat);

        TaxRate taxRate = TaxRate.withIdentity(4);
        taxRate.setValue(BigDecimal.valueOf(tax));

        MenuEntry entry = MenuEntry.withIdentity(3);
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        entry.setName(name);
        entry.setDescription(desc);
        entry.setPrice(BigDecimal.valueOf(price));
        entry.setAvailable(available);
        return entry;
    }

    @Test
    public void testValidateForCreate_shouldAcceptMenu() throws ValidationException {
        // GIVEN
        LinkedList<MenuEntry> list = new LinkedList<MenuEntry>();
        list.add(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        Menu menu = new Menu();
        menu.setName("Menu");
        menu.setEntries(list);

        // WHEN
        menuValidator.validateForCreate(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_MenuWithoutValueShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();

        // WHEN
        menuValidator.validateForCreate(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = null;

        // WHEN
        menuValidator.validateForCreate(menu);
    }


    @Test(expected = ValidationException.class)
         public void testValidateForCreate_MenuWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();
        menu.setName("");

        // WHEN
        menuValidator.validateForCreate(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_MenuWithNameNullShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();

        // WHEN
        menuValidator.validateForCreate(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_MenuWithoutListMenuEntriesShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();
        menu.setName("Menu");

        // WHEN
        menuValidator.validateForCreate(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_MenuWithEmptyListMenuEntriesShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();
        menu.setName("Menu");
        menu.setEntries(new LinkedList<MenuEntry>());

        // WHEN
        menuValidator.validateForCreate(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_MenuWithListMenuEntriesWithoutIdentificationShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();
        menu.setName("Menu");
        LinkedList<MenuEntry> list = new LinkedList<MenuEntry>();
        list.add(new MenuEntry());
        menu.setEntries(list);
        // WHEN
        menuValidator.validateForCreate(menu);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptMenu() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();
        menu.setIdentity(0L);
        menu.setName("Menu");
        LinkedList<MenuEntry> list = new LinkedList<MenuEntry>();
        list.add(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        menu.setEntries(list);

        // WHEN
        menuValidator.validateForUpdate(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_MenuWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();
        menu.setName("Menu");

        // WHEN
        menuValidator.validateForUpdate(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_MenuWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();
        menu.setIdentity(0L);

        // WHEN
        menuValidator.validateForUpdate(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = null;

        // WHEN
        menuValidator.validateForUpdate(menu);
    }

    @Test
    public void testValidateForDelete_shouldAcceptMenu() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();
        menu.setIdentity(0L);

        // WHEN
        menuValidator.validateForDelete(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_MenuWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();

        // WHEN
        menuValidator.validateForDelete(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = null;

        // WHEN
        menuValidator.validateForDelete(menu);
    }

    @Test
    public void testValidateIdentity_shouldAcceptMenu() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();
        menu.setIdentity(0L);

        // WHEN
        menuValidator.validateIdentity(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_MenuWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = new Menu();

        // WHEN
        menuValidator.validateIdentity(menu);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Menu menu = null;

        // WHEN
        menuValidator.validateIdentity(menu);
    }
}
