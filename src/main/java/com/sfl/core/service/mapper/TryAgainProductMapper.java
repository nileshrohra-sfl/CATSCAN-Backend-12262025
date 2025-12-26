package com.sfl.core.service.mapper;

import com.sfl.core.domain.TryAgainProduct;
import com.sfl.core.service.dto.TryAgainProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring", uses = {CommonAfterMapper.class, SflUserMapper.class})
public interface TryAgainProductMapper extends EntityMapper<TryAgainProductDTO, TryAgainProduct> {

    @Mapping(source = "userId", target = "user")
    TryAgainProduct toEntity(TryAgainProductDTO tryAgainProductDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "image", target = "image", qualifiedByName = "fullUrl")
    @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = "instantToString")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate", qualifiedByName = "instantToString")
    TryAgainProductDTO toDto(TryAgainProduct tryAgainProduct);


    default String instantToString(Instant instant) {
        return instant != null ? instant.toString() : null;
    }

    default TryAgainProduct fromId(Long id) {
        if (id == null) {
            return null;
        }
        TryAgainProduct tryAgainProduct = new TryAgainProduct();
        tryAgainProduct.setId(id);
        return tryAgainProduct;
    }
}
