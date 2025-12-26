package com.sfl.core.service.dto;

import com.sfl.core.domain.enumeration.MissingProductType;

import javax.annotation.Nullable;
import java.util.List;

public class MissingProductDTO {
    private Long id;
    private MissingProductType missingProductType;
    private String productName;
    private String brandName;
    private String description;
    private String barcodeImage;
    private String productBarcode;
    private String productFrontImage;
    private String frontPkgOcr;
    private String catscanErrorImage;
    private String csErrorOcr;
    private String ingredientImage;
    private String ingredOcr;
    private String referenceWebsite;
    private String moreAbout;
    private List<String> otherImages;
    private String opt1Ocr;
    private String opt2Ocr;
    private Long userId;
    private String userName;
    private String userEmail;

    @Nullable
    private String createdBy;
    @Nullable
    private String createdDate;
    @Nullable
    private String lastModifiedBy;
    @Nullable
    private String lastModifiedDate;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MissingProductType getMissingProductType() {
        return missingProductType;
    }

    public void setMissingProductType(MissingProductType missingProductType) {
        this.missingProductType = missingProductType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcodeImage() {
        return barcodeImage;
    }

    public void setBarcodeImage(String barcodeImage) {
        this.barcodeImage = barcodeImage;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public String getProductFrontImage() {
        return productFrontImage;
    }

    public void setProductFrontImage(String productFrontImage) {
        this.productFrontImage = productFrontImage;
    }

    public String getCatscanErrorImage() {
        return catscanErrorImage;
    }

    public void setCatscanErrorImage(String catscanErrorImage) {
        this.catscanErrorImage = catscanErrorImage;
    }

    public String getIngredientImage() {
        return ingredientImage;
    }

    public void setIngredientImage(String ingredientImage) {
        this.ingredientImage = ingredientImage;
    }

    public String getReferenceWebsite() {
        return referenceWebsite;
    }

    public void setReferenceWebsite(String referenceWebsite) {
        this.referenceWebsite = referenceWebsite;
    }

    public String getMoreAbout() {
        return moreAbout;
    }

    public void setMoreAbout(String moreAbout) {
        this.moreAbout = moreAbout;
    }

    public List<String> getOtherImages() {
        return otherImages;
    }

    public void setOtherImages(List<String> otherImages) {
        this.otherImages = otherImages;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Nullable
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(@Nullable String createdBy) {
        this.createdBy = createdBy;
    }

    @Nullable
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(@Nullable String createdDate) {
        this.createdDate = createdDate;
    }

    @Nullable
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(@Nullable String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Nullable
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(@Nullable String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getFrontPkgOcr() {
        return frontPkgOcr;
    }

    public void setFrontPkgOcr(String frontPkgOcr) {
        this.frontPkgOcr = frontPkgOcr;
    }

    public String getCsErrorOcr() {
        return csErrorOcr;
    }

    public void setCsErrorOcr(String csErrorOcr) {
        this.csErrorOcr = csErrorOcr;
    }

    public String getIngredOcr() {
        return ingredOcr;
    }

    public void setIngredOcr(String ingredOcr) {
        this.ingredOcr = ingredOcr;
    }

    public String getOpt1Ocr() {
        return opt1Ocr;
    }

    public void setOpt1Ocr(String opt1Ocr) {
        this.opt1Ocr = opt1Ocr;
    }

    public String getOpt2Ocr() {
        return opt2Ocr;
    }

    public void setOpt2Ocr(String opt2Ocr) {
        this.opt2Ocr = opt2Ocr;
    }
}
