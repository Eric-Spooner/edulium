package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.TableDAO;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
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
    public void testCreate_shouldAddObject() throws DAOException {
        // GIVEN
        Table table = new Table();
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
        Assert.assertEquals(tableDAO.find(matcher), table);
    }

    @Test(expected = DAOException.class)
    public void testCreate_addingObjectWithoutSeatsAndRowAndColumnShouldFail() throws DAOException {
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
    public void testUpdate_shouldUpdateObject() throws DAOException {
        // GIVEN
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        tableDAO.create(table);

        // WHEN
        table.setSeats(6);
        table.setColumn(7);
        table.setRow(8);
        tableDAO.update(table);

        // THEN
        // check if category name was updated
        Table matcher = new Table();
        matcher.setNumber(table.getNumber());
        Assert.assertEquals(tableDAO.find(matcher).get(0).getSeats(), 6);
        Assert.assertEquals(tableDAO.find(matcher).get(0).getColumn(), 7);
        Assert.assertEquals(tableDAO.find(matcher).get(0).getRow(), 8);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingObjectWithNumberNullShouldFail() throws DAOException {
        // GIVEN
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        table.setSeats(6);
        table.setColumn(7);
        table.setRow(8);
        tableDAO.update(table);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException {
        // GIVEN
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);
        table.setNumber(Long.valueOf(99999));

        // WHEN
        table.setSeats(6);
        table.setColumn(7);
        table.setRow(8);
        tableDAO.update(table);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException {
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
    public void testDelete_deletingObjectWithNumberNullShouldFail() throws DAOException {
        // GIVEN
        Table table = new Table();

        // WHEN
        tableDAO.delete(table);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(Long.valueOf(99999));

        // WHEN
        tableDAO.delete(table);
    }

    @Test
    public void testFind_byNumberShouldReturnObject() throws DAOException {
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
        Table matcher = new Table();

        matcher.setNumber(table1.getNumber());
        Assert.assertEquals(tableDAO.find(matcher).get(0), table1);

        matcher.setNumber(table2.getNumber());
        Assert.assertEquals(tableDAO.find(matcher).get(0), table2);

        matcher.setNumber(table3.getNumber());
        Assert.assertEquals(tableDAO.find(matcher).get(0), table3);
    }

    @Test
    public void testFind_bySeatsAndRowAndColumnShouldReturnObjects() throws DAOException {
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
        Assert.assertEquals(tableDAO.find(matcher).get(0), table3);
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        //WHEN
        Table matcher = new Table();
        matcher.setNumber(Long.valueOf(1));
        Assert.assertEquals(tableDAO.find(matcher).size(), 0);
    }

    @Test
    public void testGetAll_shouldReturnEmptyList() throws DAOException {
        //WHEN
        Assert.assertEquals(tableDAO.getAll().size(), 0);
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException {
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
