package com.sfl.core.service.dto;

public class VerifyOtpDTO {

    private String accessToken;
    private String refreshToken;
    private Long id;

    public VerifyOtpDTO() {
    }

    public VerifyOtpDTO(String accessToken, String refreshToken, Long id) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
