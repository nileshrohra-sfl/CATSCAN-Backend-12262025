package com.sfl.core.security.authentication.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentSecurityPolicy {

    private static final String CSS_SELF_DATA = "'self' data:";
    private static final String CSS_SELF = "'self'";
    private static final String CSS_SELF_UNSAFE_INLINE = "'self' 'unsafe-inline'";
    private static final String CSS_SELF_UNSAFE_INLINE_UNSAFE_EVAL = "self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com";

    @JsonProperty("default-src")
    private String defaultSrc = CSS_SELF;
    @JsonProperty("frame-src")
    private String frameSrc = CSS_SELF_DATA;
    @JsonProperty("script-src")
    private String scriptSrc = CSS_SELF_UNSAFE_INLINE_UNSAFE_EVAL;
    @JsonProperty("style-src")
    private String styleSrc = CSS_SELF_UNSAFE_INLINE;
    @JsonProperty("image-src")
    private String imgSrc = CSS_SELF_DATA;
    @JsonProperty("font-src")
    private String fontSrc = CSS_SELF_DATA;

    public ContentSecurityPolicy() {
    }

    public ContentSecurityPolicy(String defaultSrc, String frameSrc, String scriptSrc, String styleSrc, String imgSrc, String fontSrc) {
        this.defaultSrc = defaultSrc;
        this.frameSrc = frameSrc;
        this.scriptSrc = scriptSrc;
        this.styleSrc = styleSrc;
        this.imgSrc = imgSrc;
        this.fontSrc = fontSrc;
    }

    public String getDefaultSrc() {
        return defaultSrc;
    }

    public void setDefaultSrc(String defaultSrc) {
        this.defaultSrc = defaultSrc;
    }

    public ContentSecurityPolicy defaultSrc(String defaultSrc) {
        this.defaultSrc = defaultSrc;
        return this;
    }

    public String getFrameSrc() {
        return frameSrc;
    }

    public void setFrameSrc(String frameSrc) {
        this.frameSrc = frameSrc;
    }

    public ContentSecurityPolicy frameSrc(String frameSrc) {
        this.frameSrc = frameSrc;
        return this;
    }

    public String getScriptSrc() {
        return scriptSrc;
    }

    public void setScriptSrc(String scriptSrc) {
        this.scriptSrc = scriptSrc;
    }

    public ContentSecurityPolicy scriptSrc(String scriptSrc) {
        this.scriptSrc = scriptSrc;
        return this;
    }

    public String getStyleSrc() {
        return styleSrc;
    }

    public void setStyleSrc(String styleSrc) {
        this.styleSrc = styleSrc;
    }

    public ContentSecurityPolicy styleSrc(String styleSrc) {
        this.styleSrc = styleSrc;
        return this;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public ContentSecurityPolicy imgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
        return this;
    }

    public String getFontSrc() {
        return fontSrc;
    }

    public void setFontSrc(String fontSrc) {
        this.fontSrc = fontSrc;
    }

    public ContentSecurityPolicy fontSrc(String fontSrc) {
        this.fontSrc = fontSrc;
        return this;
    }

    @Override
    public String toString() {
        return
            " default-src " + defaultSrc + ";" +
                " frame-src " + frameSrc + ";" +
                " script-src " + scriptSrc + ";" +
                " style-src " + styleSrc + ";" +
                " img-src " + imgSrc + ";" +
                " font-src " + fontSrc + ";";
    }

}
