package com.at.ac.tuwien.sepm.ss15.edulium.dao;

/**
 * Exception indication an error in the persistence layer
 */
public class DAOException extends Exception {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
