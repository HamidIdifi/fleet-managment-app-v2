package com.fleetManagement.app.exception;

public class TransactionAlreadyCompletedException extends BusinessException{

    public TransactionAlreadyCompletedException() {
        super();
    }

    public TransactionAlreadyCompletedException(String defaultMessage, String key, Object[] args) {
        super(defaultMessage, key, args);
    }

    public TransactionAlreadyCompletedException(String defaultMessage, Throwable cause, String key, Object[] args) {
        super(defaultMessage, cause, key, args);
    }

    public TransactionAlreadyCompletedException(Throwable cause, String key, Object[] args) {
        super(cause, key, args);
    }
}
