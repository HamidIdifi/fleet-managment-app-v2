package com.fleetManagement.app.exception;

public class LinkNotFoundException extends BusinessException{

    public LinkNotFoundException() {
        super();
    }

    public LinkNotFoundException(String defaultMessage, String key, Object[] args) {
        super(defaultMessage, key, args);
    }

    public LinkNotFoundException(String defaultMessage, Throwable cause, String key, Object[] args) {
        super(defaultMessage, cause, key, args);
    }

    public LinkNotFoundException(Throwable cause, String key, Object[] args) {
        super(cause, key, args);
    }
}
