package com.sfl.core.security.jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MobileLoginSecretProperties {
    @Value("${app.mobile-login.secret}")
    private String appSecret;

    @Value("${app.mobile-login.value}")
    private String appSecretValue;

    @Value("${app.mobile-login.base64.key}")
    private String base64Key;

    @Value("${app.mobile-login.base64.iv}")
    private String base64Iv;

    // Getters for the properties
    public String getAppSecret() {
        return appSecret;
    }

    public String getAppSecretValue() {
        return appSecretValue;
    }

    public String getBase64Key() {
        return base64Key;
    }

    public String getBase64Iv() {
        return base64Iv;
    }
}
