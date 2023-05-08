package com.fleetManagement.app.exception;

public class ResourceDeletionNotAllowedException extends BusinessException{

    public ResourceDeletionNotAllowedException() {
        super();
    }

    public ResourceDeletionNotAllowedException(String defaultMessage, String key, Object[] args) {
        super(defaultMessage, key, args);
    }

    public ResourceDeletionNotAllowedException(String defaultMessage, Throwable cause, String key, Object[] args) {
        super(defaultMessage, cause, key, args);
    }

    public ResourceDeletionNotAllowedException(Throwable cause, String key, Object[] args) {
        super(cause, key, args);
    }
}
