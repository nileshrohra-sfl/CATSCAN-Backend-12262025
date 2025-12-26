package com.sfl.core.service.dto;

import com.sfl.core.domain.CatScanProduct;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CatScanProductDTO {
    private Long id;
    private String  productId;
    private String  productToken;
    private String  ingredientsTextOrig;
    private String  toxinNameToDisplay;
    private String  toxinNameIdDict;
    private String  prodScore;
    private String  prodCategory;
    private String  productName;
    private String  prodUpc;
    private String  altProdCatScanIds;
    private String altProdsNames;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductToken() {
        return productToken;
    }

    public void setProductToken(String productToken) {
        this.productToken = productToken;
    }

    public String getIngredientsTextOrig() {
        return ingredientsTextOrig;
    }

    public void setIngredientsTextOrig(String ingredientsTextOrig) {
        this.ingredientsTextOrig = ingredientsTextOrig;
    }

    public String getToxinNameToDisplay() {
        return toxinNameToDisplay;
    }

    public void setToxinNameToDisplay(String toxinNameToDisplay) {
        this.toxinNameToDisplay = toxinNameToDisplay;
    }

    public String getToxinNameIdDict() {
        return toxinNameIdDict;
    }

    public void setToxinNameIdDict(String toxinNameIdDict) {
        this.toxinNameIdDict = toxinNameIdDict;
    }

    public String getProdScore() {
        return prodScore;
    }

    public void setProdScore(String prodScore) {
        this.prodScore = prodScore;
    }

    public String getProdCategory() {
        return prodCategory;
    }

    public void setProdCategory(String prodCategory) {
        this.prodCategory = prodCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProdUpc() {
        return prodUpc;
    }

    public void setProdUpc(String prodUpc) {
        this.prodUpc = prodUpc;
    }

    public String getAltProdCatScanIds() {
        return altProdCatScanIds;
    }

    public void setAltProdCatScanIds(String altProdCatScanIds) {
        this.altProdCatScanIds = altProdCatScanIds;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getAltProdsNames() {
        return altProdsNames;
    }

    public void setAltProdsNames(String altProdsNames) {
        this.altProdsNames = altProdsNames;
    }
}
