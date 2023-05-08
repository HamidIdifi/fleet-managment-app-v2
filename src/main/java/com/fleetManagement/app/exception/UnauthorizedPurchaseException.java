package com.fleetManagement.app.exception;



public class UnauthorizedPurchaseException extends BusinessException{

    public UnauthorizedPurchaseException() {
        super();
    }

    public UnauthorizedPurchaseException(String defaultMessage, String key, Object[] args) {
        super(defaultMessage, key, args);
    }

    public UnauthorizedPurchaseException(String defaultMessage, Throwable cause, String key, Object[] args) {
        super(defaultMessage, cause, key, args);
    }

    public UnauthorizedPurchaseException(Throwable cause, String key, Object[] args) {
        super(cause, key, args);
    }
}
