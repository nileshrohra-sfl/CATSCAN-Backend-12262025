package com.sfl.core.web.rest.errors;

import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException {
    protected final int applicationStatusCode;
    protected final HttpStatus httpStatus;

    public GenericException(String message, int applicationStatusCode, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.applicationStatusCode = applicationStatusCode;
        this.httpStatus = httpStatus;
    }

    public GenericException(Throwable cause) {
        super(cause);
        applicationStatusCode = 500;
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public int getApplicationStatusCode() {
        return applicationStatusCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
