package com.sfl.core.service.dto;

import com.sfl.core.domain.enumeration.OauthProvider;

import java.util.List;

public class SocialOauthDTO {

    private List<SocialLoginKeyValueDTO> tokens;
    private OauthProvider authProvider;

    public List<SocialLoginKeyValueDTO> getTokens() {
        return tokens;
    }

    public void setTokens(List<SocialLoginKeyValueDTO> tokens) {
        this.tokens = tokens;
    }

    public OauthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(OauthProvider authProvider) {
        this.authProvider = authProvider;
    }
}
