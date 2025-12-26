package com.sfl.core.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {


    private StringUtil() {
    }

    public static String concatString(String s1, String s2){
            return s1.concat(s2);
    }

    public static String getCountryDialCode(String countryCode, String defaultCode){
        return StringUtils.isNotBlank(countryCode) ? countryCode : defaultCode;
    }

    public static Long getLong(String s){
        return StringUtils.isNotBlank(s)? Long.parseLong(s) : null;
    }

    public static boolean isNotBlank(Object object){
        return object != null;
    }
}
