package com.fleetManagement.app.exception;

public class CurrencyConversionException extends BusinessException{

    public CurrencyConversionException() {
        super();
    }

    public CurrencyConversionException(String defaultMessage, String key, Object[] args) {
        super(defaultMessage, key, args);
    }

    public CurrencyConversionException(String defaultMessage, Throwable cause, String key, Object[] args) {
        super(defaultMessage, cause, key, args);
    }

    public CurrencyConversionException(Throwable cause, String key, Object[] args) {
        super(cause, key, args);
    }
}
