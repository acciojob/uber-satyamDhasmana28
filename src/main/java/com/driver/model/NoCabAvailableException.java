package com.driver.model;

public class NoCabAvailableException extends Exception{
    public NoCabAvailableException() {
        super();
    }

    public NoCabAvailableException(String message) {
        super(message);
    }

    public NoCabAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCabAvailableException(Throwable cause) {
        super(cause);
    }

    protected NoCabAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
