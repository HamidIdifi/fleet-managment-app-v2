package com.fleetManagement.app.exception.handler;


import com.fleetManagement.app.dto.ValidationResponse;
import com.fleetManagement.app.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class GenericGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    final Logger LOG = LoggerFactory.getLogger(GenericGlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .message(ex.getMessage().split(":")[0])
                        .build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ValidationResponse> validations = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ValidationResponse.builder()
                        .field(fieldError.getField())
                        .message(getMessage(fieldError))
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(validations);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleException(final UnauthorizedException e) {
        return getResponseEntity(UNAUTHORIZED, e);
    }

    @ExceptionHandler(value = CurrencyConversionException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(final CurrencyConversionException e) {
        return getResponseEntity(INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(final BadRequestException e) {
        return getResponseEntity(BAD_REQUEST, e);
    }

    @ExceptionHandler(value = ElementNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleException(final ElementNotFoundException e) {
        LOG.warn("Warning", getMessage(e));
        return getResponseEntity(NOT_FOUND, e);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(final IllegalArgumentException e) {
        LOG.warn("Warning", e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(ErrorResponse.builder().code(BAD_REQUEST.value()).status(BAD_REQUEST).message(e.getMessage()).build());
    }

    @ExceptionHandler(value = ElementAlreadyExistException.class)
    @ResponseStatus(FOUND)
    public ResponseEntity<ErrorResponse> handleException(final ElementAlreadyExistException e) {
        return getResponseEntity(FOUND, e);
    }

    @ExceptionHandler(value = ElementNotUniqueException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ErrorResponse> handleException(final ElementNotUniqueException e) {
        return getResponseEntity(CONFLICT, e);
    }


    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(final BusinessException e) {
        return getResponseEntity(INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(value = ResourceDeletionNotAllowedException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ErrorResponse> handleException(final ResourceDeletionNotAllowedException e) {
        return getResponseEntity(CONFLICT, e);
    }

    @ExceptionHandler(value = LinkNotFoundException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(final LinkNotFoundException e) {
        return getResponseEntity(INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(value = TransactionAlreadyCompletedException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ErrorResponse> handleException(final TransactionAlreadyCompletedException e) {
        return getResponseEntity(CONFLICT, e);
    }

    @ExceptionHandler(value = UnauthorizedPurchaseException.class)
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleException(final UnauthorizedPurchaseException e) {
        return getResponseEntity(FORBIDDEN, e);
    }


    @ExceptionHandler(IOException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message(ex.getMessage().split(":")[0])
                        .build());
    }


    private ResponseEntity<ErrorResponse> getResponseEntity(final HttpStatus status, final BusinessException e) {
        if (Objects.isNull(e.getKey())) {
            // default message
            return ResponseEntity.status(status)
                    .body(ErrorResponse.builder().code(status.value()).status(status).message(e.getMessage()).build());
        }
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder().code(status.value()).status(status).message(getMessage(e)).build());
    }

    // message from properties
    private String getMessage(final BusinessException e) {
        return messageSource.getMessage(e.getKey(), e.getArgs(), null);
    }

    private String getMessage(final FieldError fieldError) {

        return fieldError.getDefaultMessage() != null && !fieldError.getDefaultMessage().trim().isEmpty() ? fieldError.getDefaultMessage() :
                messageSource.getMessage(Objects.requireNonNull(fieldError.getCode()), fieldError.getArguments(), null);
    }

}
