package com.sfl.core.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * A DTO for the {@link com.sfl.core.domain.CountryDialCode} entity.
 */
public class CountryDialCodeDTO implements Serializable {

    private Long id;

    @JsonProperty("name")
    private String countryName;

    @JsonProperty("countryCode")
    private String dialCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryDialCodeDTO)) {
            return false;
        }

        return id != null && id.equals(((CountryDialCodeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryDialCodeDTO{" +
            "id=" + getId() +
            ", countryName='" + getCountryName() + "'" +
            ", dialCode=" + getDialCode() +
            "}";
    }
}
