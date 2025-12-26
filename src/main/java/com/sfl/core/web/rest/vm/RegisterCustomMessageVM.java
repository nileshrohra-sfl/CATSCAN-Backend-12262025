package com.sfl.core.web.rest.vm;

public class RegisterCustomMessageVM  {

    private String message;
    private int httpStatus;
    private Boolean isUserActive;
    private int applicationStatusCode;
    private Long id;

    public RegisterCustomMessageVM(String message, int httpStatus, int applicationStatusCode, Boolean isUserActive, Long id) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.isUserActive = isUserActive;
        this.applicationStatusCode = applicationStatusCode;
        this.id=id;
    }

    public Boolean getUserActive() {
        return isUserActive;
    }

    public void setUserActive(Boolean userActive) {
        isUserActive = userActive;
    }

    public int getApplicationStatusCode() {
        return applicationStatusCode;
    }

    public void setApplicationStatusCode(int applicationStatusCode) {
        this.applicationStatusCode = applicationStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
