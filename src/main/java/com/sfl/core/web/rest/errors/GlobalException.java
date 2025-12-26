package com.sfl.core.web.rest.errors;

import org.springframework.http.HttpStatus;

public class GlobalException extends RuntimeException {

    private final String message;
    private final int applicationStatusCode;
    private final HttpStatus httpStatus;


    public GlobalException(String message, int applicationStatusCode, HttpStatus httpStatus) {
        super(message);
        this.applicationStatusCode = applicationStatusCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getApplicationStatusCode() {
        return applicationStatusCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
