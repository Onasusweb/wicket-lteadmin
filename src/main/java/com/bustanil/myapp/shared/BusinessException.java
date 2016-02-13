package com.bustanil.myapp.shared;

/**
 * Extend this class to indicate business validation exception
 */
public abstract class BusinessException extends RuntimeException {

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message) {
        super(message);
    }
}
