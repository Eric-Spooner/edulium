package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Unit Test for the TaxRate validator
 */
public class TestOrderValidator extends AbstractDomainTest {
    @Autowired
    private Validator<Order> orderValidator;

    private MenuEntry createMenuEntry(String name, String desc, String cat, double price, double tax, boolean available)
            throws ValidationException {
        MenuCategory menuCategory = MenuCategory.withIdentity(3);
        menuCategory.setName(cat);

        TaxRate taxRate = TaxRate.withIdentity(4);
        taxRate.setValue(BigDecimal.valueOf(tax));

        MenuEntry entry = MenuEntry.withIdentity(3);
        entry.setCategory(menuCategory);
        entry.setTaxRate(taxRate);
        entry.setName(name);
        entry.setDescription(desc);
        entry.setPrice(BigDecimal.valueOf(price));
        entry.setAvailable(available);
        return entry;
    }

    private Table createTable(int row, int col, int seats) throws ValidationException {
        Section section = Section.withIdentity(4);
        section.setName("Sec");

        Table table = Table.withIdentity(section, 3);
        table.setColumn(col);
        table.setRow(row);
        table.setSeats(seats);
        return table;
    }

    @Test
    public void testValidateForCreate_shouldAcceptOrder() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setBrutto(BigDecimal.valueOf(1));
        order.setTax(BigDecimal.valueOf(1));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("");
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutValueShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Order order = null;

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutMenuEntryShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(createTable(1, 2, 3));
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutTableShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
         public void testValidateForCreate_OrderWithoutBruttoShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutTaxShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutStateShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setBrutto(BigDecimal.valueOf(1));
        order.setTax(BigDecimal.valueOf(1));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("");

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithoutTimeShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setAdditionalInformation("Info");
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithInvalidTableThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(new Table());
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithInvalidMenuEntryThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithNegativBruttoThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setMenuEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(-1));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithNullTaxThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setMenuEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(null);
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_OrderWithInvalidTaxThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setMenuEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(-1));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.QUEUED);

        // WHEN
        orderValidator.validateForCreate(order);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptOrder() throws ValidationException {
        // GIVEN
        Order order =  Order.withIdentity(3);
        order.setIdentity((long) 1);
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setBrutto(BigDecimal.valueOf(1));
        order.setTax(BigDecimal.valueOf(1));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.IN_PROGRESS);

        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_OrderWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Order order =  Order.withIdentity(3);
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.IN_PROGRESS);

        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_OrderWithoutStateShouldThrow() throws ValidationException {
        // GIVEN
        Order order =  Order.withIdentity(3);
        order.setIdentity((long) 1);
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setBrutto(BigDecimal.valueOf(1));
        order.setTax(BigDecimal.valueOf(1));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");

        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Order order = null;

        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_OrderWithInvalidTableThrow() throws ValidationException {
        // GIVEN
        Order order =  Order.withIdentity(3);
        order.setTable(new Table());
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.IN_PROGRESS);

        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_OrderWithInvalidMenuEntryThrow() throws ValidationException {
        // GIVEN
        Order order =  Order.withIdentity(3);
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(0));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.IN_PROGRESS);

        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_OrderWithNullTaxThrow() throws ValidationException {
        // GIVEN
        Order order = Order.withIdentity(3);
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setMenuEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(null);
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.IN_PROGRESS);

        // WHEN
        orderValidator.validateForUpdate(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_OrderWithInvalidTaxThrow() throws ValidationException {
        // GIVEN
        Order order = Order.withIdentity(3);
        order.setTable(createTable(1, 2, 3));
        order.setMenuEntry(createMenuEntry("name", "desc", "cat", 50, 0.02, true));
        order.setMenuEntry(new MenuEntry());
        order.setBrutto(BigDecimal.valueOf(0));
        order.setTax(BigDecimal.valueOf(-1));
        order.setTime(LocalDateTime.now());
        order.setAdditionalInformation("Info");
        order.setState(Order.State.IN_PROGRESS);

        // WHEN
        orderValidator.validateForUpdate(order);
    }


    @Test
    public void testValidateForDelete_shouldAcceptOrder() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity(1L);
        order.setTable(Table.withIdentity(Section.withIdentity(1L), 1L));

        // WHEN
        orderValidator.validateForDelete(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_OrderWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();

        // WHEN
        orderValidator.validateForDelete(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Order order = null;

        // WHEN
        orderValidator.validateForDelete(order);
    }

    @Test
    public void testValidateIdentity_shouldAcceptOrder() throws ValidationException {
        // GIVEN
        Order order = new Order();
        order.setIdentity(0L);

        // WHEN
        orderValidator.validateIdentity(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_OrderWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Order order = new Order();

        // WHEN
        orderValidator.validateIdentity(order);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Order order =  null;

        // WHEN
        orderValidator.validateIdentity(order);
    }
}
