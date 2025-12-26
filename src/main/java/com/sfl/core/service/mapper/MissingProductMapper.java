package com.sfl.core.service.mapper;

import com.sfl.core.domain.MissingProduct;
import com.sfl.core.service.dto.MissingProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {CommonAfterMapper.class, SflUserMapper.class})
public interface MissingProductMapper extends EntityMapper<MissingProductDTO, MissingProduct> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "userName", expression = "java(missingProduct.getUser().getLastName()!=null ? " +
        "missingProduct.getUser().getFirstName() +' '+ missingProduct.getUser().getLastName() : " +
        "missingProduct.getUser().getFirstName() )")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "barcodeImage", target = "barcodeImage", qualifiedByName = "fullUrl")
    @Mapping(source = "productFrontImage", target = "productFrontImage", qualifiedByName = "fullUrl")
    @Mapping(source = "catscanErrorImage", target = "catscanErrorImage", qualifiedByName = "fullUrl")
    @Mapping(source = "ingredientImage", target = "ingredientImage", qualifiedByName = "fullUrl")
    @Mapping(source = "otherImages", target = "otherImages", qualifiedByName = "fullUrls")
    @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = "instantToString")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate", qualifiedByName = "instantToString")
    MissingProductDTO toDto(MissingProduct missingProduct);

    @Mapping(source = "userId", target = "user")
    MissingProduct toEntity(MissingProductDTO missingProductDTO);
}

