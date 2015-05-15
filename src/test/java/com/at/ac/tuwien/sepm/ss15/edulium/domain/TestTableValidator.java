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
    private User user;
    private Section section;

    @Test
    public void testValidateForCreate_shouldAcceptTable() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        user = new User();
        user.setIdentity("A");
        user.setName("User");
        user.setRole("Role");
        section = new Section();
        section.setIdentity((long) 1);
        section.setName("Section");
        table.setNumber((long) 1);
        table.setSection(section);
        table.setUser(user);
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
        User user = new User();
        user.setIdentity("A");
        user.setName("User");
        user.setRole("Role");
        Section section = new Section();
        section.setIdentity((long) 1);
        section.setName("Section");
        table.setSection(section);
        table.setUser(user);
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
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutUserShouldThrow() throws ValidationException, DAOException {
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
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutSeatsShouldThrow() throws ValidationException, DAOException {
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
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutColumnShouldThrow() throws ValidationException, DAOException {
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
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithoutRowShouldThrow() throws ValidationException, DAOException {
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
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithNegativeSeatsShouldThrow() throws ValidationException, DAOException {
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
        table.setColumn(-3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForCreate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_tableWithNegativeRowShouldThrow() throws ValidationException, DAOException {
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
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithoutNumberShouldThrow() throws ValidationException, DAOException {
        // GIVEN
        Table table = new Table();
        User user = new User();
        user.setIdentity("A");
        user.setName("User");
        user.setRole("Role");
        Section section = new Section();
        section.setIdentity((long) 1);
        section.setName("Section");
        table.setSection(section);
        table.setUser(user);
        table.setSeats(2);
        table.setColumn(3);
        table.setRow(4);

        // WHEN
        tableValidator.validateForUpdate(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_tableWithNegativeSeatsShouldThrow() throws ValidationException, DAOException {
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
        User user = new User();
        user.setIdentity("A");
        user.setName("User");
        user.setRole("Role");
        //userDAO.create(user);
        Section section = new Section();
        section.setIdentity((long) 1);
        section.setName("Section");
        //sectionDAO.create(section);
        table.setNumber((long) 1);
        table.setSection(section);
        table.setUser(user);
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

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_tableWithoutNumberShouldThrow() throws ValidationException {
        // GIVEN
        Table table = new Table();

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

        // WHEN
        tableValidator.validateIdentity(table);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_tableWithoutNumberShouldThrow() throws ValidationException {
        // GIVEN
        Table table = new Table();

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
