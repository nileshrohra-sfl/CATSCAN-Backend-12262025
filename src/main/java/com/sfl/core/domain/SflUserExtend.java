package com.sfl.core.domain;

import com.sfl.core.service.util.ObjectSerializer;
import com.sfl.core.util.CatscanHashMapConverter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A SflUserExtend.
 */
@Entity
@Table(name = "sfl_user_extend")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SflUserExtend extends SflUser implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column(name = "address")
    private String address;

    @Convert(converter = CatscanHashMapConverter.class)
    @Column(columnDefinition = "json", name = "allergens")
    private Map<String, Object> allergens;
//    @Column(name = "allergens")
//    private String allergens;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SflUserExtend() {

    }

    public SflUserExtend(SflUser sflUser) {
        // Initialize fields from SflUser
        this.id = sflUser.getId();
        this.userName =sflUser.getUserName();
        this.email = sflUser.getEmail();
        this.phoneNumber = sflUser.getPhoneNumber();
        this.imageUrl = sflUser.getImageUrl();
        this.firstName = sflUser.getFirstName();
        this.lastName = sflUser.getLastName();
        this.activationKey = sflUser.getActivationKey();
        this.status = sflUser.getStatus();
        this.resetKey = sflUser.getResetKey();
        this.resetDate = sflUser.getResetDate();
        this.setAuthorities(sflUser.getAuthorities());
        this.setCountryDialCode(sflUser.getCountryDialCode());
        this.setCreatedBy(sflUser.getCreatedBy());
        this.setCreatedDate(sflUser.getCreatedDate());
        this.setLastModifiedBy(sflUser.getLastModifiedBy());
        this.setLastModifiedDate(sflUser.getLastModifiedDate());
        this.setDeletedBy(sflUser.getDeletedBy());
        this.setDeletedDate(sflUser.getDeletedDate());
        this.isDeleted = sflUser.getIsDeleted();
        // Copy other fields as needed
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SflUserExtend address(String address) {
        this.address = address;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    public Map<String, Object> getAllergens() {
        return allergens;
    }

    public void setAllergens(Map<String, Object> allergens) {
        this.allergens = allergens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SflUserExtend)) {
            return false;
        }
        return id != null && id.equals(((SflUserExtend) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return ObjectSerializer.serialize(this);
    }
}
