package com.sfl.core.web.rest.errors;

import org.springframework.http.HttpStatus;

public class FilterException extends RuntimeException {

    protected final int applicationStatusCode;
    protected final HttpStatus httpStatus;

    public FilterException(Throwable e, int applicationStatusCode, HttpStatus httpStatus) {
        super(e.getClass() + ":" + e.getMessage(), e);
        this.applicationStatusCode = applicationStatusCode;
        this.httpStatus = httpStatus;
    }

    public FilterException(String message, int applicationStatusCode, HttpStatus httpStatus) {
        super(message);
        this.applicationStatusCode = applicationStatusCode;
        this.httpStatus = httpStatus;
    }

    public FilterException(int applicationStatusCode, HttpStatus httpStatus, Throwable e) {
        super(e.getClass() + ":" + e.getMessage(), e);
        this.applicationStatusCode = applicationStatusCode;
        this.httpStatus = httpStatus;
    }

}
