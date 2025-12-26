package com.sfl.core.service.dto;

import java.util.List;

public class TryAgainProductSaveDTO {
    private Long id;
    private String image;
    private List<String> originalOcrResult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
