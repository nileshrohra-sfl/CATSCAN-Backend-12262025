package com.sfl.core.service.mapper;

import com.sfl.core.domain.FavoriteProduct;
import com.sfl.core.service.dto.FavouriteProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommonAfterMapper.class})
public interface FavouriteProductMapper extends EntityMapper<FavouriteProductDTO, FavoriteProduct> {

    FavoriteProduct toEntity(FavouriteProductDTO favouriteProductDTO);

    @Mapping(source = "url", target = "url", qualifiedByName = "fullUrl")
    FavouriteProductDTO toDto(FavoriteProduct favoriteProduct);

    default FavoriteProduct fromId(Long id) {
        if (id == null) {
            return null;
        }
        FavoriteProduct favoriteProduct = new FavoriteProduct();
        favoriteProduct.setId(id);
        return favoriteProduct;
    }
}
