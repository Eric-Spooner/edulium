package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.dao.DAOException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Unit Test for the Table validator
 */
public class TestTableValidator extends AbstractDomainTest {
    @Autowired
    private Validator<Table> tableValidator;

    @Test
    public void testValidateForCreate_shouldAcceptTable() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutNumberShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutSectionShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithInvalidSectionShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(new Section());
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    /* user is optional */
    @Test
    public void testValidateForCreate_tableWithoutUserShouldAccept() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithInvalidUserShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(new User());
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutSeatsShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutColumnShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutRowShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithNegativeSeatsShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(-2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithNegativeColumnShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(-3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithNegativeRowShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(-4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Table table = null;

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptTable() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutNumberShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutSectionShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        User user = new User();
        user.setIdentity("A");
        user.setName("User");
        user.setRole("Role");
        table.setNumber((long) 1);
        table.setUser(user);
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithInvalidSectionShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(new Section());
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    /* user is optional */
    @Test
    public void testValidateForUpdate_tableWithoutUserShouldAccept() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        Section section = new Section();
        section.setIdentity((long) 1);
        section.setName("Section");
        table.setNumber((long) 1);
        table.setSection(section);
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithInvalidUserShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(new User());
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutSeatsShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        User user = new User();
        user.setIdentity("A");
        user.setName("User");
        user.setRole("Role");
        Section section = new Section();
        section.setIdentity((long) 1);
        section.setName("Section");
        table.setNumber((long) 1);
        table.setSection(section);
        table.setUser(user);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutColumnShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        User user = new User();
        user.setIdentity("A");
        user.setName("User");
        user.setRole("Role");
        Section section = new Section();
        section.setIdentity((long) 1);
        section.setName("Section");
        table.setNumber((long) 1);
        table.setSection(section);
        table.setUser(user);
        table.setSeats(2);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutRowShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        User user = new User();
        user.setIdentity("A");
        user.setName("User");
        user.setRole("Role");
        Section section = new Section();
        section.setIdentity((long) 1);
        section.setName("Section");
        table.setNumber((long) 1);
        table.setSection(section);
        table.setUser(user);
        table.setSeats(2);
        table.setColumn(3);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithNegativeSeatsShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(-2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithNegativeColumnShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(-3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithNegativeRowShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(Section.withIdentity(1L));
        table.setUser(User.withIdentity("A"));
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(-4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Table table = null;

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test
    public void testValidateForDelete_shouldAcceptTable() throws ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(0L);
        table.setSection(Section.withIdentity(1L));

        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_tableWithoutNumberShouldThrow() throws ValidationException {
        // GIVEN
        Table table = new Table();
        table.setSection(Section.withIdentity(1L));

        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_tableWithoutSectionShouldThrow() throws ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);

        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_tableWithInvalidSectionShouldThrow() throws ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(1L);
        table.setSection(new Section());

        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Table table = null;

        // WHEN
        tableValidator.validateForDelete(table);
    }

    @Test
    public void testValidateIdentity_shouldAcceptTable() throws ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(0L);
        table.setSection(Section.withIdentity(1L));

        // WHEN
        tableValidator.validateIdentity(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_tableWithoutNumberShouldThrow() throws ValidationException {
        // GIVEN
        Table table = new Table();
        table.setSection(Section.withIdentity(1L));

        // WHEN
        tableValidator.validateIdentity(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_tableWithoutSectionShouldThrow() throws ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(0L);

        // WHEN
        tableValidator.validateIdentity(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_tableWithInvalidSectionShouldThrow() throws ValidationException {
        // GIVEN
        Table table = new Table();
        table.setNumber(0L);
        table.setSection(new Section());

        // WHEN
        tableValidator.validateIdentity(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Table table = null;

        // WHEN
        tableValidator.validateIdentity(table);
    }
}
