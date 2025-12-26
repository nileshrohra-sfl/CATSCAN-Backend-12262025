package com.sfl.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfl.core.service.dto.TryAgainProductResultJsonData;
import com.sfl.core.util.CatscanListConverter;
import com.sfl.core.util.ListToJsonConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "try_again_product")
public class TryAgainProduct extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prod_id")
    private Long prodId;
    @Column(name = "prod_name")
    private String prodName;

    @Column(name = "image")
    private String image;

    @Column(name = "raw_ocr")
    private String rawOcr;

    @Convert(converter = ListToJsonConverter.class)
    @Column(columnDefinition = "json")
    private List<String> originalOcrResult;

    @Convert(converter = CatscanListConverter.class)
    @Column(columnDefinition = "json", name = "ocr_result")
    private List<TryAgainProductResultJsonData> result;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "user_selection")
    private String userSelection;

    @ManyToOne
    @JsonIgnoreProperties("user")
    @JoinColumn(name = "user_id")
    private SflUser user;

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

    public String getRawOcr() {
        return rawOcr;
    }

    public void setRawOcr(String rawOcr) {
        this.rawOcr = rawOcr;
    }

    public List<TryAgainProductResultJsonData> getResult() {
        return result;
    }

    public void setResult(List<TryAgainProductResultJsonData> result) {
        this.result = result;
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

    public SflUser getUser() {
        return user;
    }

    public void setUser(SflUser user) {
        this.user = user;
    }

    public List<TryAgainProductResultJsonData> getOcrResult() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(result, new TypeReference<List<TryAgainProductResultJsonData>>() {});
    }
}
