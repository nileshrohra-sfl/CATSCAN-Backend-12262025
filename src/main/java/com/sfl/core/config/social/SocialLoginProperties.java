package com.sfl.core.config.social;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sso", ignoreUnknownFields = false)
public class SocialLoginProperties {

    private final Google google = new Google();

    private final Apple apple = new Apple();

    private final Facebook facebook = new Facebook();

    public Google getGoogle() {
        return google;
    }

    public Apple getApple() {
        return apple;
    }

    public Facebook getFacebook() {
        return facebook;
    }

    public static class Google {
        private String appleClientId;
        private String clientId;

        public String getAppleClientId() {
            return appleClientId;
        }

        public void setAppleClientId(String appleClientId) {
            this.appleClientId = appleClientId;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }
    }

    public static class Apple {
        private String appId;
        private String keyId;
        private String teamId;
        private String authUrl;
        private String audienceUrl;
        public static final Long APPLE_HTTP_REQUEST_TIME_OUT = 70L;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getKeyId() {
            return keyId;
        }

        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getAuthUrl() {
            return authUrl;
        }

        public void setAuthUrl(String authUrl) {
            this.authUrl = authUrl;
        }

        public String getAudienceUrl() {
            return audienceUrl;
        }

        public void setAudienceUrl(String audienceUrl) {
            this.audienceUrl = audienceUrl;
        }
    }

    public static class Facebook {
        private String appId;
        private String appSecret;
        private String profileDetailUrl;
        private String verifyTokenUrl;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }

        public String getProfileDetailUrl() {
            return profileDetailUrl;
        }

        public void setProfileDetailUrl(String profileDetailUrl) {
            this.profileDetailUrl = profileDetailUrl;
        }

        public String getVerifyTokenUrl() {
            return verifyTokenUrl;
        }

        public void setVerifyTokenUrl(String verifyTokenUrl) {
            this.verifyTokenUrl = verifyTokenUrl;
        }
    }
}
