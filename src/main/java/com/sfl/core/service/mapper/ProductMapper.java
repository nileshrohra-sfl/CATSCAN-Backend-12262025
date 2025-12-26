package com.sfl.core.service.mapper;

import com.sfl.core.domain.Products;
import com.sfl.core.service.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * A Mapper for {@link Products}
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Products> {

    @Override
    @Mapping(source = "imageUrl", target = "image")
    ProductDTO toDto(Products entity);

    default Products fromId(Long id) {
        if (id == null) {
            return null;
        }
        Products products = new Products();
        products.setId(id);
        return products;
    }
}
