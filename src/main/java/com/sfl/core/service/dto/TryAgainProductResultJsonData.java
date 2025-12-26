package com.sfl.core.service.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class TryAgainProductResultJsonData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String prodId;
    private String prodName;
    private List<String> ocrToken;
    private List<String> exactMatches;
    private List<String> distanceMatches;
    private Long matchScores;


    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public List<String> getOcrToken() {
        return ocrToken;
    }

    public void setOcrToken(List<String> ocrToken) {
        this.ocrToken = ocrToken;
    }

    public List<String> getExactMatches() {
        return exactMatches;
    }

    public void setExactMatches(List<String> exactMatches) {
        this.exactMatches = exactMatches;
    }

    public List<String> getDistanceMatches() {
        return distanceMatches;
    }

    public void setDistanceMatches(List<String> distanceMatches) {
        this.distanceMatches = distanceMatches;
    }

    public Long getMatchScores() {
        return matchScores;
    }

    public void setMatchScores(Long matchScores) {
        this.matchScores = matchScores;
    }
}
