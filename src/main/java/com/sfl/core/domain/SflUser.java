package com.sfl.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sfl.core.domain.audit.EntityAuditListener;
import com.sfl.core.service.util.ObjectSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A SflUser.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(EntityAuditListener.class)
public class SflUser extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "user_name")
    protected String userName;

    @Column(name = "email")
    protected String email;

    @Column(name = "phone_number")
    protected Long phoneNumber;

    @Column(name = "password")
    protected String password;

    @Column(name = "image_url")
    protected String imageUrl;

    @Column(name = "first_name")
    protected String firstName;

    @Column(name = "last_name")
    protected String lastName;

    @Column(name = "activation_key")
    protected String activationKey;

    @Column(name = "status")
    protected Boolean status;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    protected String resetKey;

    @Column(name = "reset_date")
    protected Instant resetDate = null;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority" ,
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Authority> authorities = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    private CountryDialCode countryDialCode;


    @Column(name = "deleted_by", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private String deletedBy;


    @Column(name = "deleted_date", updatable = false)
    @JsonIgnore
    private Instant deletedDate;

    @Column(name = "is_deleted")
    protected Boolean isDeleted = false;


    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public SflUser userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public SflUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public SflUser phoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public SflUser password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public SflUser imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public SflUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SflUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public SflUser activationKey(String activationKey) {
        this.activationKey = activationKey;
        return this;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public Boolean getStatus() {
        return status;
    }

    public SflUser status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public SflUser authorities(Set<Authority> authorities) {
        this.authorities = authorities;
        return this;
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

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public CountryDialCode getCountryDialCode() {
        return countryDialCode;
    }

    public void setCountryDialCode(CountryDialCode countryDialCode) {
        this.countryDialCode = countryDialCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


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
        if (!(o instanceof SflUser)) {
            return false;
        }
        return id != null && id.equals(((SflUser) o).id);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hashCode(getId());
//    }

    // prettier-ignore
    @Override
    public String toString() {
        return ObjectSerializer.serialize(this);
    }


}
