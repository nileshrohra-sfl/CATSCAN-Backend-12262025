package com.sfl.core.domain;


import javax.persistence.*;

@Entity
@Table(name = "favorite_product")
public class FavoriteProduct extends AbstractAuditingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prod_id")
    private Long prodId;

    @Column(name = "prod_name")
    private String prodName;

    @Column(name = "prod_score")
    private Integer prodScore;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
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

    public SflUser getUser() {
        return user;
    }

    public void setUser(SflUser user) {
        this.user = user;
    }
}

