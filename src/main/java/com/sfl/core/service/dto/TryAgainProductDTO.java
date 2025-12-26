package com.sfl.core.service.dto;


import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class TryAgainProductDTO {

    private Long id;
    private Long prodId;
    private String prodName;
    private String image;
    private List<String> originalOcrResult;
    private List<TryAgainProductResultJsonData> result;
    private Map<String, String> dynamicOcrResult;
    private String rawOcr;
    private String sessionId;
    private String userSelection;
    private Long userId;
    @Nullable
    private String createdBy;
    @Nullable
    private String createdDate;
    @Nullable
    private String lastModifiedBy;
    @Nullable
    private String lastModifiedDate;

    public TryAgainProductDTO() {
    }

    public TryAgainProductDTO(Long id, Long prodId, String prodName, String image, List<String> originalOcrResult, List<TryAgainProductResultJsonData> result, String rawOcr, @Nullable String createdBy, @Nullable String createdDate, @Nullable String lastModifiedBy, @Nullable String lastModifiedDate) {
        this.id = id;
        this.prodId = prodId;
        this.prodName = prodName;
        this.image = image;
        this.originalOcrResult = originalOcrResult;
        this.result = result;
        this.rawOcr = rawOcr;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getOriginalOcrResult() {
        return originalOcrResult;
    }

    public void setOriginalOcrResult(List<String> originalOcrResult) {
        this.originalOcrResult = originalOcrResult;
    }

    public List<TryAgainProductResultJsonData> getResult() {
        return result;
    }

    public void setResult(List<TryAgainProductResultJsonData> result) {
        this.result = result;
    }

    public Map<String, String> getDynamicOcrResult() {
        return dynamicOcrResult;
    }

    public void setDynamicOcrResult(Map<String, String> dynamicOcrResult) {
        this.dynamicOcrResult = dynamicOcrResult;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Nullable
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(@Nullable String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Nullable
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(@Nullable String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getRawOcr() {
        return rawOcr;
    }

    public void setRawOcr(String rawOcr) {
        this.rawOcr = rawOcr;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserSelection() {
        return userSelection;
    }

    public void setUserSelection(String userSelection) {
        this.userSelection = userSelection;
    }
}
