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
//TODO: add tests
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
}
