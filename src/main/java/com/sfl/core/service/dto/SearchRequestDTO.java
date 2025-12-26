package com.sfl.core.service.dto;

import java.util.List;

/**
 * A SearchRequestBodyDTO
 */
public class SearchRequestDTO {

    private List<KeywordDTO> keywordList;

    public List<KeywordDTO> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<KeywordDTO> keywordList) {
        this.keywordList = keywordList;
    }

    @Override
    public String toString() {
        return "SearchRequestDTO{" +
            "keywordList=" + keywordList +
            '}';
    }
}
