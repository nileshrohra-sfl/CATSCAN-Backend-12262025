package com.sfl.core.web.rest.customhandler;

import com.sfl.core.web.rest.errors.GlobalException;
import com.sfl.core.web.rest.vm.CustomExceptionVM;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<CustomExceptionVM> insufficientAuthenticationExceptionHandler() {
        return new ResponseEntity<>(new CustomExceptionVM(Constants.AUTHENTICATION_EXCEPTION_MESSAGE, Constants.UNAUTHORIZED,
            HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomExceptionVM> authenticationExceptionHandler(AuthenticationException e) {
        return new ResponseEntity<>(new CustomExceptionVM(e.getMessage(), Constants.AUTHENTICATION_EXCEPTION_CODE,
            HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<CustomExceptionVM> globalExceptionHandler(GlobalException e) {
        return new ResponseEntity<>(new CustomExceptionVM(e.getMessage(), e.getApplicationStatusCode(),
            e.getHttpStatus().value()), e.getHttpStatus());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CustomExceptionVM> nullPointerExceptionHandler(NullPointerException e) {
        return new ResponseEntity<>(new CustomExceptionVM(e.getMessage(), Constants.NULL_POINTER_EXCEPTION_CODE,
            HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CustomExceptionVM> dataAccessExceptionHandler(DataAccessException e) {
        return new ResponseEntity<>(new CustomExceptionVM(e.getMessage(), Constants.DATA_ACCESS_EXCEPTION_CODE,
            HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<CustomExceptionVM> sqlExceptionHandler(DataIntegrityViolationException e) {
        return new ResponseEntity<>(new CustomExceptionVM(e.getMessage(), Constants.SQL_EXCEPTION_CODE,
            HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomExceptionVM> userNameNotFoundExceptionHandler(UsernameNotFoundException e) {
        return new ResponseEntity<>(new CustomExceptionVM(e.getMessage(), Constants.SECURITY_USER_NOT_FOUND_EXCEPTION_CODE,
            HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionVM> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return new ResponseEntity<>(new CustomExceptionVM(errors, "", Constants.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_CODE,
            HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    public ResponseEntity<CustomExceptionVM> concurrencyFailureExceptionHandler(ConcurrencyFailureException e) {
        return new ResponseEntity<>(new CustomExceptionVM(e.getMessage(), Constants.CONCURRENCY_FAILURE_EXCEPTION_CODE,
            HttpStatus.CONFLICT.value()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<CustomExceptionVM> internalServerErrorExceptionHandler(HttpServerErrorException.InternalServerError e) {
        return new ResponseEntity<>(new CustomExceptionVM(e.getMessage(), Constants.INTERNAL_SERVER_ERROR_EXCEPTION_CODE,
            HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomExceptionVM> accessDeniedExceptionHandler(AccessDeniedException e) {
        return new ResponseEntity<>(new CustomExceptionVM(e.getMessage(),
            Constants.SECURITY_USER_NOT_FOUND_EXCEPTION_CODE,
            HttpStatus.NOT_FOUND.value()), HttpStatus.UNAUTHORIZED);
    }

}
