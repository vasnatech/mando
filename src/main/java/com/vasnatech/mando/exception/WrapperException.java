package com.vasnatech.mando.exception;

public class WrapperException extends RuntimeException {

    public WrapperException(Exception cause) {
        super(cause.getMessage(), cause);
    }

    @Override
    public synchronized Exception getCause() {
        return (Exception) super.getCause();
    }
}
