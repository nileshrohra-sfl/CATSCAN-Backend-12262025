package com.sfl.core.service.dto;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonProperty;

public class AppleTokenResponseDTO {

    private  String idToken;
    private  String accessToken;
    private  String tokenType;
    private  Long expiresIn;
    private  String refreshToken;

    public AppleTokenResponseDTO(@JsonProperty("id_token") String idToken,
                            @JsonProperty("access_token") String accessToken,
                            @JsonProperty("token_type") String tokenType,
                            @JsonProperty("expires_in") Long expiresIn,
                            @JsonProperty("refresh_token") String refreshToken) {
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "AppleTokenResponseDTO{" +
            "idToken='" + idToken + '\'' +
            ", accessToken='" + accessToken + '\'' +
            ", tokenType='" + tokenType + '\'' +
            ", expiresIn=" + expiresIn +
            ", refreshToken='" + refreshToken + '\'' +
            '}';
    }
}
