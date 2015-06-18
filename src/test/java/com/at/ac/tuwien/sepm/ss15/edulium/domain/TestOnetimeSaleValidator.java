package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

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

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

        // WHEN
        onetimeSaleValidator.validateForCreate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

        // WHEN
        onetimeSaleValidator.validateForCreate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("");
        onetimeSale.setFromTime(LocalDateTime.now());
        onetimeSale.setToTime(LocalDateTime.now());

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

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
        onetimeSale.setFromTime(LocalDateTime.parse("2007-12-03T10:15:30"));
        onetimeSale.setToTime(LocalDateTime.parse("2007-12-03T10:14:00"));

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

        // WHEN
        onetimeSaleValidator.validateForCreate(onetimeSale);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptOnetimeSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.parse("2007-12-03T10:15:30"));
        onetimeSale.setToTime(LocalDateTime.parse("2007-12-03T10:16:00"));

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

        // WHEN
        onetimeSaleValidator.validateForUpdate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_onetimeSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.parse("2007-12-03T10:15:30"));
        onetimeSale.setToTime(LocalDateTime.parse("2007-12-03T10:16:00"));

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

        // WHEN
        onetimeSaleValidator.validateForUpdate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setFromTime(LocalDateTime.parse("2007-12-03T10:15:30"));
        onetimeSale.setToTime(LocalDateTime.parse("2007-12-03T10:16:00"));

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

        // WHEN
        onetimeSaleValidator.validateForUpdate(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("");
        onetimeSale.setFromTime(LocalDateTime.parse("2007-12-03T10:15:30"));
        onetimeSale.setToTime(LocalDateTime.parse("2007-12-03T10:16:00"));

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

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
        onetimeSale.setFromTime(LocalDateTime.parse("2007-12-03T10:15:30"));
        onetimeSale.setToTime(LocalDateTime.parse("2007-12-03T10:14:00"));

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

        // WHEN
        onetimeSaleValidator.validateForUpdate(onetimeSale);
    }

    @Test
    public void testValidateForDelete_shouldAcceptOnetimeSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.parse("2007-12-03T10:15:30"));
        onetimeSale.setToTime(LocalDateTime.parse("2007-12-03T10:16:00"));

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

        // WHEN
        onetimeSaleValidator.validateForDelete(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_onetimeSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.parse("2007-12-03T10:15:30"));
        onetimeSale.setToTime(LocalDateTime.parse("2007-12-03T10:16:00"));

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

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

    @Test
    public void testValidateIdentity_shouldAcceptSale() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setIdentity(new Long(123));
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.parse("2007-12-03T10:15:30"));
        onetimeSale.setToTime(LocalDateTime.parse("2007-12-03T10:16:00"));

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

        // WHEN
        onetimeSaleValidator.validateIdentity(onetimeSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        OnetimeSale onetimeSale = new OnetimeSale();
        onetimeSale.setName("Sale");
        onetimeSale.setFromTime(LocalDateTime.parse("2007-12-03T10:15:30"));
        onetimeSale.setToTime(LocalDateTime.parse("2007-12-03T10:16:00"));

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = new MenuEntry();
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(new MenuEntry());

        onetimeSale.setEntries(entries);

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
}