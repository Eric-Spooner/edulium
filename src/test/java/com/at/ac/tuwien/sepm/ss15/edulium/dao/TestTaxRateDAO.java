package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.TaxRate;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        taxRate.setValue(BigDecimal.valueOf(20.25));

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
        taxRate.setValue(BigDecimal.valueOf(19.0));

        // check if tax rate is stored
        taxRateDAO.create(taxRate);
        assertEquals(1, taxRateDAO.find(taxRate).size());

        // GIVEN
        TaxRate updatedTaxRate = new TaxRate();
        updatedTaxRate.setIdentity(taxRate.getIdentity());
        updatedTaxRate.setValue(BigDecimal.valueOf(10.0));

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
        taxRate.setValue(BigDecimal.valueOf(15.0));

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

        taxRate.setValue(BigDecimal.valueOf(25.0));

        // WHEN
        taxRateDAO.update(taxRate);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // PREPARE
        final int numberOfTaxRatesBefore = taxRateDAO.getAll().size();

        TaxRate taxRate = new TaxRate();
        taxRate.setValue(BigDecimal.valueOf(30.0));

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
        taxRate1.setValue(BigDecimal.valueOf(11.11));

        taxRateDAO.create(taxRate1);
        assertEquals(1, taxRateDAO.find(taxRate1).size());

        // tax rate 2
        TaxRate taxRate2 = new TaxRate();
        taxRate2.setValue(BigDecimal.valueOf(22.22));

        taxRateDAO.create(taxRate2);
        assertEquals(1, taxRateDAO.find(taxRate2).size());

        // tax rate 3
        TaxRate taxRate3 = new TaxRate();
        taxRate3.setValue(BigDecimal.valueOf(22.22));

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
        taxRate1.setValue(BigDecimal.valueOf(11.11));

        taxRateDAO.create(taxRate1);
        assertEquals(1, taxRateDAO.find(taxRate1).size());

        // tax rate 2
        TaxRate taxRate2 = new TaxRate();
        taxRate2.setValue(BigDecimal.valueOf(22.22));

        taxRateDAO.create(taxRate2);
        assertEquals(1, taxRateDAO.find(taxRate2).size());

        // tax rate 3
        TaxRate taxRate3 = new TaxRate();
        taxRate3.setValue(BigDecimal.valueOf(22.22));

        taxRateDAO.create(taxRate3);
        assertEquals(1, taxRateDAO.find(taxRate3).size());

        // GIVEN
        TaxRate matcher1 = new TaxRate(); // for tax rate 1
        matcher1.setValue(BigDecimal.valueOf(11.11));

        TaxRate matcher2 = new TaxRate(); // for tax rate 2 and tax rate 3
        matcher2.setValue(BigDecimal.valueOf(22.22));

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
        taxRate1.setValue(BigDecimal.valueOf(11.11));

        taxRateDAO.create(taxRate1);
        assertEquals(1, taxRateDAO.find(taxRate1).size());

        // tax rate 2
        TaxRate taxRate2 = new TaxRate();
        taxRate2.setValue(BigDecimal.valueOf(22.22));

        taxRateDAO.create(taxRate2);
        assertEquals(1, taxRateDAO.find(taxRate2).size());

        // tax rate 3
        TaxRate taxRate3 = new TaxRate();
        taxRate3.setValue(BigDecimal.valueOf(22.22));

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
}
