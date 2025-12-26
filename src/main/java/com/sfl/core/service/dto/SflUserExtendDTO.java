package com.sfl.core.service.dto;


import com.sfl.core.domain.SflUser;
import com.sfl.core.service.util.ObjectSerializer;

import java.util.Map;
import java.util.Objects;

/**
 * A DTO for the {@link com.sfl.core.domain.SflUserExtend} entity.
 */
public class SflUserExtendDTO extends SflUserDTO {

    private String address = null;

    private Map<String, Object> allergens = null;

    private Map<String, String> dynamicAllergens;

    private String phoneWithCountryCode ;

    public SflUserExtendDTO() {

    }
    public SflUserExtendDTO(SflUser sflUser) {
        super(sflUser);
    }
    public String getAddress() {
        return address;
    }

    public Map<String, String> getDynamicAllergens() {
        return dynamicAllergens;
    }

    public void setDynamicAllergens(Map<String, String> dynamicAllergens) {
        this.dynamicAllergens = dynamicAllergens;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SflUserExtendDTO sflUserExtendDTO = (SflUserExtendDTO) o;
        if (sflUserExtendDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sflUserExtendDTO.getId());
    }

    public String getPhoneWithCountryCode() {
        return phoneWithCountryCode;
    }

    public void setPhoneWithCountryCode(String phoneWithCountryCode) {
        this.phoneWithCountryCode = phoneWithCountryCode;
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
