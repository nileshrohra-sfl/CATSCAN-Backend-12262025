package com.sfl.core.service.dto;

/**
 * A KeywordDTO
 */
public class KeywordDTO {

    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "KeywordDTO{" +
            "keyword='" + keyword + '\'' +
            '}';
    }
}
