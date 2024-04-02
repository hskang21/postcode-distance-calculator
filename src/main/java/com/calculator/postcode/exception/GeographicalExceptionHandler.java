package com.calculator.postcode.exception;

import com.calculator.postcode.error.GeographicalError;
import com.calculator.postcode.model.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class GeographicalExceptionHandler {

    @ExceptionHandler(value = {GeographicalException.class})
    public ResponseEntity<Object> handleCalculatorException(GeographicalException e, WebRequest request) {
        log.error("===== Handling CalculatorException => {}:{}", e.getErrorCode(), e.getErrorMessage(), e);
        return new ResponseEntity<>(new ResponseWrapper<>().fail(e.getErrorCode(), e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("===== Handling AccessDeniedException => {}", e.getMessage(), e);
        return new ResponseEntity<>(new ResponseWrapper<>().fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {InsufficientAuthenticationException.class})
    public ResponseEntity<Object> handleInsufficientAuthenticationException(InsufficientAuthenticationException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("===== Handling InsufficientAuthenticationException => {}", e.getMessage(), e);
        return new ResponseEntity<>(new ResponseWrapper<>().fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("===== Handling AuthenticationException => {}", e.getMessage(), e);
        return new ResponseEntity<>(new ResponseWrapper<>().fail(String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class, Throwable.class})
    public ResponseEntity<Object> handleAllException(Exception e, WebRequest request) {
        log.error("===== Handling Exception => {}", e.getMessage(), e);
        GeographicalException generalError = new GeographicalException(GeographicalError.GENERAL_ERROR, e);
        return new ResponseEntity<>(new ResponseWrapper<>().fail(generalError.getErrorCode(), generalError.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
