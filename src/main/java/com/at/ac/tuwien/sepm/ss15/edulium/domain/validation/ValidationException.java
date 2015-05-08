package com.at.ac.tuwien.sepm.ss15.edulium.domain.validation;

/**
 * Exception indicating a validation error
 */
public class ValidationException extends Exception {
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }
}
