package com.sfl.core.web.rest.vm;

import java.util.List;

public class CustomExceptionVM {

    private String message;
    private int applicationStatusCode;
    private int httpStatus;
    private List<String> fieldErrors;

    public CustomExceptionVM(String message, int applicationStatusCode, int httpStatus) {
        this.message = message;
        this.applicationStatusCode = applicationStatusCode;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getApplicationStatusCode() {
        return applicationStatusCode;
    }

    public void setApplicationStatusCode(int applicationStatusCode) {
        this.applicationStatusCode = applicationStatusCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public List<String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public CustomExceptionVM(List<String> fieldErrors, String message, int applicationStatusCode, int httpStatus) {
        this.fieldErrors = fieldErrors;
        this.message = message;
        this.applicationStatusCode = applicationStatusCode;
        this.httpStatus = httpStatus;
    }
}
