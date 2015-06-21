package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.*;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit Test for the IntermittentSaleDAO
 */
public class TestIntermittentSaleDAO extends AbstractDAOTest {
    @Autowired
    private DAO<IntermittentSale> intermittentSaleDAO;
    @Autowired
    private DAO<MenuCategory> menuCategoryDAO;
    @Autowired
    private DAO<MenuEntry> menuEntryDAO;
    @Autowired
    private DAO<TaxRate> taxRateDAO;

    private MenuEntry createMenuEntry(Long identity, String name, String description, String category, double price, double tax, boolean available)
            throws ValidationException, DAOException {
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setName(category);
        menuCategoryDAO.create(menuCategory);

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(tax));
        taxRateDAO.create(taxRate);

        MenuEntry entry = new MenuEntry();
        entry.setIdentity(identity);
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        entry.setName(name);
        entry.setDescription(description);
        entry.setPrice(BigDecimal.valueOf(price));
        entry.setAvailable(available);
        menuEntryDAO.create(entry);

        return entry;
    }

    private List<MenuEntry> createRandomEntries() throws ValidationException, DAOException {
        int count = 1 + (int) Math.random() * 100;
        List<MenuEntry> entries = new ArrayList();

        for(int i = 0; i < count; i++) {
            MenuEntry entry = createMenuEntry(Long.valueOf((long)Math.random() * 99), "entry", "desc", "cat", 20, 0.2, true);
            entries.add(entry);
        }

        return entries;
    }

    private IntermittentSale createIntermittentSale(Long identity, String name, List<MenuEntry> entries, Integer duration, Boolean enabled, LocalTime fromDayTime, Set<DayOfWeek> days) {
        IntermittentSale intermittentSale = new IntermittentSale();
        intermittentSale.setIdentity(identity);
        intermittentSale.setName(name);
        intermittentSale.setEntries(entries);
        intermittentSale.setDuration(Duration.ofMinutes(duration));
        intermittentSale.setEnabled(enabled);
        intermittentSale.setFromDayTime(fromDayTime);
        intermittentSale.setDaysOfSale(days);
        return intermittentSale;
    }

    private IntermittentSale createIntermittentSale(Long identity) throws ValidationException, DAOException {

        HashSet<DayOfWeek> daysOfWeek = new HashSet<>();

        daysOfWeek.add(DayOfWeek.MONDAY);
        daysOfWeek.add(DayOfWeek.TUESDAY);
        daysOfWeek.add(DayOfWeek.SATURDAY);
        daysOfWeek.add(DayOfWeek.SUNDAY);

        return createIntermittentSale(identity, "Sale", createRandomEntries(), 60, true, LocalTime.parse("10:15:30"), daysOfWeek);
    }

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = createIntermittentSale(123L);

        // WHEN
        intermittentSaleDAO.create(intermittentSale);

        // THEN
        // try to find the intermittentsale and compare it
        List<IntermittentSale> storedObjects = intermittentSaleDAO.find(IntermittentSale.withIdentity(intermittentSale.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(intermittentSale, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = createIntermittentSale(null);

        // WHEN
        intermittentSaleDAO.create(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        intermittentSaleDAO.create(intermittentSale);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // PREPARE
        IntermittentSale intermittentSale = createIntermittentSale(123L);

        // check if user is stored
        intermittentSaleDAO.create(intermittentSale);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale).size());

        // GIVEN
        intermittentSale.setName("updatedSALE");

        // WHEN
        intermittentSaleDAO.update(intermittentSale);

        // THEN
        // check if the user has been updated;
        List<IntermittentSale> storedObjects = intermittentSaleDAO.find(intermittentSale);
        assertEquals(1, storedObjects.size());
        assertEquals(intermittentSale, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = createIntermittentSale(null);

        // WHEN
        intermittentSaleDAO.update(intermittentSale);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        intermittentSaleDAO.update(intermittentSale);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();

        // search for a non-existing intermittent sale identity
        try {
            do {
                intermittentSale.setIdentity(new Long((int)(Math.random()*999999999)));
            } while (!intermittentSaleDAO.find(intermittentSale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing intermittent sale identity");
        }

        intermittentSale = createIntermittentSale(intermittentSale.getIdentity());

        // WHEN
        intermittentSaleDAO.update(intermittentSale);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // PREPARE
        final int numberBefore = intermittentSaleDAO.getAll().size();
        IntermittentSale intermittentSale = createIntermittentSale(123L);

        // check if user is stored
        intermittentSaleDAO.create(intermittentSale);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale).size());

        // GIVEN
        IntermittentSale intermittentSale2 = IntermittentSale.withIdentity(intermittentSale.getIdentity());

        // WHEN
        intermittentSaleDAO.delete(intermittentSale2);

        // THEN
        // check if the user has been updated;
        List<IntermittentSale> storedObjects = intermittentSaleDAO.find(intermittentSale2);
        assertEquals(numberBefore, storedObjects.size());
        assertEquals(0, intermittentSaleDAO.find(intermittentSale).size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = null;

        // WHEN
        intermittentSaleDAO.delete(intermittentSale);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();

        // search for a non-existing intermittent sale identity
        try {
            do {
                intermittentSale.setIdentity(new Long((int)(Math.random()*999999999)));
            } while (!intermittentSaleDAO.find(intermittentSale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing intermittent sale identity");
        }

        // WHEN
        intermittentSaleDAO.delete(intermittentSale);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(123L);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(124L);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = createIntermittentSale(125L);

        intermittentSaleDAO.create(intermittentSale3);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale3).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(intermittentSale1.getIdentity()); // for sale 1
        IntermittentSale matcher2 = IntermittentSale.withIdentity(intermittentSale2.getIdentity()); // for sale 2
        IntermittentSale matcher3 = IntermittentSale.withIdentity(intermittentSale3.getIdentity()); // for sale 3

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);
        List<IntermittentSale> result3 = intermittentSaleDAO.find(matcher3);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(intermittentSale3));
    }

    @Test
    public void testFind_byFromDayTimeShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        //FromDayTimes
        LocalTime time1 = LocalTime.parse("10:15:30");
        LocalTime time2 = LocalTime.parse("10:16:00");
        LocalTime time3 = LocalTime.parse("10:17:50");

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(123L);
        intermittentSale1.setFromDayTime(time1);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(124L);
        intermittentSale2.setFromDayTime(time2);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = createIntermittentSale(125L);
        intermittentSale3.setFromDayTime(time3);

        intermittentSaleDAO.create(intermittentSale3);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale3).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(123L); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setFromDayTime(time1);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(123L); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setFromDayTime(time2);
        IntermittentSale matcher3 = IntermittentSale.withIdentity(123L); // for sale 3
        matcher3.setIdentity(null);
        matcher3.setFromDayTime(time3);

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);
        List<IntermittentSale> result3 = intermittentSaleDAO.find(matcher3);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(intermittentSale3));
    }

    @Test
    public void testFind_byDurationShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        //FromDayTimes
        Duration duration50 = Duration.ofMinutes(50);
        Duration duration30 = Duration.ofMinutes(30);
        Duration duration120 = Duration.ofMinutes(120);

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(123L);
        intermittentSale1.setDuration(duration50);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(124L);
        intermittentSale2.setDuration(duration30);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = createIntermittentSale(125L);
        intermittentSale3.setDuration(duration120);

        intermittentSaleDAO.create(intermittentSale3);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale3).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(123L); // for sale 1
        matcher1.setIdentity(null);
        matcher1.setDuration(duration50);
        IntermittentSale matcher2 = IntermittentSale.withIdentity(123L); // for sale 2
        matcher2.setIdentity(null);
        matcher2.setDuration(duration30);
        IntermittentSale matcher3 = IntermittentSale.withIdentity(123L); // for sale 3
        matcher3.setIdentity(null);
        matcher3.setDuration(duration120);

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);
        List<IntermittentSale> result3 = intermittentSaleDAO.find(matcher3);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(intermittentSale3));
    }

    @Test
    public void testFind_bySaleDayShouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(123L);

        HashSet<DayOfWeek> days1 = new HashSet<>();
        days1.add(DayOfWeek.MONDAY);
        intermittentSale1.setDaysOfSale(days1);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(124L);

        HashSet<DayOfWeek> days2 = new HashSet<>();
        days2.add(DayOfWeek.FRIDAY);
        days2.add(DayOfWeek.SUNDAY);
        intermittentSale2.setDaysOfSale(days2);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // GIVEN
        IntermittentSale matcher1 = new IntermittentSale();
        IntermittentSale matcher2 = new IntermittentSale();

        matcher1.setDaysOfSale(new HashSet<>());
        matcher1.getDaysOfSale().add(DayOfWeek.MONDAY);

        matcher2.setDaysOfSale(new HashSet<>());
        matcher2.getDaysOfSale().add(DayOfWeek.FRIDAY);
        matcher2.getDaysOfSale().add(DayOfWeek.SUNDAY);

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);
        List<IntermittentSale> result2 = intermittentSaleDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(intermittentSale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(intermittentSale2));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException, ValidationException {
        //Find a non-existent ID
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(123L);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // GIVEN
        IntermittentSale matcher1 = IntermittentSale.withIdentity(intermittentSale1.getIdentity().intValue()+3492189); // for sale 1

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);

        // THEN
        assertEquals(0, result1.size());
    }

    @Test
    public void testFind_nullObjectShouldReturnEmptyList() throws DAOException {
        // GIVEN
        IntermittentSale matcher1 = null;

        // WHEN
        List<IntermittentSale> result1 = intermittentSaleDAO.find(matcher1);

        // THEN
        assertEquals(0, result1.size());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(123L);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(124L);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = createIntermittentSale(125L);

        intermittentSaleDAO.create(intermittentSale3);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale3).size());

        // WHEN
        List<IntermittentSale> result = intermittentSaleDAO.getAll();
        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(intermittentSale1));
        assertTrue(result.contains(intermittentSale2));
        assertTrue(result.contains(intermittentSale3));
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        intermittentSaleDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();

        // WHEN
        intermittentSaleDAO.getHistory(intermittentSale);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        IntermittentSale intermittentSale = new IntermittentSale();

        // search for a non-existing intermittent sale identity
        try {
            do {
                intermittentSale.setIdentity(new Long((int)(Math.random()*999999999)));
            } while (!intermittentSaleDAO.find(intermittentSale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing intermittent sale identity");
        }

        assertTrue(intermittentSaleDAO.getHistory(intermittentSale).isEmpty());
    }

    @Test
    public void testCreate_shouldAddRemoveAndReaddObject() throws DAOException, ValidationException {
        IntermittentSale intermittentSale = createIntermittentSale(123L);

        intermittentSaleDAO.create(intermittentSale);
        intermittentSaleDAO.create(intermittentSale);
        intermittentSaleDAO.create(intermittentSale);
        assertEquals(intermittentSaleDAO.find(intermittentSale).size(), 1);

        System.out.println(intermittentSale);
        System.out.println(intermittentSaleDAO.find(intermittentSale).get(0));

        assertTrue(intermittentSaleDAO.find(intermittentSale).contains(intermittentSale));

        intermittentSaleDAO.delete(intermittentSale);
        assertEquals(intermittentSaleDAO.find(intermittentSale).size(), 0);
        assertFalse(intermittentSaleDAO.find(intermittentSale).contains(intermittentSale));

        intermittentSaleDAO.create(intermittentSale);
        assertEquals(intermittentSaleDAO.find(intermittentSale).size(), 1);
        assertTrue(intermittentSaleDAO.find(intermittentSale).contains(intermittentSale));
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(123L);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(124L);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = createIntermittentSale(125L);

        intermittentSaleDAO.create(intermittentSale3);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale3).size());

        List<IntermittentSale> salesList = Arrays.asList(intermittentSale1, intermittentSale2, intermittentSale3);

        // WHEN
        List<IntermittentSale> result = intermittentSaleDAO.populate(salesList);
        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(intermittentSale1));
        assertTrue(result.contains(intermittentSale2));
        assertTrue(result.contains(intermittentSale3));
    }

    @Test
    public void testPopulate_shouldReturnFullyPopulatedObjectsOfDeletedObjects() throws DAOException, ValidationException {
        // Prepare

        // intermittent sale 1
        IntermittentSale intermittentSale1 = createIntermittentSale(123L);

        intermittentSaleDAO.create(intermittentSale1);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale1).size());
        intermittentSaleDAO.delete(intermittentSale1);
        assertEquals(0, intermittentSaleDAO.find(intermittentSale1).size());

        // intermittent sale 2
        IntermittentSale intermittentSale2 = createIntermittentSale(124L);

        intermittentSaleDAO.create(intermittentSale2);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale2).size());
        intermittentSaleDAO.delete(intermittentSale2);
        assertEquals(0, intermittentSaleDAO.find(intermittentSale2).size());

        // intermittent sale 3
        IntermittentSale intermittentSale3 = createIntermittentSale(125L);

        intermittentSaleDAO.create(intermittentSale3);
        assertEquals(1, intermittentSaleDAO.find(intermittentSale3).size());
        intermittentSaleDAO.delete(intermittentSale3);
        assertEquals(0, intermittentSaleDAO.find(intermittentSale3).size());

        List<IntermittentSale> salesList = Arrays.asList(intermittentSale1, intermittentSale2, intermittentSale3);

        // WHEN
        List<IntermittentSale> result = intermittentSaleDAO.populate(salesList);
        // THEN
        assertEquals(3, result.size());
        assertTrue(result.contains(intermittentSale1));
        assertTrue(result.contains(intermittentSale2));
        assertTrue(result.contains(intermittentSale3));
    }

    @Test
    public void testPopulate_nullListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // GIVEN
        List<IntermittentSale> invalidSales = null;

        // WHEN
        List<IntermittentSale> result = intermittentSaleDAO.populate(invalidSales);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPopulate_emptyListShouldReturnEmptyObjects() throws DAOException, ValidationException {
        // GIVEN
        List<IntermittentSale> invalidSales = new ArrayList<>();

        // WHEN
        List<IntermittentSale> result = intermittentSaleDAO.populate(invalidSales);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithInvalidObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<IntermittentSale> invalidSales = Arrays.asList(new IntermittentSale());

        // WHEN
        List<IntermittentSale> result = intermittentSaleDAO.populate(invalidSales);
    }

    @Test(expected = ValidationException.class)
    public void testPopulate_listWithNullObjectsShouldThrow() throws DAOException, ValidationException {
        // GIVEN
        List<IntermittentSale> invalidSales = new ArrayList<>();
        invalidSales.add(null);

        // WHEN
        List<IntermittentSale> result = intermittentSaleDAO.populate(invalidSales);
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN
        // create data
        IntermittentSale intermittentSale = createIntermittentSale(123L);
        LocalDateTime createTime = LocalDateTime.now();
        intermittentSaleDAO.create(intermittentSale);

        // update data
        IntermittentSale intermittentSale2 = createIntermittentSale(intermittentSale.getIdentity());
        intermittentSale2.setName("updated name");

        LocalDateTime updateTime = LocalDateTime.now();
        intermittentSaleDAO.update(intermittentSale2);

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        intermittentSaleDAO.delete(intermittentSale2);

        // WHEN
        List<History<IntermittentSale>> history = intermittentSaleDAO.getHistory(intermittentSale);

        // THEN
        assertEquals(3, history.size());

        // check create history
        History<IntermittentSale> entry = history.get(0);
        assertEquals(Long.valueOf(1), entry.getChangeNumber());
        assertEquals(intermittentSale, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(createTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check update history
        entry = history.get(1);
        assertEquals(Long.valueOf(2), entry.getChangeNumber());
        assertEquals(intermittentSale2, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(updateTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check delete history
        entry = history.get(2);
        assertEquals(Long.valueOf(3), entry.getChangeNumber());
        assertEquals(intermittentSale2, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(deleteTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertTrue(entry.isDeleted());
    }
}