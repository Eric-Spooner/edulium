package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Assert;
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
        before();
        Table table = new Table();
        table.setNumber((long)1);
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
        Table matcher = new Table();
        matcher.setNumber(table.getNumber());
        List<Table> storedObjects = tableDAO.find(matcher);
        Assert.assertEquals(storedObjects.size(), 1);
        Assert.assertEquals(storedObjects.get(0), table);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutNumberShouldFail() throws DAOException, ValidationException {
        // GIVEN
        before();
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
        before();
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
    public void testCreate_addingObjectWithoutUserShouldFail() throws DAOException, ValidationException {
        // GIVEN
        before();
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        tableDAO.create(table);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutSeatsShouldFail() throws DAOException, ValidationException {
        // GIVEN
        before();
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(user1);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        tableDAO.create(table);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutColumnShouldFail() throws DAOException, ValidationException {
        // GIVEN
        before();
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setRow(5);

        // WHEN
        tableDAO.create(table);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutRowShouldFail() throws DAOException, ValidationException {
        // GIVEN
        before();
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);

        // WHEN
        tableDAO.create(table);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // GIVEN
        before();
        Table table = new Table();
        table.setNumber((long)1);
        table.setSection(section1);
        table.setUser(user1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        tableDAO.create(table);

        // check if cat is stored
        Table matcher = new Table();
        matcher.setNumber(table.getNumber());
        Assert.assertEquals(tableDAO.find(matcher).get(0), table);

        // WHEN
        table.setSeats(6);
        table.setColumn(7);
        table.setRow(8);
        tableDAO.update(table);

        // THEN
        // check if category name was updated
        List<Table> storedObjects = tableDAO.find(matcher);
        Assert.assertEquals(storedObjects.size(), 1);
        Assert.assertEquals(storedObjects.get(0), table);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithNumberNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        before();
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setUser(user1);
        table.setSection(section1);

        // WHEN
        tableDAO.update(table);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        before();
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        Long number = (long) 1;
        table.setNumber(number);
        table.setSection(section1);
        table.setUser(user1);

        // check if no item with this number exists
        try {
            while (!tableDAO.find(table).isEmpty()) {
                number++;
                table.setNumber(number);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.assertTrue(false);
        }

        // WHEN
        tableDAO.update(table);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // GIVEN
        before();
        Table table = new Table();
        table.setNumber((long)2);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setSection(section1);
        table.setUser(user1);

        tableDAO.create(table);

        // check if table created
        List<Table> objects = tableDAO.find(Table.withNumber(table.getNumber()));
        assertEquals(objects.size(), 1);
        assertEquals(objects.get(0), table);

        // WHEN
        tableDAO.delete(table);

        // check if category was removed
        Table matcher = new Table();
        matcher.setNumber(table.getNumber());

        Assert.assertEquals(tableDAO.find(matcher).size(), 0);
        Assert.assertEquals(tableDAO.getAll().size(), 0);
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithNumberNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        before();
        Table table = new Table();
        table.setSection(section1);
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
        before();
        Table table = new Table();
        Long number = (long) 1;
        table.setNumber(number);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setSection(section1);
        table.setUser(user1);

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
    public void testFind_byNumberShouldReturnObject() throws DAOException, ValidationException {
        // GIVEN
        before();
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
        table2.setNumber((long)2);
        table2.setUser(user2);
        table2.setSection(section2);
        table3.setSeats(23);
        table3.setColumn(24);
        table3.setRow(25);
        table3.setNumber((long)3);
        table3.setUser(user3);
        table3.setSection(section3);
        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        matcher.setNumber(table1.getNumber());
        List<Table> objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), table1);

        // WHEN
        matcher.setNumber(table2.getNumber());
        objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), table2);

        // WHEN
        matcher.setNumber(table3.getNumber());
        objects = tableDAO.find(matcher);
        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), table3);
    }

    @Test
    public void testFind_bySeatsAndRowAndColumnShouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        before();
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        table1.setNumber((long)1);
        table1.setSection(section1);
        table1.setUser(user1);
        table1.setSeats(3);
        table1.setColumn(4);
        table1.setRow(5);
        table2.setNumber((long)2);
        table2.setSection(section2);
        table2.setUser(user2);
        table2.setSeats(3);
        table2.setColumn(4);
        table2.setRow(5);
        table3.setNumber((long)3);
        table3.setSection(section3);
        table3.setUser(user3);
        table3.setSeats(23);
        table3.setColumn(24);
        table3.setRow(25);
        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        Table matcher = new Table();
        matcher.setSeats(table1.getSeats());
        matcher.setColumn(table1.getColumn());
        matcher.setRow(table1.getRow());
        List<Table> objects = tableDAO.find(matcher);

        // THEN
        Assert.assertEquals(objects.size(), 2);
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table2));

        // WHEN
        matcher.setSeats(table3.getSeats());
        matcher.setColumn(table3.getColumn());
        matcher.setRow(table3.getRow());
        objects = tableDAO.find(matcher);

        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), table3);
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
        Assert.assertEquals(storedObjects.size(), 0);
    }

    @Test
    public void testGetAll_shouldReturnEmptyList() throws DAOException {
        // WHEN / THEN
        Assert.assertEquals(tableDAO.getAll().size(), 0);
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        before();
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
        table2.setNumber((long)2);
        table2.setUser(user2);
        table2.setSection(section2);
        table3.setSeats(23);
        table3.setColumn(24);
        table3.setRow(25);
        table3.setNumber((long)3);
        table3.setUser(user3);
        table3.setSection(section3);
        tableDAO.create(table1);
        tableDAO.create(table2);
        tableDAO.create(table3);

        // WHEN
        List<Table> objects = tableDAO.getAll();

        // THEN
        Assert.assertEquals(objects.size(), 3);
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

        // WHEN
        tableDAO.getHistory(table);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        Long number = (long) 1;
        Table table = new Table();
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
        before();
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
        Table table2 = Table.withNumber(table1.getNumber());
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table2.setUser(user2);
        table2.setSection(section2);
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
}