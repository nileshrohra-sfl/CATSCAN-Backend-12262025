package com.sfl.core.service.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sfl.core.domain.Authority;
import com.sfl.core.domain.CountryDialCode;
import com.sfl.core.domain.SflUser;
import com.sfl.core.service.util.ObjectSerializer;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.sfl.core.domain.SflUser} entity.
 */
public class SflUserDTO implements Serializable {

    private Long id;

    private String userName;

    @Email
    private String email;

    private Long phoneNumber;

    private String imageUrl;

    private String firstName;

    private String lastName;

    private String activationKey;

    private Boolean status;

    private String resetKey;

    private Instant resetDate = null;

    private Set<String> authorities;

    private CountryDialCode countryDialCode;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private String deletedBy;

    private Instant deletedDate;

    protected Boolean isDeleted;

    public SflUserDTO() {
    }

    public SflUserDTO(SflUser sflUser) {
        this.id = sflUser.getId();
        this.userName = sflUser.getUserName();
        this.email = sflUser.getEmail();
        this.phoneNumber = sflUser.getPhoneNumber();
        this.imageUrl = sflUser.getImageUrl();
        this.firstName = sflUser.getFirstName();
        this.lastName = sflUser.getLastName();
        this.activationKey = sflUser.getActivationKey();
        this.status = sflUser.getStatus();
        this.resetKey = sflUser.getResetKey();
        this.resetDate = sflUser.getResetDate();
        this.authorities = sflUser.getAuthorities().stream()
            .map(Authority::getName)
            .collect(Collectors.toSet());
        this.createdBy = sflUser.getCreatedBy();
        this.createdDate = sflUser.getCreatedDate();
        this.lastModifiedBy = sflUser.getLastModifiedBy();
        this.lastModifiedDate = sflUser.getLastModifiedDate();
        this.deletedBy = sflUser.getDeletedBy();
        this. deletedDate = sflUser.getDeletedDate();
        this.isDeleted = sflUser.getIsDeleted();
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public CountryDialCode getCountryDialCode() {
        return countryDialCode;
    }

    public void setCountryDialCode(CountryDialCode countryDialCode) {
        this.countryDialCode = countryDialCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Instant getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SflUserDTO)) {
            return false;
        }

        return id != null && id.equals(((SflUserDTO) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return ObjectSerializer.serialize(this);
    }
}
