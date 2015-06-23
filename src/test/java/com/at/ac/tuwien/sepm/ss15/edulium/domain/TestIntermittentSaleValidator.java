package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();

        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_intermittentSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_intermittentSaleWithNegativeDurationShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(-120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_intermittentSaleWithoutEntriesShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        intermittentSale.setEntries(null);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_intermittentSaleWithEmptyEntriesShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_intermittentSaleWithInvalidEntriesShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        entries.add(new MenuEntry());

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_intermittentSaleWithoutFromDayTimeShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);;

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForCreate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_intermittentSaleWithoutEnabledShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

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
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_intermittentSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_saleWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForUpdate(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_intermittentSaleWithNegativeDurationShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(-120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

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
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateForDelete(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_intermittentSaleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

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
        intermittentSale.setIdentity(123L);
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

        // WHEN
        intermittentSaleValidator.validateIdentity(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_saleWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setName("Sale");
        intermittentSale.setEnabled(true);
        intermittentSale.setDuration(Duration.ofMinutes(120));
        intermittentSale.setFromDayTime(LocalTime.now());

        HashSet<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        intermittentSale.setDaysOfSale(days);

        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry entry = MenuEntry.withIdentity(1L);
        entry.setPrice(BigDecimal.valueOf(10));
        entries.add(entry);

        intermittentSale.setEntries(entries);

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