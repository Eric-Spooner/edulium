package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Test for the TableDAO
 */
public class TestTableDAO extends AbstractDAOTest {
    @Autowired
    private DAO<Table> tableDAO;
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
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        tableDAO.create(table);

        // THEN
        // check if identity is set
        Assert.assertNotNull(table.getNumber());

        // check retrieving object
        List<Table> storedObjects = tableDAO.find(Table.withIdentity(section1, 1L));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(table, storedObjects.get(0));
    }

    /* user is optional */
    @Test
    public void testCreate_shouldAddObjectWithoutUser() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(null);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        tableDAO.create(table);

        // THEN
        // check if identity is set
        Assert.assertNotNull(table.getNumber());

        // check retrieving object
        List<Table> storedObjects = tableDAO.find(Table.withIdentity(section1, 1L));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(table, storedObjects.get(0));
    }

    @Test(expected = DAOException.class)
    public void testCreate_addingTwoObjectsWithSameIdentityShouldFail() throws DAOException, ValidationException {
        // PREPARE
        Table table1 = new Table();
        table1.setNumber(1L);
        table1.setSection(section1);
        table1.setUser(user1);
        table1.setSeats(3);
        table1.setColumn(4);
        table1.setRow(5);

        try {
            tableDAO.create(table1);
        } catch (DAOException e) {
            fail("DAOException should not occur while adding a new table with a non-existing identity");
        }

        // check if table is stored
        assertEquals(1, tableDAO.find(table1).size());

        // GIVEN
        Table table2 = new Table();
        table2.setNumber(1L); // same number
        table2.setSection(section1); // same section
        table2.setUser(user2);
        table2.setSeats(6);
        table2.setColumn(3);
        table2.setRow(2);

        // WHEN
        tableDAO.create(table2);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutNumberShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        tableDAO.create(table);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutSectionShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        tableDAO.create(table);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingInvalidObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(section1);

        // WHEN
        tableDAO.create(table);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = null;

        // WHEN
        tableDAO.create(table);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        tableDAO.create(table);

        // check if cat is stored
        assertEquals(1, tableDAO.find(table).size());

        // GIVEN
        table.setSeats(6);
        table.setColumn(7);
        table.setRow(8);
        table.setUser(user2);

        // WHEN
        tableDAO.update(table);

        // THEN
        // check if category name was updated
        List<Table> storedObjects = tableDAO.find(Table.withIdentity(section1, 1L));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(table, storedObjects.get(0));
    }

    /* user is optional */
    @Test
    public void testUpdate_shouldUpdateObjectWithoutUser() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        tableDAO.create(table);

        // check if table is stored
        assertEquals(1, tableDAO.find(table).size());

        // GIVEN
        table.setSeats(6);
        table.setColumn(7);
        table.setRow(8);
        table.setUser(null);

        // WHEN
        tableDAO.update(table);

        // THEN
        // check if table was updated
        List<Table> storedObjects = tableDAO.find(Table.withIdentity(section1, 1L));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(table, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutNumberShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setUser(user1);
        table.setSection(section1);

        // WHEN
        tableDAO.update(table);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutSectionShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setUser(user1);

        // WHEN
        tableDAO.update(table);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingInvalidObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(section1);

        // WHEN
        tableDAO.update(table);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = null;

        // WHEN
        tableDAO.update(table);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table notPersitentTable = new Table();
        Long number = (long) 1;
        notPersitentTable.setNumber(number);
        notPersitentTable.setSection(section1);

        // check if no item with this number exists
        try {
            while (!tableDAO.find(notPersitentTable).isEmpty()) {
                number++;
                notPersitentTable.setNumber(number);
            }
        } catch (DAOException e) {
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
        tableDAO.update(table);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber((long) 2);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setSection(section1);
        table.setUser(user1);

        tableDAO.create(table);

        // check if table created
        assertEquals(1, tableDAO.find(table).size());

        // WHEN
        tableDAO.delete(table);

        // check if category was removed
        Assert.assertEquals(0, tableDAO.find(Table.withIdentity(section1, 2L)).size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithoutNumberShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        tableDAO.delete(table);
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithoutSectionShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        tableDAO.delete(table);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        Long number = (long) 1;
        table.setNumber(number);
        table.setSection(section1);

        // check if no item with this number exists
        try {
            while (!tableDAO.find(table).isEmpty()) {
                number++;
                table.setNumber(number);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.fail();
        }

        // WHEN
        tableDAO.delete(table);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
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

        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        List<Table> objects = tableDAO.find(Table.withIdentity(section3, 1L));
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table1));

        // WHEN
        objects = tableDAO.find(Table.withIdentity(section2, 1L));
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table2));

        // WHEN
        objects = tableDAO.find(Table.withIdentity(section3, 3L));
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table3));
    }

    @Test
    public void testFind_byNumberShouldReturnObject() throws DAOException, ValidationException {
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

        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setNumber(1L); // table 1 and table 2
        List<Table> objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table2));

        // WHEN
        matcher.setNumber(3L); // table 3
        objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table3));
    }

    @Test
    public void testFind_bySectionShouldReturnObject() throws DAOException, ValidationException {
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

        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setSection(section3); // table 1 and table 3
        List<Table> objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table3));

        // WHEN
        matcher.setSection(section2); // table 2
        objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table2));
    }

    @Test
    public void testFind_byUserShouldReturnObject() throws DAOException, ValidationException {
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

        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setUser(user2); // table 1 and table 2
        List<Table> objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table2));

        // WHEN
        matcher.setUser(user3); // table 3
        objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table3));
    }

    @Test
    public void testFind_bySeatsShouldReturnObject() throws DAOException, ValidationException {
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

        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setSeats(3); // table 1 and table 3
        List<Table> objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table3));

        // WHEN
        matcher.setSeats(13); // table 2
        objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table2));
    }

    @Test
    public void testFind_byRowShouldReturnObject() throws DAOException, ValidationException {
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

        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setRow(15); // table 2 and table 3
        List<Table> objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table2));
        Assert.assertTrue(objects.contains(table3));

        // WHEN
        matcher.setRow(5); // table 1
        objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table1));
    }

    @Test
    public void testFind_byColumnShouldReturnObject() throws DAOException, ValidationException {
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

        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setColumn(14); // table 1 and table 2
        List<Table> objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table2));

        // WHEN
        matcher.setColumn(24); // table 3
        objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertTrue(objects.contains(table3));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        Long number = (long) 1;
        Table matcher = new Table();
        matcher.setNumber(Long.valueOf(1));

        // check if no item with this number exists
        try {
            while (!tableDAO.find(matcher).isEmpty()) {
                number++;
                matcher.setNumber(number);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.assertTrue(false);
        }

        // WHEN
        List<Table> storedObjects = tableDAO.find(matcher);

        // THEN
        Assert.assertEquals(0, storedObjects.size());
    }

    @Test
    public void testFindNull_shouldReturnEmptyList() throws DAOException {
        // WHEN
        List<Table> storedObjects = tableDAO.find(null);

        // THEN
        Assert.assertTrue(storedObjects.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
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
        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        List<Table> objects = tableDAO.getAll();

        // THEN
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table2));
        Assert.assertTrue(objects.contains(table3));
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        tableDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutNumberShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setSection(section2);

        // WHEN
        tableDAO.getHistory(table);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutSectionShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);

        // WHEN
        tableDAO.getHistory(table);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        Long number = (long) 1;
        Table table = new Table();
        table.setSection(section2);
        table.setNumber(number);

        // generate identity which is not used by any persistent object
        while (!tableDAO.find(table).isEmpty()) {
            number++;
            table.setNumber(number);
        }

        // WHEN / THEN
        assertTrue(tableDAO.getHistory(table).isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN
        // create data
        Table table1 = new Table();
        table1.setSeats(3);
        table1.setColumn(4);
        table1.setRow(5);
        table1.setNumber((long) 1);
        table1.setUser(user1);
        table1.setSection(section1);
        LocalDateTime createTime = LocalDateTime.now();
        tableDAO.create(table1);

        // update data
        Table table2 = Table.withIdentity(section1, table1.getNumber());
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table2.setUser(user2);
        LocalDateTime updateTime = LocalDateTime.now();
        tableDAO.update(table2);

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        tableDAO.delete(table2);

        // WHEN
        List<History<Table>> history = tableDAO.getHistory(table1);

        // THEN
        assertEquals(3, history.size());

        // check create history
        History<Table> entry = history.get(0);
        assertEquals(Long.valueOf(1), entry.getChangeNumber());
        assertEquals(table1, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(createTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check update history
        entry = history.get(1);
        assertEquals(Long.valueOf(2), entry.getChangeNumber());
        assertEquals(table2, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(updateTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check delete history
        entry = history.get(2);
        assertEquals(Long.valueOf(3), entry.getChangeNumber());
        assertEquals(table2, entry.getData());
        assertEquals(user, entry.getUser());
        assertTrue(Duration.between(deleteTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertTrue(entry.isDeleted());
    }

    /**
     * 1. add a new table
     * 2. delete the table
     * 3. add the table again (same section and same number)
     */
    @Test
    public void testCreate_shouldAddRemoveAndReaddObject() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        tableDAO.create(table);
        // THEN
        Assert.assertEquals(1, tableDAO.find(table).size());

        // WHEN
        tableDAO.delete(table);
        // THEN
        Assert.assertEquals(0, tableDAO.find(table).size());

        // WHEN
        tableDAO.create(table); // readd
        // THEN
        Assert.assertEquals(1, tableDAO.find(table).size());
    }
}