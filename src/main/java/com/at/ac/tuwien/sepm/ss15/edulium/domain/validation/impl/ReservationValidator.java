package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.impl;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.Reservation;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.Table;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * validator implementation for the Reservation domain object
 */
class ReservationValidator implements Validator<Reservation> {
    @Autowired
    private Validator<Table> tableValidator;

    @Override
    public void validateForCreate(Reservation reservation) throws ValidationException {
        validateNotNull(reservation);
        checkForRequiredDataAttributesForCreateAndUpdate(reservation);
    }

    @Override
    public void validateForUpdate(Reservation reservation) throws ValidationException {
        validateNotNull(reservation);
        validateIdentity(reservation);
        checkForRequiredDataAttributesForCreateAndUpdate(reservation);
    }

    @Override
    public void validateForDelete(Reservation reservation) throws ValidationException {
        validateNotNull(reservation);
        validateIdentity(reservation);
    }

    @Override
    public void validateIdentity(Reservation reservation) throws ValidationException {
        validateNotNull(reservation);

        if (reservation.getIdentity() == null) {
            throw new ValidationException("identity must not be null");
        }
    }

    private void validateNotNull(Reservation reservation) throws ValidationException {
        if (reservation == null) {
            throw new ValidationException("reservation must not be null");
        }
    }

    private void checkForRequiredDataAttributesForCreateAndUpdate(Reservation reservation) throws ValidationException {
        if (reservation.getTime() == null) {
            throw new ValidationException("time must not be null");
        }

        if (reservation.getName() == null) {
            throw new ValidationException("name must not be null");
        }
        if (reservation.getName().isEmpty()) {
            throw new ValidationException("name must not be empty");
        }

        if (reservation.getQuantity() == null) {
            throw new ValidationException("quantity must not be null");
        }
        if (reservation.getQuantity() < 1) {
            throw new ValidationException("quantity must be equal or greater than 1");
        }

        if (reservation.getDuration() == null) {
            throw new ValidationException("duration must not be null");
        }
        if (reservation.getDuration().isZero()) {
            throw new ValidationException("duration must no be zero");
        }
        if (reservation.getDuration().isNegative()) {
            throw new ValidationException("duration must no be negative");
        }

        if (reservation.getTables() == null) {
            throw new ValidationException("tables must not be null");
        }
        if (reservation.getTables().isEmpty()) {
            throw new ValidationException("tables must not be empty");
        }
        for (Table table : reservation.getTables()) {
            tableValidator.validateIdentity(table);
        }
    }
}
