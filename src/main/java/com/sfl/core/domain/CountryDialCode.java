package com.sfl.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A CountryDialCode.
 */
@Entity
@Table(name = "country_dial_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CountryDialCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "dial_code", nullable = false)
    private String dialCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public CountryDialCode countryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDialCode() {
        return dialCode;
    }

    public CountryDialCode dialCode(String dialCode) {
        this.dialCode = dialCode;
        return this;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryDialCode)) {
            return false;
        }
        return id != null && id.equals(((CountryDialCode) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryDialCode{" +
            "id=" + getId() +
            ", countryName='" + getCountryName() + "'" +
            ", dialCode=" + getDialCode() +
            "}";
    }
}
