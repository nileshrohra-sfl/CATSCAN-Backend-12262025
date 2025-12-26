package com.sfl.core.service.dto;

public class JwtTokenDTO {

    private String accessToken;
    private String refreshToken;
    private Boolean isProfileExist = Boolean.FALSE;

    public JwtTokenDTO() {
    }

    public JwtTokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtTokenDTO(Boolean isProfileExist) {
        this.isProfileExist = isProfileExist;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Boolean getIsProfileExist() {
        return isProfileExist;
    }

    public void setIsProfileExist(Boolean profileExist) {
        isProfileExist = profileExist;
    }

    @Override
    public String toString() {
        return "JwtTokenDTO{" +
            "accessToken='" + accessToken + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            '}';
    }
}
