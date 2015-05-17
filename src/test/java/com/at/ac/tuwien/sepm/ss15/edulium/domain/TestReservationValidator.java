package com.at.ac.tuwien.sepm.ss15.edulium.domain;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Unit Test for the Reservation validator
 */
public class TestReservationValidator extends AbstractDomainTest {
    @Autowired
    private Validator<Reservation> reservationValidator;

    @Test
    public void testValidateForCreate_shouldAcceptReservation() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(1);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithoutValueShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = null;

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithoutTimeShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithoutQuantityShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithQuantityZeroShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(0);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithNegativeQuantityShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(-1);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithoutDurationShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithZeroDurationShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ZERO);
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithNegativeDurationShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(-90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithoutTablesShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithEmptyListOfTablesShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList());

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForCreate_reservationWithInvalidTableShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                null));

        // WHEN
        reservationValidator.validateForCreate(reservation);
    }

    @Test
    public void testValidateForUpdate_shouldAcceptReservation() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(1);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithoutValueShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = null;

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithoutTimeShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithoutNameShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithEmptyNameShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithoutQuantityShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithQuantityZeroShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(0);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithNegativeQuantityShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(-1);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithoutDurationShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithZeroDurationShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ZERO);
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithNegativeDurationShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(-90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                Table.withIdentity(Section.withIdentity(2L), 2L)));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithoutTablesShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithEmptyListOfTablesShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList());

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForUpdate_reservationWithInvalidTableShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(1L);
        reservation.setTime(LocalDateTime.now());
        reservation.setName("Hardy Heron");
        reservation.setQuantity(10);
        reservation.setDuration(Duration.ofMinutes(90));
        reservation.setTables(Arrays.asList(Table.withIdentity(Section.withIdentity(1L), 1L),
                null));

        // WHEN
        reservationValidator.validateForUpdate(reservation);
    }

    @Test
    public void testValidateForDelete_shouldAcceptReservation() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(0L);

        // WHEN
        reservationValidator.validateForDelete(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_reservationWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();

        // WHEN
        reservationValidator.validateForDelete(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateForDelete_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = null;

        // WHEN
        reservationValidator.validateForDelete(reservation);
    }

    @Test
    public void testValidateIdentity_shouldAcceptReservation() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();
        reservation.setIdentity(0L);

        // WHEN
        reservationValidator.validateIdentity(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_reservationWithoutIdentityShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = new Reservation();

        // WHEN
        reservationValidator.validateIdentity(reservation);
    }

    @Test(expected = ValidationException.class)
    public void testValidateIdentity_nullObjectShouldThrow() throws ValidationException {
        // GIVEN
        Reservation reservation = null;

        // WHEN
        reservationValidator.validateIdentity(reservation);
    }
}
