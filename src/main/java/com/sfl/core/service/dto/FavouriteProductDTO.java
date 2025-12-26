package com.sfl.core.service.dto;

public class FavouriteProductDTO {
    private Long id;
    private Long prodId;
    private String prodName;
    private Integer prodScore;
    private String url;

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

    public Integer getProdScore() {
        return prodScore;
    }

    public void setProdScore(Integer prodScore) {
        this.prodScore = prodScore;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
