package com.driver.model;

public class NoCabAvailable extends Exception{
    public NoCabAvailable() {
        super();
    }

    public NoCabAvailable(String message) {
        super(message);
    }

    public NoCabAvailable(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCabAvailable(Throwable cause) {
        super(cause);
    }

    protected NoCabAvailable(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
