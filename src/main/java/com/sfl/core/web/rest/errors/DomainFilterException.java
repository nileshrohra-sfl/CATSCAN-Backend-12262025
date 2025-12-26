package com.sfl.core.web.rest.errors;

import org.springframework.http.HttpStatus;

public class DomainFilterException extends FilterException {

    public DomainFilterException(String message, int applicationStatusCode, HttpStatus httpStatus) {
        super(message, applicationStatusCode, httpStatus);
    }

}
