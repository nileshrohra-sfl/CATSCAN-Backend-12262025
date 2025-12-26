package com.sfl.core.web.rest.vm;

public class CustomMessageVM {

    private String message;
    private int httpStatus;
    private String accessToken;
    private String refreshToken;

    public CustomMessageVM(String message, int httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public CustomMessageVM(String message) {
        this.message = message;
    }

    public CustomMessageVM(String message, String accessToken, String refreshToken) {
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
