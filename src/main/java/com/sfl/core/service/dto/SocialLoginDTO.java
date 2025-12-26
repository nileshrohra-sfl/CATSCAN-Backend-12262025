package com.sfl.core.service.dto;

import com.sfl.core.domain.enumeration.OauthProvider;

public class SocialLoginDTO {

    private String name;
    private String email;
    private OauthProvider oauthProvider;

    public SocialLoginDTO() { }

    public SocialLoginDTO(String name, String email, OauthProvider oauthProvider) {
        this.name = name;
        this.email = email;
        this.oauthProvider = oauthProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OauthProvider getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(OauthProvider oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    @Override
    public String toString() {
        return "SocialLoginDTO{" +
            "name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", oauthProvider=" + oauthProvider +
            '}';
    }
}
