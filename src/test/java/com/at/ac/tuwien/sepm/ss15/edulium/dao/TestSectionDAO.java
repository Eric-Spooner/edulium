package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Section;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit Test for the SectionDAO
 */
public class TestSectionDAO extends AbstractDAOTest {
    private static final Logger LOGGER = LogManager.getLogger(TestSectionDAO.class);

    @Autowired
    private DAO<Section> sectionDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        Section section = new Section();
        section.setName("section");

        // WHEN
        sectionDAO.create(section);

        // THEN
        // check if identity is set
        Assert.assertNotNull(section.getIdentity());

        // check retrieving object
        List<Section> storedObjects = sectionDAO.find(Section.withIdentity(section.getIdentity()));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(section, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingInvalidObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Section section = new Section();

        // WHEN
        try {
            sectionDAO.create(section);
        } finally {
            Assert.assertNull(section.getIdentity());
        }
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Section section = null;

        // WHEN
        sectionDAO.create(section);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // PREPARE
        Section section = new Section();
        section.setName("section");
        sectionDAO.create(section);

        // check if section is stored
        Assert.assertEquals(1, sectionDAO.find(section).size());

        // GIVEN
        section.setName("newSection");

        // WHEN
        sectionDAO.update(section);

        // THEN
        // check if section name was updated
        List<Section> storedObjects = sectionDAO.find(Section.withIdentity(section.getIdentity()));
        Assert.assertEquals(1, storedObjects.size());
        Assert.assertEquals(section, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Section section = new Section();
        section.setName("section");

        // WHEN
        sectionDAO.update(section);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingInvalidObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Section section = new Section();

        // WHEN
        sectionDAO.update(section);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Section section = null;

        // WHEN
        sectionDAO.update(section);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Section notPersitentSection = new Section();
        Long identity = (long) 1;
        notPersitentSection.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        try {
            while (!sectionDAO.find(notPersitentSection).isEmpty()) {
                identity++;
                notPersitentSection.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.fail();
        }

        Section section = new Section();
        section.setIdentity(notPersitentSection.getIdentity());
        section.setName("section");

        // WHEN
        sectionDAO.update(section);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // PREPARE
        Section section = new Section();
        section.setName("section");
        sectionDAO.create(section);
        
        // check if section created
        Assert.assertEquals(1, sectionDAO.find(section).size());

        // WHEN
        sectionDAO.delete(section);

        // THEN
        // check if section was removed
        Assert.assertEquals(0, sectionDAO.find(section).size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Section section = new Section();

        // WHEN
        sectionDAO.delete(section);
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Section section = null;

        // WHEN
        sectionDAO.delete(section);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Section section = new Section();
        Long identity = (long) 1;
        section.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        try {
            while (!sectionDAO.find(section).isEmpty()) {
                identity++;
                section.setIdentity(identity);
            }
        } catch (DAOException e) {
            // exception should not occur here
            Assert.fail();
        }

        // WHEN
        sectionDAO.delete(section);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // GIVEN
        Section matcher = new Section();
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section1");
        section2.setName("section2");
        section3.setName("section3");
        sectionDAO.create(section1);
        sectionDAO.create(section2);
        sectionDAO.create(section3);

        // WHEN
        matcher.setIdentity(section1.getIdentity());
        List<Section> objects = sectionDAO.find(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(section1, objects.get(0));

        // WHEN
        matcher.setIdentity(section2.getIdentity());
        objects = sectionDAO.find(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(section2, objects.get(0));

        // WHEN
        matcher.setIdentity(section3.getIdentity());
        objects = sectionDAO.find(matcher);
        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(section3, objects.get(0));
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        Section matcher = new Section();
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section");
        section2.setName("section");
        section3.setName("section2");
        sectionDAO.create(section1);
        sectionDAO.create(section2);
        sectionDAO.create(section3);

        // WHEN
        matcher.setName(section1.getName());
        List<Section> objects = sectionDAO.find(matcher);

        // THEN
        Assert.assertEquals(2, objects.size());
        Assert.assertTrue(objects.contains(section1));
        Assert.assertTrue(objects.contains(section2));

        // WHEN
        matcher.setName(section3.getName());
        objects = sectionDAO.find(matcher);

        // THEN
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(objects.get(0), section3);
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        Long identity = (long) 1;
        Section matcher = new Section();
        matcher.setIdentity(identity);

        // check if no item with the id IDENTITY exists
        while (!sectionDAO.find(matcher).isEmpty()) {
            identity++;
            matcher.setIdentity(identity);
        }

        // WHEN
        List<Section> storedObjects = sectionDAO.find(matcher);

        // THEN
        Assert.assertTrue(storedObjects.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // GIVEN
        Section section1 = new Section();
        Section section2 = new Section();
        Section section3 = new Section();
        section1.setName("section1");
        section2.setName("section2");
        section3.setName("section3");
        sectionDAO.create(section1);
        sectionDAO.create(section2);
        sectionDAO.create(section3);

        // WHEN
        List<Section> objects = sectionDAO.getAll();

        // THEN
        Assert.assertTrue(objects.contains(section1));
        Assert.assertTrue(objects.contains(section2));
        Assert.assertTrue(objects.contains(section3));
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        sectionDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Section section = new Section();

        // WHEN
        sectionDAO.getHistory(section);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        Long identity = (long) 1;
        Section section = new Section();
        section.setIdentity(identity);

        // generate identity which is not used by any persistent object
        while (!sectionDAO.find(section).isEmpty()) {
            identity++;
            section.setIdentity(identity);
        }

        // WHEN / THEN
        Assert.assertTrue(sectionDAO.getHistory(section).isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User user = getCurrentUser();

        // GIVEN
        // create data
        Section section1 = new Section();
        section1.setName("section");
        section1.setIdentity((long)1);
        LocalDateTime createTime = LocalDateTime.now();
        sectionDAO.create(section1);

        // update data
        Section section2 = Section.withIdentity(section1.getIdentity());
        section2.setName("update");
        LocalDateTime updateTime = LocalDateTime.now();
        sectionDAO.update(section2);

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        sectionDAO.delete(section2);

        // WHEN
        List<History<Section>> history = sectionDAO.getHistory(section1);

        // THEN
        Assert.assertEquals(3, history.size());

        // check create history
        History<Section> entry = history.get(0);
        Assert.assertEquals(Long.valueOf(1), entry.getChangeNumber());
        Assert.assertEquals(section1, entry.getData());
        Assert.assertEquals(user, entry.getUser());
        Assert.assertTrue(Duration.between(createTime, entry.getTimeOfChange()).getSeconds() < 1);
        Assert.assertFalse(entry.isDeleted());

        // check update history
        entry = history.get(1);
        Assert.assertEquals(Long.valueOf(2), entry.getChangeNumber());
        Assert.assertEquals(section2, entry.getData());
        Assert.assertEquals(user, entry.getUser());
        Assert.assertTrue(Duration.between(updateTime, entry.getTimeOfChange()).getSeconds() < 1);
        Assert.assertFalse(entry.isDeleted());

        // check delete history
        entry = history.get(2);
        Assert.assertEquals(Long.valueOf(3), entry.getChangeNumber());
        Assert.assertEquals(section2, entry.getData());
        Assert.assertEquals(user, entry.getUser());
        Assert.assertTrue(Duration.between(deleteTime, entry.getTimeOfChange()).getSeconds() < 1);
        Assert.assertTrue(entry.isDeleted());
    }
}
