package com.at.ac.tuwien.sepm.ss15.edulium.service;

/**
 * Exception indicating an error in the service layer
 */
public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
