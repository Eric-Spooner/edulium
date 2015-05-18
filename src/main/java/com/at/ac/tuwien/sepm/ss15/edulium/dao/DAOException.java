package com.at.ac.tuwien.sepm.ss15.edulium.dao;

/**
 * Exception indicating an error in the persistence layer
 */
public class DAOException extends Exception {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
