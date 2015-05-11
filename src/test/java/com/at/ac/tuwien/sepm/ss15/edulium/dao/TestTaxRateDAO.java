package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.User;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.history.History;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

/**
 * Unit Test for the TaxRate DAO
 */
public class TestTaxRateDAO extends AbstractDAOTest {
    @Autowired
    private DAO<TaxRate> taxRateDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(0.2025));

        // WHEN
        taxRateDAO.create(taxRate);

        // THEN
        // try to find the tax rate and compare it
        List<TaxRate> storedObjects = taxRateDAO.find(TaxRate.withIdentity(taxRate.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(taxRate, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutValueShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateDAO.create(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = null;

        // WHEN
        taxRateDAO.create(taxRate);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // PREPARE
        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(0.19));

        // check if tax rate is stored
        taxRateDAO.create(taxRate);
        assertEquals(1, taxRateDAO.find(taxRate).size());

        // GIVEN
        TaxRate updatedTaxRate = new TaxRate();
        updatedTaxRate.setIdentity(taxRate.getIdentity());
        updatedTaxRate.setValue(BigDecimal.valueOf(0.1));

        // WHEN
        taxRateDAO.update(updatedTaxRate);

        // THEN
        // check if the tax rate has been updated;
        List<TaxRate> storedObjects = taxRateDAO.find(updatedTaxRate);
        assertEquals(1, storedObjects.size());
        assertEquals(updatedTaxRate, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(0.15));

        // WHEN
        taxRateDAO.update(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingEmptyObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateDAO.update(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = null;

        // WHEN
        taxRateDAO.update(taxRate);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();

        // search for a non-existing tax rate identity
        try {
            taxRate.setIdentity(0L);
            while (!taxRateDAO.find(taxRate).isEmpty()) {
                taxRate.setIdentity(taxRate.getIdentity() + 1L);
            }
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing tax rate identity");
        }

        taxRate.setValue(BigDecimal.valueOf(0.25));

        // WHEN
        taxRateDAO.update(taxRate);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // PREPARE
        final int numberOfTaxRatesBefore = taxRateDAO.getAll().size();

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(0.3));

        // check if taxRate is stored
        taxRateDAO.create(taxRate);
        assertEquals(1, taxRateDAO.find(taxRate).size());
        assertEquals(numberOfTaxRatesBefore + 1, taxRateDAO.getAll().size());

        // GIVEN
        TaxRate taxRateForDelete = new TaxRate();
        taxRateForDelete.setIdentity(taxRate.getIdentity());

        // WHEN
        taxRateDAO.delete(taxRateForDelete);

        // THEN
        // check if taxRate was removed
        assertEquals(0, taxRateDAO.find(taxRate).size());
        assertEquals(numberOfTaxRatesBefore, taxRateDAO.getAll().size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingObjectWithIdentityNullShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateDAO.delete(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = null;

        // WHEN
        taxRateDAO.delete(taxRate);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();

        // search for a non-existing tax rate identity
        try {
            taxRate.setIdentity(0L);
            while (!taxRateDAO.find(taxRate).isEmpty()) {
                taxRate.setIdentity(taxRate.getIdentity() + 1L);
            }
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing tax rate identity");
        }

        // WHEN
        taxRateDAO.delete(taxRate);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // PREPARE
        // tax rate 1
        TaxRate taxRate1 = new TaxRate();
        taxRate1.setValue(BigDecimal.valueOf(0.1111));

        taxRateDAO.create(taxRate1);
        assertEquals(1, taxRateDAO.find(taxRate1).size());

        // tax rate 2
        TaxRate taxRate2 = new TaxRate();
        taxRate2.setValue(BigDecimal.valueOf(0.2222));

        taxRateDAO.create(taxRate2);
        assertEquals(1, taxRateDAO.find(taxRate2).size());

        // tax rate 3
        TaxRate taxRate3 = new TaxRate();
        taxRate3.setValue(BigDecimal.valueOf(0.2222));

        taxRateDAO.create(taxRate3);
        assertEquals(1, taxRateDAO.find(taxRate3).size());

        // GIVEN
        TaxRate matcher1 = TaxRate.withIdentity(taxRate1.getIdentity()); // for tax rate 1
        TaxRate matcher2 = TaxRate.withIdentity(taxRate2.getIdentity()); // for tax rate 2
        TaxRate matcher3 = TaxRate.withIdentity(taxRate3.getIdentity()); // for tax rate 3

        // WHEN
        List<TaxRate> result1 = taxRateDAO.find(matcher1);
        List<TaxRate> result2 = taxRateDAO.find(matcher2);
        List<TaxRate> result3 = taxRateDAO.find(matcher3);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(taxRate1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(taxRate2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(taxRate3));
    }

    @Test
    public void testFind_byValueShouldReturnObject() throws DAOException, ValidationException {
        // PREPARE
        // tax rate 1
        TaxRate taxRate1 = new TaxRate();
        taxRate1.setValue(BigDecimal.valueOf(0.1111));

        taxRateDAO.create(taxRate1);
        assertEquals(1, taxRateDAO.find(taxRate1).size());

        // tax rate 2
        TaxRate taxRate2 = new TaxRate();
        taxRate2.setValue(BigDecimal.valueOf(0.2222));

        taxRateDAO.create(taxRate2);
        assertEquals(1, taxRateDAO.find(taxRate2).size());

        // tax rate 3
        TaxRate taxRate3 = new TaxRate();
        taxRate3.setValue(BigDecimal.valueOf(0.2222));

        taxRateDAO.create(taxRate3);
        assertEquals(1, taxRateDAO.find(taxRate3).size());

        // GIVEN
        TaxRate matcher1 = new TaxRate(); // for tax rate 1
        matcher1.setValue(BigDecimal.valueOf(0.1111));

        TaxRate matcher2 = new TaxRate(); // for tax rate 2 and tax rate 3
        matcher2.setValue(BigDecimal.valueOf(0.2222));

        // WHEN
        List<TaxRate> result1 = taxRateDAO.find(matcher1);
        List<TaxRate> result2 = taxRateDAO.find(matcher2);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(taxRate1));

        assertEquals(2, result2.size());
        assertTrue(result2.contains(taxRate2));
        assertTrue(result2.contains(taxRate3));
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        // GIVEN
        TaxRate matcher = new TaxRate();

        // search for a non-existing tax rate identity
        try {
            matcher.setIdentity(0L);
            while (!taxRateDAO.find(matcher).isEmpty()) {
                matcher.setIdentity(matcher.getIdentity() + 1L);
            }
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing tax rate identity");
        }

        // WHEN
        List<TaxRate> result = taxRateDAO.find(matcher);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        final int numberOfTaxRatesBefore = taxRateDAO.getAll().size();

        // tax rate 1
        TaxRate taxRate1 = new TaxRate();
        taxRate1.setValue(BigDecimal.valueOf(0.1111));

        taxRateDAO.create(taxRate1);
        assertEquals(1, taxRateDAO.find(taxRate1).size());

        // tax rate 2
        TaxRate taxRate2 = new TaxRate();
        taxRate2.setValue(BigDecimal.valueOf(0.2222));

        taxRateDAO.create(taxRate2);
        assertEquals(1, taxRateDAO.find(taxRate2).size());

        // tax rate 3
        TaxRate taxRate3 = new TaxRate();
        taxRate3.setValue(BigDecimal.valueOf(0.2222));

        taxRateDAO.create(taxRate3);
        assertEquals(1, taxRateDAO.find(taxRate3).size());

        // WHEN
        List<TaxRate> result = taxRateDAO.getAll();

        // THEN
        assertEquals(numberOfTaxRatesBefore + 3, result.size());
        assertTrue(result.contains(taxRate1));
        assertTrue(result.contains(taxRate2));
        assertTrue(result.contains(taxRate3));
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = null;

        // WHEN
        taxRateDAO.getHistory(taxRate);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();

        // WHEN
        taxRateDAO.getHistory(taxRate);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        TaxRate taxRate = new TaxRate();

        // search for a non-existing tax rate identity
        try {
            taxRate.setIdentity(0L);
            while (!taxRateDAO.find(taxRate).isEmpty()) {
                taxRate.setIdentity(taxRate.getIdentity() + 1L);
            }
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing tax rate identity");
        }

        // WHEN
        List<TaxRate> result = taxRateDAO.find(taxRate);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        // PREPARE
        // get test user
        User testUser = getCurrentUser();

        // GIVEN
        // create data
        TaxRate taxRate1 = new TaxRate();
        taxRate1.setValue(BigDecimal.valueOf(0.5));
        LocalDateTime createTime = LocalDateTime.now();
        taxRateDAO.create(taxRate1);

        // update data
        TaxRate taxRate2 = new TaxRate();
        taxRate2.setValue(BigDecimal.valueOf(0.4));
        LocalDateTime updateTime = LocalDateTime.now();
        taxRateDAO.update(taxRate2);

        // delete data
        LocalDateTime deleteTime = LocalDateTime.now();
        taxRateDAO.delete(taxRate2);

        // WHEN
        List<History<TaxRate>> history = taxRateDAO.getHistory(taxRate1);

        // THEN
        assertEquals(3, history.size());

        // check create history
        History<TaxRate> entry = history.get(0);
        assertEquals(Long.valueOf(1), entry.getChangeNumber());
        assertEquals(taxRate1, entry.getData());
        assertEquals(testUser, entry.getUser());
        assertTrue(Duration.between(createTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check update history
        entry = history.get(1);
        assertEquals(Long.valueOf(2), entry.getChangeNumber());
        assertEquals(taxRate2, entry.getData());
        assertEquals(testUser, entry.getUser());
        assertTrue(Duration.between(updateTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertFalse(entry.isDeleted());

        // check delete history
        entry = history.get(2);
        assertEquals(Long.valueOf(3), entry.getChangeNumber());
        assertEquals(taxRate2, entry.getData());
        assertEquals(testUser, entry.getUser());
        assertTrue(Duration.between(deleteTime, entry.getTimeOfChange()).getSeconds() < 1);
        assertTrue(entry.isDeleted());
    }
}
