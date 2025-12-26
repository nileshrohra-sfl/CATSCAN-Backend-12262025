package com.sfl.core.service.dto;

public class SocialLoginKeyValueDTO {

    String key;
    String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SsoLoginKeyValueDTO{" +
            "key='" + key + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
