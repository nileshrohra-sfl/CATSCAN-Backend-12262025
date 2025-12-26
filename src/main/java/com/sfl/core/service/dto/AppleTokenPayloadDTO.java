package com.sfl.core.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppleTokenPayloadDTO {

    private String iss;
    private String aud;
    private Long exp;
    private Long iat;
    private String sub;

    @JsonProperty("at_hash")
    private String atHash;

    @JsonProperty("auth_time")
    private Long authTime;

    @JsonProperty("nonce_supported")
    private Boolean nonceSupported;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    private String email;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAtHash() {
        return atHash;
    }

    public void setAtHash(String atHash) {
        this.atHash = atHash;
    }

    public Long getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Long authTime) {
        this.authTime = authTime;
    }

    public Boolean getNonceSupported() {
        return nonceSupported;
    }

    public void setNonceSupported(Boolean nonceSupported) {
        this.nonceSupported = nonceSupported;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "AppleTokenPayloadDTO{" +
            "iss='" + iss + '\'' +
            ", aud='" + aud + '\'' +
            ", exp=" + exp +
            ", iat=" + iat +
            ", sub='" + sub + '\'' +
            ", atHash='" + atHash + '\'' +
            ", authTime=" + authTime +
            ", nonceSupported=" + nonceSupported +
            ", emailVerified=" + emailVerified +
            ", email='" + email + '\'' +
            '}';
    }
}
