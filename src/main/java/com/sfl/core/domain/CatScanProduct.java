package com.sfl.core.domain;


import javax.persistence.*;

@Entity
@Table(name = "cat_scan_product")
public class CatScanProduct extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cat_scan_product_id")
    private String  productId;

    @Column(name = "cat_scan_product_tokens")
    private String  productToken;

    @Column(name = "product_ingredients_text_orig")
    private String  ingredientsTextOrig;

    @Column(name = "toxin_name_to_display")
    private String  toxinNameToDisplay;

    @Column(name = "toxin_name_id_dict")
    private String  toxinNameIdDict;

    @Column(name = "product_score")
    private String  prodScore;

    @Column(name = "product_category")
    private String  prodCategory;

    @Column(name = "product_name")
    private String  productName;

    @Column(name = "product_upc")
    private String  prodUpc;

    @Column(name = "alt_prods_cat_scan_ids")
    private String  altProdCatScanIds;

    @Column(name = "alt_prods_names")
    private String  altProdsNames;

    @Column(name = "is_deleted")
    private boolean  deleted;

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

    public String getAltProdsNames() {
        return altProdsNames;
    }

    public void setAltProdsNames(String altProdsNames) {
        this.altProdsNames = altProdsNames;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
