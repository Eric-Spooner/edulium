package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit Test for the Table validator
 */
public class TestTableValidator extends AbstractDomainTest {
    @Autowired
    private Validator<Table> tableValidator;
    private Table table;

    @Before
    public void before() {
        table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);
    }

    @Test
    public void testValidateForCreate_shouldAcceptTable() throws ValidationException, DAOException {
        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutNumberShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setNumber(null);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithInvalidNumberShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setNumber((long)-1);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutSectionShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setSection(null);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithInvalidSectionShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setSection(new Section());

        // WHEN
        tableValidator.validateForCreate(table);
    }

    /* user is optional */
    @Test
    public void testValidateForCreate_tableWithoutUserShouldAccept() throws ValidationException, DAOException {
        // GIVEN
        table.setUser(null);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithInvalidUserShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setUser(new User());

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutSeatsShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setSeats(null);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutColumnShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setColumn(null);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutRowShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setRow(null);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithNegativeSeatsShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setSeats(-2);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithNegativeColumnShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setColumn(-3);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithNegativeRowShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setRow(-4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        table = null;

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptTable() throws ValidationException, DAOException {
        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutNumberShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setNumber(null);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithInvalidNumberShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setNumber((long)-1);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutSectionShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setSection(null);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithInvalidSectionShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setSection(new Section());

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    /* user is optional */
    @Test
    public void testValidateForUpdate_tableWithoutUserShouldAccept() throws ValidationException, DAOException {
        // GIVEN
        table.setUser(null);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithInvalidUserShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setUser(new User());

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutSeatsShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setSeats(null);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithNegativeSeatsShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setSeats(-2);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithNegativeColumnShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setColumn(-3);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithNegativeRowShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setRow(-4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutColumnShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setColumn(null);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutRowShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        table.setRow(null);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        table = null;

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test
    public void testValidateForDelete_shouldAcceptTable() throws ValidationException {
        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_tableWithoutNumberShouldThrow() throws ValidationException {
        // GIVEN
        table.setNumber(null);

        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_tableWithInvalidNumberShouldThrow() throws ValidationException {
        // GIVEN
        table.setNumber((long)-1);

        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_tableWithoutSectionShouldThrow() throws ValidationException {
        // GIVEN
        table.setSection(null);

        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_tableWithInvalidSectionShouldThrow() throws ValidationException {
        // GIVEN
        table.setSection(new Section());

        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        table = null;

        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test
    public void testValidateIdentity_shouldAcceptTable() throws ValidationException {
        // WHEN
        tableValidator.validateIdentity(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_tableWithoutNumberShouldThrow() throws ValidationException {
        // GIVEN
        table.setNumber(null);

        // WHEN
        tableValidator.validateIdentity(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_tableWithInvalidNumberShouldThrow() throws ValidationException {
        // GIVEN
        table.setNumber((long)-1);

        // WHEN
        tableValidator.validateIdentity(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_tableWithoutSectionShouldThrow() throws ValidationException {
        // GIVEN
        table.setSection(null);

        // WHEN
        tableValidator.validateIdentity(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_tableWithInvalidSectionShouldThrow() throws ValidationException {
        // GIVEN
        table.setSection(new Section());

        // WHEN
        tableValidator.validateIdentity(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        table = null;

        // WHEN
        tableValidator.validateIdentity(table);
    }
}
