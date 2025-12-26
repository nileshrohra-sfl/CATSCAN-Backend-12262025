package com.sfl.core.config;

import java.util.regex.Pattern;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String ANY_WILDCARD = "%";
    public static final String SEPARATOR = ",";
    public static final String PATH_SEPARATOR = "\\.";
    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String SPRING_PROFILE_LOCAL = "local";
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_ORDER_BY = "ASC";
    public static final String DEFAULT_ORDER_FIELD = "id";
    public static final String MOBILE_USER_AGENT = "mobile";
    public static final String PLATFORM_TYPE = "platform";
    public static final String USER_ID = "userId";

    public static final Pattern BASE64_PATTERN = Pattern.compile(
        "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$"
    );

    private Constants() {
    }
}
