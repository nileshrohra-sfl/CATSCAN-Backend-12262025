package com.sfl.core.service.mapper;

import com.sfl.core.domain.CountryDialCode;
import com.sfl.core.service.dto.CountryDialCodeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link CountryDialCode} and its DTO {@link CountryDialCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountryDialCodeMapper extends EntityMapper<CountryDialCodeDTO, CountryDialCode> {

    default CountryDialCode fromId(Long id) {
        if (id == null) {
            return null;
        }
        CountryDialCode countryDialCode = new CountryDialCode();
        countryDialCode.setId(id);
        return countryDialCode;
    }

}
