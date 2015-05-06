package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit Test for the SectionDAO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/Spring-DAO.xml")
@Transactional
public class TestSectionDAO {
    @Autowired
    private SectionDAO sectionDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        Table table = new Table();
        table.setNumber((long)3);
        section.addTable(table);

        // WHEN
        sectionDAO.create(section);

        // THEN
        // check if identity is set
        Assert.assertNotNull(section.getIdentity());

        // check retrieving object
        Section matcher = new Section();
        matcher.setIdentity(section.getIdentity());
        List<Section> storedObjects = sectionDAO.find(matcher);
        Assert.assertEquals(storedObjects.size(), 1);
        Assert.assertEquals(storedObjects.get(0), section);
    }

    @Test(expected = DAOException.class)
    public void testCreate_addingObjectWithoutNameShouldFail() throws DAOException {
        // GIVEN
        Section section = new Section();

        // WHEN
        try {
            sectionDAO.create(section);
        } finally {
            Assert.assertNull(section.getIdentity());
        }
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        Table table = new Table();
        table.setNumber((long) 3);
        section.addTable(table);
        sectionDAO.create(section);

        // check if section is stored
        Section matcher = new Section();
        matcher.setIdentity(section.getIdentity());
        Assert.assertEquals(sectionDAO.find(matcher).get(0), section);

        // WHEN
        section.setName("newSection");
        section.getTables().get(0).setNumber((long)4);
        sectionDAO.update(section);

        // THEN
        // check if section name was updated
        List<Section> storedObjects = sectionDAO.find(matcher);
        Assert.assertEquals(storedObjects.size(), 1);
        Assert.assertEquals(storedObjects.get(0), section);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        Table table = new Table();
        table.setNumber((long) 3);
        section.addTable(table);

        // WHEN
        sectionDAO.update(section);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException {
        // GIVEN
        Section section = new Section();
        Long identity = (long) 1;
        section.setIdentity(identity);
        section.setName("section");
        Table table = new Table();
        table.setNumber((long) 3);
        section.addTable(table);

        // check if no item with the id IDENTITY exists
        try {
            while (!sectionDAO.find(section).isEmpty()) {
                identity++;
                section.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.assertTrue(false);
        }

        // WHEN
        sectionDAO.update(section);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        Table table = new Table();
        table.setNumber((long) 3);
        section.addTable(table);
        sectionDAO.create(section);

        // WHEN
        sectionDAO.delete(section);

        // check if section was removed
        Section matcher = new Section();
        matcher.setIdentity(section.getIdentity());

        // THEN
        Assert.assertEquals(sectionDAO.find(matcher).size(), 0);
        Assert.assertEquals(sectionDAO.getAll().size(), 0);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException {
        // GIVEN
        Section section = new Section();

        // WHEN
        sectionDAO.delete(section);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException {
        // GIVEN
        Section section = new Section();
        Long identity = (long) 1;
        section.setIdentity(identity);
        Table table = new Table();
        table.setNumber((long)3);
        section.addTable(table);

        // check if no item with the id IDENTITY exists
        try {
            while (!sectionDAO.find(section).isEmpty()) {
                identity++;
                section.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.assertTrue(false);
        }

        // WHEN
        sectionDAO.delete(section);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException {
        // GIVEN
        Section matcher = new Section();
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section1");
        section2.setName("section2");
        section3.setName("section3");
        Table table1 = new Table();
        table1.setNumber((long)3);
        section1.addTable(table1);
        Table table2 = new Table();
        table2.setNumber((long)4);
        section2.addTable(table2);
        Table table3 = new Table();
        table3.setNumber((long)5);
        section3.addTable(table3);
        sectionDAO.create(section1);
        sectionDAO.create(section2);
        sectionDAO.create(section3);

        // WHEN
        matcher.setIdentity(section1.getIdentity());
        List<Section> objects = sectionDAO.find(matcher);
        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), section1);

        // WHEN
        matcher.setIdentity(section2.getIdentity());
        objects = sectionDAO.find(matcher);
        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), section2);

        // WHEN
        matcher.setIdentity(section3.getIdentity());
        objects = sectionDAO.find(matcher);
        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), section3);
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException {
        // GIVEN
        Section matcher = new Section();
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section");
        section2.setName("section");
        section3.setName("section2");
        Table table1 = new Table();
        table1.setNumber((long)3);
        section1.addTable(table1);
        Table table2 = new Table();
        table2.setNumber((long)3);
        section2.addTable(table2);
        Table table3 = new Table();
        table3.setNumber((long)5);
        section3.addTable(table3);
        sectionDAO.create(section1);
        sectionDAO.create(section2);
        sectionDAO.create(section3);

        // WHEN
        matcher.setName(section1.getName());
        List<Section> objects = sectionDAO.find(matcher);

        // THEN
        Assert.assertEquals(objects.size(), 2);
        Assert.assertTrue(objects.contains(section1));
        Assert.assertTrue(objects.contains(section2));

        // WHEN
        matcher.setName(section3.getName());
        objects = sectionDAO.find(matcher);

        // THEN
        Assert.assertEquals(objects.size(), 1);
        Assert.assertEquals(objects.get(0), section3);
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        Long identity = (long) 1;
        Section matcher = new Section();
        matcher.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        try {
            while (!sectionDAO.find(matcher).isEmpty()) {
                identity++;
                matcher.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.assertTrue(false);
        }

        // WHEN
        List<Section> storedObjects = sectionDAO.find(matcher);

        // THEN
        Assert.assertEquals(storedObjects.size(), 0);
    }

    @Test
    public void testGetAll_shouldReturnEmptyList() throws DAOException {
        // WHEN / THEN
        Assert.assertEquals(sectionDAO.getAll().size(), 0);
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException {
        // GIVEN
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section1");
        section2.setName("section2");
        section3.setName("section3");
        Table table1 = new Table();
        table1.setNumber((long)3);
        section1.addTable(table1);
        Table table2 = new Table();
        table2.setNumber((long)4);
        section2.addTable(table2);
        Table table3 = new Table();
        table3.setNumber((long)5);
        section3.addTable(table3);
        sectionDAO.create(section1);
        sectionDAO.create(section2);
        sectionDAO.create(section3);

        // WHEN
        List<Section> objects = sectionDAO.getAll();

        // THEN
        Assert.assertEquals(objects.size(), 3);
        Assert.assertTrue(objects.contains(section1));
        Assert.assertTrue(objects.contains(section2));
        Assert.assertTrue(objects.contains(section3));
    }
}
