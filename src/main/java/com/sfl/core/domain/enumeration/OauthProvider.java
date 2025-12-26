package com.sfl.core.domain.enumeration;

public enum OauthProvider {

    FACEBOOK("facebook"), GOOGLE("google"), APPLE("apple");

    String value;

    OauthProvider(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

