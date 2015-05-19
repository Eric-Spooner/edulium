package com.at.ac.tuwien.sepm.ss15.edulium.dao;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Sale;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.junit.Assert.*;

/**
 * Unit Test for the SaleDAO
 */
public class TestSaleDAO extends AbstractDAOTest {
    @Autowired
    private DAO<Sale> saleDAO;

    @Test
    public void testCreate_shouldAddObject() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(3));
        sale.setName("New Sale");

        // WHEN
        saleDAO.create(sale);

        // THEN
        // try to find the sale and compare it
        List<Sale> storedObjects = saleDAO.find(Sale.withIdentity(sale.getIdentity()));
        assertEquals(1, storedObjects.size());
        assertEquals(sale, storedObjects.get(0));
    }


    @Test(expected = DAOException.class)
    public void testCreate_addingTwoObjectsWithSameIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setIdentity(new Long(3));
        sale.setName("New Sale");

        // WHEN
        saleDAO.create(sale);

        // GIVEN
        Sale sale2 = new Sale();
        sale2.setIdentity(new Long(3));
        sale2.setName("Another Sale");

        // WHEN
        saleDAO.create(sale2);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setName("New Sale");

        // WHEN
        saleDAO.create(sale);
    }

    @Test(expected = ValidationException.class)
    public void testCreate_addingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = null;

        // WHEN
        saleDAO.create(sale);
    }

    @Test
    public void testUpdate_shouldUpdateObject() throws DAOException, ValidationException {
        // PREPARE
        Sale sale = new Sale();
        sale.setIdentity(new Long(3));
        sale.setName("New Sale");

        // check if sale is stored
        saleDAO.create(sale);
        assertEquals(1, saleDAO.find(sale).size());

        // GIVEN
        Sale sale2 = new Sale();
        sale.setIdentity(sale.getIdentity());
        sale2.setName("Sale Modified Name");

        // WHEN
        saleDAO.update(sale2);

        // THEN
        // check if the sale has been updated;
        List<Sale> storedObjects = saleDAO.find(sale2);
        assertEquals(1, storedObjects.size());
        assertEquals(sale2, storedObjects.get(0));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingObjectWithoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = new Sale();
        sale.setName("New Sale");

        // WHEN
        saleDAO.update(sale);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_updatingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = null;

        // WHEN
        saleDAO.update(sale);
    }

    @Test(expected = DAOException.class)
    public void testUpdate_updatingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = new Sale();

        // search for a non-existing sale identity
        try {
            do {
                sale.setIdentity(new Long((int) (Math.random() * 999999999)));
            } while (!saleDAO.find(sale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing sale identity");
        }

        sale.setName("Updated Sale");

        // WHEN
        saleDAO.update(sale);
    }

    @Test
    public void testDelete_shouldDeleteObject() throws DAOException, ValidationException {
        // PREPARE
        final int numberBefore = saleDAO.getAll().size();
        Sale sale = new Sale();
        sale.setIdentity(new Long(3));
        sale.setName("New Sale");

        // check if sale is stored
        saleDAO.create(sale);
        assertEquals(1, saleDAO.find(sale).size());

        // GIVEN
        Sale sale2 = new Sale();
        sale.setIdentity(sale.getIdentity());

        // WHEN
        saleDAO.delete(sale2);

        // THEN
        // check if the sale has been updated;
        List<Sale> storedObjects = saleDAO.find(sale2);
        assertEquals(numberBefore, storedObjects.size());
        assertEquals(0, saleDAO.find(sale).size());
    }

    @Test(expected = ValidationException.class)
    public void testDelete_deletingNullObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = null;

        // WHEN
        saleDAO.delete(sale);
    }

    @Test(expected = DAOException.class)
    public void testDelete_deletingNotPersistentObjectShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = new Sale();

        // search for a non-existing sale identity
        try {
            do {
                sale.setIdentity(new Long((int) (Math.random() * 999999999)));
            } while (!saleDAO.find(sale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing sale identity");
        }

        // WHEN
        saleDAO.delete(sale);
    }

    @Test
    public void testFind_byIdentityShouldReturnObject() throws DAOException, ValidationException {
        // Prepare

        // sale 1
        Sale sale1 = new Sale();
        sale1.setIdentity(new Long(3));
        sale1.setName("New Sale");

        saleDAO.create(sale1);
        assertEquals(1, saleDAO.find(sale1).size());

        // sale 2
        Sale sale2 = new Sale();
        sale2.setIdentity(new Long(4));
        sale2.setName("New Sale");

        saleDAO.create(sale2);
        assertEquals(1, saleDAO.find(sale2).size());

        // sale 3
        Sale sale3 = new Sale();
        sale3.setIdentity(new Long(5));
        sale3.setName("New Sale");

        saleDAO.create(sale3);
        assertEquals(1, saleDAO.find(sale3).size());

        // GIVEN
        Sale matcher1 = Sale.withIdentity(sale1.getIdentity()); // for sale 1
        Sale matcher2 = Sale.withIdentity(sale2.getIdentity()); // for sale 2
        Sale matcher3 = Sale.withIdentity(sale3.getIdentity()); // for sale 3

        // WHEN
        List<Sale> result1 = saleDAO.find(matcher1);
        List<Sale> result2 = saleDAO.find(matcher2);
        List<Sale> result3 = saleDAO.find(matcher3);

        // THEN
        assertEquals(1, result1.size());
        assertTrue(result1.contains(sale1));

        assertEquals(1, result2.size());
        assertTrue(result2.contains(sale2));

        assertEquals(1, result3.size());
        assertTrue(result3.contains(sale3));
    }

    @Test
    public void testFind_byNameShouldReturnObjects() throws DAOException, ValidationException {
        //TODO: complete
    }

    @Test
    public void testFind_shouldReturnEmptyList() throws DAOException {
        //find a non existent id
        //TODO: complete
    }

    @Test
    public void testFind_nullObjectShouldReturnEmptyList() throws DAOException {
        //TODO: complete
    }

    @Test
    public void testGetAll_shouldReturnObjects() throws DAOException, ValidationException {
        //TODO: complete
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutObjectShouldFail() throws DAOException, ValidationException {
        saleDAO.getHistory(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetHistory_withoutIdentityShouldFail() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = new Sale();

        // WHEN
        saleDAO.getHistory(sale);
    }

    @Test
    public void testGetHistory_notPersistentDataShouldReturnEmptyList() throws DAOException, ValidationException {
        // GIVEN
        Sale sale = new Sale();

        // search for a non-existing sale identity
        try {
            do {
                sale.setIdentity(new Long((int) (Math.random() * 999999999)));
            } while (!saleDAO.find(sale).isEmpty());
        } catch (DAOException e) {
            fail("DAOException should not occur while searching for a non-existing sale identity");
        }

        assertTrue(saleDAO.getHistory(sale).isEmpty());
    }

    @Test
    public void testGetHistory_shouldReturnObjects() throws DAOException, ValidationException {
        //TODO: complete
    }
}