package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.TableDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Unit Test for the TableDAO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/Spring-DAO.xml")
@Transactional
public class TestTableDAO {
    @Autowired
    private TableDAO tableDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setSection_id((long)1);
        table.setUser_id((long)2);
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

    @Test(expected = DAOException.class)
    public void testCreate_addingObjectWithoutSeatsAndRowAndColumnShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();

        // WHEN
        try {
            tableDAO.create(table);
        } finally {
            Assert.assertNull(table.getNumber());
        }
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setSection_id((long)1);
        table.setUser_id((long)2);
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

    @Test(expected = DAOException.class)
    public void testUpdate_updatingObjectWithNumberNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        tableDAO.update(table);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        Long number = (long) 1;
        table.setNumber(number);

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
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        tableDAO.create(table);

        // WHEN
        tableDAO.delete(table);

        // check if category was removed
        Table matcher = new Table();
        matcher.setNumber(table.getNumber());

        Assert.assertEquals(tableDAO.find(matcher).size(), 0);
        Assert.assertEquals(tableDAO.getAll().size(), 0);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingObjectWithNumberNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();

        // WHEN
        tableDAO.delete(table);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Table table = new Table();
        Long number = (long) 1;
        table.setNumber(number);

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
        tableDAO.delete(table);
    }

    @Test
    public void testFind_byNumberShouldReturnObject() throws DAOException, ValidationException {
        // GIVEN
        Table matcher = new Table();
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        table1.setSeats(3);
        table1.setColumn(4);
        table1.setRow(5);
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table3.setSeats(23);
        table3.setColumn(24);
        table3.setRow(25);
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
        Assert.assertEquals(objects.get(0), table3);

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
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        table1.setSeats(3);
        table1.setColumn(4);
        table1.setRow(5);
        table2.setSeats(3);
        table2.setColumn(4);
        table2.setRow(5);
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
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        table1.setSeats(3);
        table1.setColumn(4);
        table1.setRow(5);
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        table3.setSeats(23);
        table3.setColumn(24);
        table3.setRow(25);
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

}
