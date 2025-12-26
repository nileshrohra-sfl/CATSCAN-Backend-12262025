package com.sfl.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sfl.core.domain.enumeration.MissingProductType;
import com.sfl.core.util.ListToJsonConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "missing_product")
public class MissingProduct extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MissingProductType missingProductType;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "description")
    private String description;

    @Column(name = "barcode_image")
    private String barcodeImage;

    @Column(name = "product_barcode")
    private String productBarcode;

    @Column(name = "product_front_image")
    private String productFrontImage;

    @Column(name = "urep_front_pkg_ocr")
    private String frontPkgOcr;

    @Column(name = "catscan_error_image")
    private String catscanErrorImage;

    @Column(name = "urep_cs_error_ocr")
    private String csErrorOcr;

    @Column(name = "ingredient_image")
    private String ingredientImage;

    @Column(name = "urep_ingred_ocr")
    private String ingredOcr;

    @Column(name = "reference_website")
    private String referenceWebsite;

    @Column(name = "more_about")
    private String moreAbout;


    @Convert(converter = ListToJsonConverter.class)
    @Column(columnDefinition = "json")
    private List<String> otherImages;

    @Column(name = "urep_opt1_ocr")
    private String opt1Ocr;

    @Column(name = "urep_opt2_ocr")
    private String opt2Ocr;


    @ManyToOne
    @JsonIgnoreProperties(allowSetters = true)
    private SflUser user;

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

    public SflUser getUser() {
        return user;
    }

    public void setUser(SflUser user) {
        this.user = user;
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
