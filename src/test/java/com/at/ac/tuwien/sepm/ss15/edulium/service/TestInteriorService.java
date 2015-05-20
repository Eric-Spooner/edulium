package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.fail;

/**
 * Unit test for InteriorService
 */
public class TestInteriorService extends AbstractServiceTest {
    @Autowired
    private InteriorService interiorService;
    @Autowired
    private DAO<User> userDAO;
    @Autowired
    private DAO<Section> sectionDAO;

    private User user1;
    private User user2;
    private User user3;
    private Section section1;
    private Section section2;
    private Section section3;

    @Before
    public void before() throws ValidationException, DAOException {
        user1 = new User();
        user2 = new User();
        user3 = new User();
        section1 = new Section();
        section2 = new Section();
        section3 = new Section();

        user1.setIdentity("A");
        user1.setName("User1");
        user1.setRole("Role1");
        userDAO.create(user1);
        user2.setIdentity("B");
        user2.setName("User2");
        user2.setRole("Role2");
        userDAO.create(user2);
        user3.setIdentity("C");
        user3.setName("User3");
        user3.setRole("Role3");
        userDAO.create(user3);

        section1.setIdentity((long) 1);
        section1.setName("Section1");
        sectionDAO.create(section1);
        section2.setIdentity((long) 2);
        section2.setName("Section2");
        sectionDAO.create(section2);
        section3.setIdentity((long) 3);
        section3.setName("Section3");
        sectionDAO.create(section3);
    }

    @Test
    public void testAddTable_shouldAddTable() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        interiorService.addTable(table);

        // THEN
        // check if identity is set
        Assert.assertNotNull(table.getNumber());

        // check retrieving object
        List<Table> storedObjects = interiorService.findTables(Table.withIdentity(section1, 1L));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(table, storedObjects.get(0));
    }

    /* user is optional */
    @Test
    public void testAddTable_shouldAddTableWithoutUser() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(null);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        interiorService.addTable(table);

        // THEN
        // check if identity is set
        Assert.assertNotNull(table.getNumber());

        // check retrieving object
        List<Table> storedObjects = interiorService.findTables(Table.withIdentity(section1, 1L));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(table, storedObjects.get(0));
    }

    @Test(expected = ServiceException.class)
    public void testAddTable_addingTwoTablesWithSameIdentityShouldFail() throws ServiceException {
        // PREPARE
        Table table1 = new Table();
        table1.setNumber(1L);
        table1.setSection(section1);
        table1.setUser(user1);
        table1.setSeats(3);
        table1.setColumn(4);
        table1.setRow(5);

        try {
            interiorService.addTable(table1);
        } catch (ServiceException e) {
            fail("ServiceException should not occur while adding a new table with a non-existing identity");
        }

        // check if table is stored
        Assert.assertEquals(1, interiorService.findTables(table1).size());

        // GIVEN
        Table table2 = new Table();
        table2.setNumber(1L); // same number
        table2.setSection(section1); // same section
        table2.setUser(user2);
        table2.setSeats(6);
        table2.setColumn(3);
        table2.setRow(2);

        // WHEN
        interiorService.addTable(table2);
    }

    @Test(expected = ServiceException.class)
    public void testAddTable_addingTableWithoutNumberShouldFail() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        interiorService.addTable(table);
    }

    @Test(expected = ServiceException.class)
    public void testAddTable_addingTableWithoutSectionShouldFail() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        interiorService.addTable(table);
    }

    @Test(expected = ServiceException.class)
    public void testAddTable_addingInvalidTableShouldFail() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(section1);

        // WHEN
        interiorService.addTable(table);
    }

    @Test(expected = ServiceException.class)
    public void testAddTable_addingNullTableShouldFail() throws ServiceException {
        // GIVEN
        Table table = null;

        // WHEN
        interiorService.addTable(table);
    }

    @Test
    public void testUpdateTable_shouldUpdateTable() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        interiorService.addTable(table);

        // check if cat is stored
        Assert.assertEquals(1, interiorService.findTables(table).size());

        // GIVEN
        table.setSeats(6);
        table.setColumn(7);
        table.setRow(8);
        table.setUser(user2);

        // WHEN
        interiorService.updateTable(table);

        // THEN
        // check if category name was updated
        List<Table> storedObjects = interiorService.findTables(Table.withIdentity(section1, 1L));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(table, storedObjects.get(0));
    }

    /* user is optional */
    @Test
    public void testUpdateTable_shouldUpdateTableWithoutUser() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        interiorService.addTable(table);

        // check if table is stored
        Assert.assertEquals(1, interiorService.findTables(table).size());

        // GIVEN
        table.setSeats(6);
        table.setColumn(7);
        table.setRow(8);
        table.setUser(null);

        // WHEN
        interiorService.updateTable(table);

        // THEN
        // check if table was updated
        List<Table> storedObjects = interiorService.findTables(Table.withIdentity(section1, 1L));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(table, storedObjects.get(0));
    }

    @Test(expected = ServiceException.class)
    public void testUpdateTable_updatingTableWithoutNumberShouldFail() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setUser(user1);
        table.setSection(section1);

        // WHEN
        interiorService.updateTable(table);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateTable_updatingTableWithoutSectionShouldFail() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setUser(user1);

        // WHEN
        interiorService.updateTable(table);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateTable_updatingInvalidTableShouldFail() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(section1);

        // WHEN
        interiorService.updateTable(table);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateTable_updatingNullTableShouldFail() throws ServiceException {
        // GIVEN
        Table table = null;

        // WHEN
        interiorService.updateTable(table);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateTable_updatingNotPersistentTableShouldFail() throws ServiceException {
        // GIVEN
        Table notPersitentTable = new Table();
        Long number = (long) 1;
        notPersitentTable.setNumber(number);
        notPersitentTable.setSection(section1);

        // check if no item with this number exists
        try {
            while (!interiorService.findTables(notPersitentTable).isEmpty()) {
                number++;
                notPersitentTable.setNumber(number);
            }
        } catch (ServiceException e) {
            // exception should not occur here
            Assert.assertTrue(false);
        }

        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setNumber(notPersitentTable.getNumber());
        table.setSection(section1);
        table.setUser(user1);

        // WHEN
        interiorService.updateTable(table);
    }

    @Test
    public void testDeleteTable_shouldDeleteTable() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 2);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setSection(section1);
        table.setUser(user1);

        interiorService.addTable(table);

        // check if table created
        Assert.assertEquals(1, interiorService.findTables(table).size());

        // WHEN
        interiorService.deleteTable(table);

        // check if category was removed
        Assert.assertEquals(0, interiorService.findTables(Table.withIdentity(section1, 2L)).size());
    }

    @Test(expected = ServiceException.class)
    public void testDeleteTable_deletingTableWithoutNumberShouldFail() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        interiorService.deleteTable(table);
    }

    @Test(expected = ServiceException.class)
    public void testDeleteTable_deletingTableWithoutSectionShouldFail() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        interiorService.deleteTable(table);
    }

    @Test(expected = ServiceException.class)
    public void testDeleteTable_deletingNotPersistentTableShouldFail() throws ServiceException {
        // GIVEN
        Table table = new Table();
        Long number = (long) 1;
        table.setNumber(number);
        table.setSection(section1);

        // check if no item with this number exists
        try {
            while (!interiorService.findTables(table).isEmpty()) {
                number++;
                table.setNumber(number);
            }
        } catch (ServiceException e) {
            // exception should not occur here
            Assert.fail();
        }

        // WHEN
        interiorService.deleteTable(table);
    }

    @Test
    public void testFindTable_byIdentityShouldReturnTable() throws ServiceException {
        // PREPARE
        Table table1 = new Table();
        table1.setSeats(3);
        table1.setColumn(14);
        table1.setRow(5);
        table1.setNumber((long) 1);
        table1.setUser(user2);
        table1.setSection(section3);

        Table table2 = new Table();
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table2.setNumber((long) 1);
        table2.setUser(user2);
        table2.setSection(section2);

        Table table3 = new Table();
        table3.setSeats(3);
        table3.setColumn(24);
        table3.setRow(15);
        table3.setNumber((long) 3);
        table3.setUser(user3);
        table3.setSection(section3);

        interiorService.addTable(table1);
        interiorService.addTable(table2);
        interiorService.addTable(table3);

        // WHEN
        List<Table> objects = interiorService.findTables(Table.withIdentity(section3, 1L));
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table1));

        // WHEN
        objects = interiorService.findTables(Table.withIdentity(section2, 1L));
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table2));

        // WHEN
        objects = interiorService.findTables(Table.withIdentity(section3, 3L));
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table3));
    }

    @Test
    public void testFindTable_byNumberShouldReturnTable() throws ServiceException {
        // PREPARE
        Table table1 = new Table();
        table1.setSeats(3);
        table1.setColumn(14);
        table1.setRow(5);
        table1.setNumber((long) 1);
        table1.setUser(user2);
        table1.setSection(section3);

        Table table2 = new Table();
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table2.setNumber((long) 1);
        table2.setUser(user2);
        table2.setSection(section2);

        Table table3 = new Table();
        table3.setSeats(3);
        table3.setColumn(24);
        table3.setRow(15);
        table3.setNumber((long) 3);
        table3.setUser(user3);
        table3.setSection(section3);

        interiorService.addTable(table1);
        interiorService.addTable(table2);
        interiorService.addTable(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setNumber(1L); // table 1 and table 2
        List<Table> objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table2));

        // WHEN
        matcher.setNumber(3L); // table 3
        objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table3));
    }

    @Test
    public void testFindTable_bySectionShouldReturnTable() throws ServiceException {
        // PREPARE
        Table table1 = new Table();
        table1.setSeats(3);
        table1.setColumn(14);
        table1.setRow(5);
        table1.setNumber((long) 1);
        table1.setUser(user2);
        table1.setSection(section3);

        Table table2 = new Table();
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table2.setNumber((long) 1);
        table2.setUser(user2);
        table2.setSection(section2);

        Table table3 = new Table();
        table3.setSeats(3);
        table3.setColumn(24);
        table3.setRow(15);
        table3.setNumber((long) 3);
        table3.setUser(user3);
        table3.setSection(section3);

        interiorService.addTable(table1);
        interiorService.addTable(table2);
        interiorService.addTable(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setSection(section3); // table 1 and table 3
        List<Table> objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table3));

        // WHEN
        matcher.setSection(section2); // table 2
        objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table2));
    }

    @Test
    public void testFindTable_byUserShouldReturnTable() throws ServiceException {
        // PREPARE
        Table table1 = new Table();
        table1.setSeats(3);
        table1.setColumn(14);
        table1.setRow(5);
        table1.setNumber((long) 1);
        table1.setUser(user2);
        table1.setSection(section3);

        Table table2 = new Table();
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table2.setNumber((long) 1);
        table2.setUser(user2);
        table2.setSection(section2);

        Table table3 = new Table();
        table3.setSeats(3);
        table3.setColumn(24);
        table3.setRow(15);
        table3.setNumber((long) 3);
        table3.setUser(user3);
        table3.setSection(section3);

        interiorService.addTable(table1);
        interiorService.addTable(table2);
        interiorService.addTable(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setUser(user2); // table 1 and table 2
        List<Table> objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table2));

        // WHEN
        matcher.setUser(user3); // table 3
        objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table3));
    }

    @Test
    public void testFindTable_bySeatsShouldReturnTable() throws ServiceException {
        // PREPARE
        Table table1 = new Table();
        table1.setSeats(3);
        table1.setColumn(14);
        table1.setRow(5);
        table1.setNumber((long) 1);
        table1.setUser(user2);
        table1.setSection(section3);

        Table table2 = new Table();
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table2.setNumber((long) 1);
        table2.setUser(user2);
        table2.setSection(section2);

        Table table3 = new Table();
        table3.setSeats(3);
        table3.setColumn(24);
        table3.setRow(15);
        table3.setNumber((long) 3);
        table3.setUser(user3);
        table3.setSection(section3);

        interiorService.addTable(table1);
        interiorService.addTable(table2);
        interiorService.addTable(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setSeats(3); // table 1 and table 3
        List<Table> objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table3));

        // WHEN
        matcher.setSeats(13); // table 2
        objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table2));
    }

    @Test
    public void testFindTable_byRowShouldReturnTable() throws ServiceException {
        // PREPARE
        Table table1 = new Table();
        table1.setSeats(3);
        table1.setColumn(14);
        table1.setRow(5);
        table1.setNumber((long) 1);
        table1.setUser(user2);
        table1.setSection(section3);

        Table table2 = new Table();
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table2.setNumber((long) 1);
        table2.setUser(user2);
        table2.setSection(section2);

        Table table3 = new Table();
        table3.setSeats(3);
        table3.setColumn(24);
        table3.setRow(15);
        table3.setNumber((long) 3);
        table3.setUser(user3);
        table3.setSection(section3);

        interiorService.addTable(table1);
        interiorService.addTable(table2);
        interiorService.addTable(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setRow(15); // table 2 and table 3
        List<Table> objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table2));
        Assert.assertTrue(objects.contains(table3));

        // WHEN
        matcher.setRow(5); // table 1
        objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table1));
    }

    @Test
    public void testFindTable_byColumnShouldReturnTable() throws ServiceException {
        // PREPARE
        Table table1 = new Table();
        table1.setSeats(3);
        table1.setColumn(14);
        table1.setRow(5);
        table1.setNumber((long) 1);
        table1.setUser(user2);
        table1.setSection(section3);

        Table table2 = new Table();
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table2.setNumber((long) 1);
        table2.setUser(user2);
        table2.setSection(section2);

        Table table3 = new Table();
        table3.setSeats(3);
        table3.setColumn(24);
        table3.setRow(15);
        table3.setNumber((long) 3);
        table3.setUser(user3);
        table3.setSection(section3);

        interiorService.addTable(table1);
        interiorService.addTable(table2);
        interiorService.addTable(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setColumn(14); // table 1 and table 2
        List<Table> objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table2));

        // WHEN
        matcher.setColumn(24); // table 3
        objects = interiorService.findTables(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table3));
    }

    @Test
    public void testFindTable_shouldReturnEmptyList() throws ServiceException {
        // GIVEN
        Long number = (long) 1;
        Table matcher = new Table();
        matcher.setNumber(Long.valueOf(1));

        // check if no item with this number exists
        try {
            while (!interiorService.findTables(matcher).isEmpty()) {
                number++;
                matcher.setNumber(number);
            }
        } catch (ServiceException e) {
            // exception should not occur here
            Assert.assertTrue(false);
        }

        // WHEN
        List<Table> storedObjects = interiorService.findTables(matcher);

        // THEN
        Assert.assertEquals(0, storedObjects.size());
    }

    @Test
    public void testFindNullTable_shouldReturnEmptyList() throws ServiceException {
        // WHEN
        List<Table> storedObjects = interiorService.findTables(null);

        // THEN
        Assert.assertTrue(storedObjects.isEmpty());
    }

    @Test
    public void testGetAllTables_shouldReturnTables() throws ServiceException {
        // GIVEN
        Table matcher = new Table();
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        table1.setSeats(3);
        table1.setColumn(4);
        table1.setRow(5);
        table1.setNumber((long) 1);
        table1.setUser(user1);
        table1.setSection(section1);
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table2.setNumber((long) 2);
        table2.setUser(user2);
        table2.setSection(section2);
        table3.setSeats(23);
        table3.setColumn(24);
        table3.setRow(25);
        table3.setNumber((long) 3);
        table3.setUser(user3);
        table3.setSection(section3);
        interiorService.addTable(table1);
        interiorService.addTable(table2);
        interiorService.addTable(table3);

        // WHEN
        List<Table> objects = interiorService.getAllTables();

        // THEN
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table2));
        Assert.assertTrue(objects.contains(table3));
    }

    /**
     * 1. add a new table
     * 2. delete the table
     * 3. add the table again (same section and same number)
     */
    @Test
    public void testAddTable_shouldAddRemoveAndReaddTable() throws ServiceException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        interiorService.addTable(table);
        // THEN
        Assert.assertEquals(1, interiorService.findTables(table).size());

        // WHEN
        interiorService.deleteTable(table);
        // THEN
        Assert.assertEquals(0, interiorService.findTables(table).size());

        // WHEN
        interiorService.addTable(table); // readd
        // THEN
        Assert.assertEquals(1, interiorService.findTables(table).size());
    }
    
    @Test
    public void testAddSection_shouldAddSection() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");

        // WHEN
        interiorService.addSection(section);

        // THEN
        // check if identity is set
        Assert.assertNotNull(section.getIdentity());

        // check retrieving object
        List<Section> storedObjects = interiorService.findSections(Section.withIdentity(section.getIdentity()));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(section, storedObjects.get(0));
    }

    @Test(expected = ServiceException.class)
    public void testAddSection_addingInvalidSectionShouldFail() throws ServiceException {
        // GIVEN
        Section section = new Section();

        // WHEN
        try {
            interiorService.addSection(section);
        } finally {
            Assert.assertNull(section.getIdentity());
        }
    }

    @Test(expected = ServiceException.class)
    public void testAddSection_addingNullSectionShouldFail() throws ServiceException {
        // GIVEN
        Section section = null;

        // WHEN
        interiorService.addSection(section);
    }

    @Test
    public void testUpdateSection_shouldUpdateSection() throws ServiceException {
        // PREPARE
        Section section = new Section();
        section.setName("section");
        interiorService.addSection(section);

        // check if section is stored
        Assert.assertEquals(1, interiorService.findSections(section).size());

        // GIVEN
        section.setName("newSection");

        // WHEN
        interiorService.updateSection(section);

        // THEN
        // check if section name was updated
        List<Section> storedObjects = interiorService.findSections(Section.withIdentity(section.getIdentity()));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(section, storedObjects.get(0));
    }

    @Test(expected = ServiceException.class)
    public void testUpdateSection_updatingSectionWithIdentityNullShouldFail() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");

        // WHEN
        interiorService.updateSection(section);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateSection_updatingInvalidSectionShouldFail() throws ServiceException {
        // GIVEN
        Section section = new Section();

        // WHEN
        interiorService.updateSection(section);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateSection_updatingNullSectionShouldFail() throws ServiceException {
        // GIVEN
        Section section = null;

        // WHEN
        interiorService.updateSection(section);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateSection_updatingNotPersistentSectionShouldFail() throws ServiceException {
        // GIVEN
        Section notPersitentSection = new Section();
        Long identity = (long) 1;
        notPersitentSection.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        try {
            while (!interiorService.findSections(notPersitentSection).isEmpty()) {
                identity++;
                notPersitentSection.setIdentity(identity);
            }
        } catch (ServiceException e) {
            // exception should not occur here
            Assert.fail();
        }

        Section section = new Section();
        section.setIdentity(notPersitentSection.getIdentity());
        section.setName("section");

        // WHEN
        interiorService.updateSection(section);
    }

    @Test
    public void testDeleteSection_shouldDeleteSection() throws ServiceException {
        // PREPARE
        Section section = new Section();
        section.setName("section");
        interiorService.addSection(section);

        // check if section added
        Assert.assertEquals(1, interiorService.findSections(section).size());

        // WHEN
        interiorService.deleteSection(section);

        // THEN
        // check if section was removed
        Assert.assertEquals(0, interiorService.findSections(section).size());
    }

    @Test(expected = ServiceException.class)
    public void testDeleteSection_deletingSectionWithIdentityNullShouldFail() throws ServiceException {
        // GIVEN
        Section section = new Section();

        // WHEN
        interiorService.deleteSection(section);
    }

    @Test(expected = ServiceException.class)
    public void testDeleteSection_deletingNullSectionShouldFail() throws ServiceException {
        // GIVEN
        Section section = null;

        // WHEN
        interiorService.deleteSection(section);
    }

    @Test(expected = ServiceException.class)
    public void testDeleteSection_deletingNotPersistentSectionShouldFail() throws ServiceException {
        // GIVEN
        Section section = new Section();
        Long identity = (long) 1;
        section.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        try {
            while (!interiorService.findSections(section).isEmpty()) {
                identity++;
                section.setIdentity(identity);
            }
        } catch (ServiceException e) {
            // exception should not occur here
            Assert.fail();
        }

        // WHEN
        interiorService.deleteSection(section);
    }

    @Test
    public void testFindSection_byIdentityShouldReturnSection() throws ServiceException {
        // GIVEN
        Section matcher = new Section();
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section1");
        section2.setName("section2");
        section3.setName("section3");
        interiorService.addSection(section1);
        interiorService.addSection(section2);
        interiorService.addSection(section3);

        // WHEN
        matcher.setIdentity(section1.getIdentity());
        List<Section> objects = interiorService.findSections(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(section1, objects.get(0));

        // WHEN
        matcher.setIdentity(section2.getIdentity());
        objects = interiorService.findSections(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(section2, objects.get(0));

        // WHEN
        matcher.setIdentity(section3.getIdentity());
        objects = interiorService.findSections(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(section3, objects.get(0));
    }

    @Test
    public void testFindSection_byNameShouldReturnSections() throws ServiceException {
        // GIVEN
        Section matcher = new Section();
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section");
        section2.setName("section");
        section3.setName("section2");
        interiorService.addSection(section1);
        interiorService.addSection(section2);
        interiorService.addSection(section3);

        // WHEN
        matcher.setName(section1.getName());
        List<Section> objects = interiorService.findSections(matcher);

        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(section1));
        Assert.assertTrue(objects.contains(section2));

        // WHEN
        matcher.setName(section3.getName());
        objects = interiorService.findSections(matcher);

        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(objects.get(0), section3);
    }

    @Test
    public void testFindSection_shouldReturnEmptyList() throws ServiceException {
        // GIVEN
        Long identity = (long) 1;
        Section matcher = new Section();
        matcher.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        while (!interiorService.findSections(matcher).isEmpty()) {
            identity++;
            matcher.setIdentity(identity);
        }

        // WHEN
        List<Section> storedObjects = interiorService.findSections(matcher);

        // THEN
        Assert.assertTrue(storedObjects.isEmpty());
    }

    @Test
    public void testFindNullSection_shouldReturnEmptyList() throws ServiceException {
        // WHEN
        List<Section> storedObjects = interiorService.findSections(null);

        // THEN
        Assert.assertTrue(storedObjects.isEmpty());
    }

    @Test
    public void testGetAllSections_shouldReturnSections() throws ServiceException {
        // GIVEN
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section1");
        section2.setName("section2");
        section3.setName("section3");
        interiorService.addSection(section1);
        interiorService.addSection(section2);
        interiorService.addSection(section3);

        // WHEN
        List<Section> objects = interiorService.getAllSections();

        // THEN
        Assert.assertTrue(objects.contains(section1));
        Assert.assertTrue(objects.contains(section2));
        Assert.assertTrue(objects.contains(section3));
    }
}
