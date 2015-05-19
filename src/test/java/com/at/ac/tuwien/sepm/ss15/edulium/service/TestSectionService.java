package com.at.ac.tuwien.sepm.ss15.edulium.service;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.AbstractDAOTest;
import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAO;
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

/**
 * Unit Test for the SectionDAO
 */
public class TestSectionService extends AbstractDAOTest {
    @Autowired
    private SectionService sectionService;
    @Autowired
    private TableService tableService;

    @Test
    public void testAdd_shouldAddObject() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");

        // WHEN
        sectionService.addSection(section);

        // THEN
        // check if identity is set
        Assert.assertNotNull(section.getIdentity());

        // check retrieving object
        List<Section> storedObjects = sectionService.findSections(Section.withIdentity(section.getIdentity()));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(section, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testAdd_addingInvalidObjectShouldFail() throws ServiceException {
        // GIVEN
        Section section = new Section();

        // WHEN
        try {
            sectionService.addSection(section);
        } finally {
            Assert.assertNull(section.getIdentity());
        }
    }

    @Test(expected = ValidationException.class)
    public void testAdd_addingNullObjectShouldFail() throws ServiceException {
        // GIVEN
        Section section = null;

        // WHEN
        sectionService.addSection(section);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws ServiceException {
        // PREPARE
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);

        // check if section is stored
        Assert.assertEquals(1, sectionService.findSections(section).size());

        // GIVEN
        section.setName("newSection");

        // WHEN
        sectionService.updateSection(section);

        // THEN
        // check if section name was updated
        List<Section> storedObjects = sectionService.findSections(Section.withIdentity(section.getIdentity()));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(section, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");

        // WHEN
        sectionService.updateSection(section);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingInvalidObjectShouldFail() throws ServiceException {
        // GIVEN
        Section section = new Section();

        // WHEN
        sectionService.updateSection(section);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws ServiceException {
        // GIVEN
        Section section = null;

        // WHEN
        sectionService.updateSection(section);
    }

    @Test(expected = ServiceException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws ServiceException {
        // GIVEN
        Section notPersitentSection = new Section();
        Long identity = (long) 1;
        notPersitentSection.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        try {
            while (!sectionService.findSections(notPersitentSection).isEmpty()) {
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
        sectionService.updateSection(section);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws ServiceException {
        // PREPARE
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);

        // check if section added
        Assert.assertEquals(1, sectionService.findSections(section).size());

        // WHEN
        sectionService.deleteSection(section);

        // THEN
        // check if section was removed
        Assert.assertEquals(0, sectionService.findSections(section).size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws ServiceException {
        // GIVEN
        Section section = new Section();

        // WHEN
        sectionService.deleteSection(section);
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws ServiceException {
        // GIVEN
        Section section = null;

        // WHEN
        sectionService.deleteSection(section);
    }

    @Test(expected = ServiceException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws ServiceException {
        // GIVEN
        Section section = new Section();
        Long identity = (long) 1;
        section.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        try {
            while (!sectionService.findSections(section).isEmpty()) {
                identity++;
                section.setIdentity(identity);
            }
        } catch (ServiceException e) {
            // exception should not occur here
            Assert.fail();
        }

        // WHEN
        sectionService.deleteSection(section);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws ServiceException {
        // GIVEN
        Section matcher = new Section();
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section1");
        section2.setName("section2");
        section3.setName("section3");
        sectionService.addSection(section1);
        sectionService.addSection(section2);
        sectionService.addSection(section3);

        // WHEN
        matcher.setIdentity(section1.getIdentity());
        List<Section> objects = sectionService.findSections(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(section1, objects.get(0));

        // WHEN
        matcher.setIdentity(section2.getIdentity());
        objects = sectionService.findSections(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(section2, objects.get(0));

        // WHEN
        matcher.setIdentity(section3.getIdentity());
        objects = sectionService.findSections(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(section3, objects.get(0));
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws ServiceException {
        // GIVEN
        Section matcher = new Section();
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section");
        section2.setName("section");
        section3.setName("section2");
        sectionService.addSection(section1);
        sectionService.addSection(section2);
        sectionService.addSection(section3);

        // WHEN
        matcher.setName(section1.getName());
        List<Section> objects = sectionService.findSections(matcher);

        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(section1));
        Assert.assertTrue(objects.contains(section2));

        // WHEN
        matcher.setName(section3.getName());
        objects = sectionService.findSections(matcher);

        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(objects.get(0), section3);
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws ServiceException {
        // GIVEN
        Long identity = (long) 1;
        Section matcher = new Section();
        matcher.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        while (!sectionService.findSections(matcher).isEmpty()) {
            identity++;
            matcher.setIdentity(identity);
        }

        // WHEN
        List<Section> storedObjects = sectionService.findSections(matcher);

        // THEN
        Assert.assertTrue(storedObjects.isEmpty());
    }

    @Test
    public void testFindNull_shouldReturnEmptyList() throws ServiceException {
        // WHEN
        List<Section> storedObjects = sectionService.findSections(null);

        // THEN
        Assert.assertTrue(storedObjects.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws ServiceException {
        // GIVEN
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section1");
        section2.setName("section2");
        section3.setName("section3");
        sectionService.addSection(section1);
        sectionService.addSection(section2);
        sectionService.addSection(section3);

        // WHEN
        List<Section> objects = sectionService.getAllSections();

        // THEN
        Assert.assertTrue(objects.contains(section1));
        Assert.assertTrue(objects.contains(section2));
        Assert.assertTrue(objects.contains(section3));
    }

    @Test
    public void testAddTable_shouldAddTable() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        sectionService.addTable(section, table);

        // THEN
        Table matcher = new Table();
        matcher.setNumber(table.getNumber());
        matcher.setSection(section);


        // check retrieved object
        List<Table> storedObjects = tableService.findTables(matcher);
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(table, storedObjects.get(0));
    }

    @Test(expected = ServiceException.class)
    public void testAddNullTable_shouldThrow() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);

        // WHEN
        sectionService.addTable(section, null);
    }

    @Test(expected = ServiceException.class)
    public void testAddTableWithoutNumber_shouldThrow() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);
        Table table = new Table();
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        sectionService.addTable(section, table);
    }

    @Test(expected = ServiceException.class)
    public void testAddTableWithoutSeats_shouldThrow() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);
        Table table = new Table();
        table.setNumber((long) 1);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        sectionService.addTable(section, table);
    }

    @Test(expected = ServiceException.class)
    public void testAddTableWithoutColumn_shouldThrow() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSeats(3);
        table.setRow(5);

        // WHEN
        sectionService.addTable(section, table);
    }

    @Test(expected = ServiceException.class)
    public void testAddTableWithoutRow_shouldThrow() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSeats(3);
        table.setColumn(4);

        // WHEN
        sectionService.addTable(section, table);
    }

    @Test(expected = ServiceException.class)
    public void testAddTableWithNegativeNumber_shouldThrow() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);
        Table table = new Table();
        table.setNumber((long) -1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        sectionService.addTable(section, table);
    }

    @Test(expected = ServiceException.class)
    public void testAddTableWithNegativeSeats_shouldThrow() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSeats(-3);
        table.setColumn(4);
        table.setRow(5);

        // WHEN
        sectionService.addTable(section, table);
    }

    @Test(expected = ServiceException.class)
    public void testAddTableWithNegativeColumn_shouldThrow() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);
        Table table = new Table();
        table.setNumber((long) 1);
        table.setSeats(3);
        table.setColumn(-4);
        table.setRow(5);

        // WHEN
        sectionService.addTable(section, table);
    }

    @Test(expected = ServiceException.class)
    public void testAddTableWithNegativeRow_shouldThrow() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);
        Table table = new Table();
        table.setNumber((long) -1);
        table.setSeats(3);
        table.setColumn(4);
        table.setRow(-5);

        // WHEN
        sectionService.addTable(section, table);
    }

    @Test
    public void testAddTables_shouldAddTables() throws ServiceException {
        // GIVEN
        Section section = new Section();
        section.setName("section");
        sectionService.addSection(section);
        Table table1 = new Table();
        table1.setNumber((long) 1);
        table1.setSeats(3);
        table1.setColumn(4);
        table1.setRow(5);
        Table table2 = new Table();
        table2.setNumber((long) 2);
        table2.setSeats(13);
        table2.setColumn(14);
        table2.setRow(15);
        Table table3 = new Table();
        table3.setNumber((long) 3);
        table3.setSeats(23);
        table3.setColumn(24);
        table3.setRow(25);

        // WHEN
        sectionService.addTable(section, table1);
        sectionService.addTable(section, table2);
        sectionService.addTable(section, table3);

        // THEN
        List<Table> objects = sectionService.getAllTables(section);
        Assert.assertTrue(objects.contains(table1));
        Assert.assertTrue(objects.contains(table2));
        Assert.assertTrue(objects.contains(table3));
    }

    @Test
    public void testGetAllTables_shouldReturnEmptyList() throws ServiceException {
        // GIVEN
        Section section = new Section();

        // WHEN
        List<Table> objects = sectionService.getAllTables(section);

        // THEN
        Assert.assertTrue(objects.isEmpty());
    }
}
