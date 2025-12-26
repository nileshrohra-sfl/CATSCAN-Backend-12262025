package com.sfl.core.service.impl;


import com.sfl.core.domain.FavoriteProduct;
import com.sfl.core.domain.SflUser;
import com.sfl.core.repository.FavoriteProductRepository;
import com.sfl.core.service.FavoriteProductService;
import com.sfl.core.service.UserHelperService;
import com.sfl.core.service.dto.FavouriteProductDTO;
import com.sfl.core.service.mapper.FavouriteProductMapper;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteProductServiceImpl implements FavoriteProductService {

    private final UserHelperService userHelperService;
    private final FavoriteProductRepository favoriteProductRepository;
    private final FavouriteProductMapper favouriteProductMapper;


    /**
     * Creates a new FavouriteProduct with the given products.
     *
     * @param products the list of FavouriteProductDTO containing data for the new FavoriteProducts
     * @return the created list of FavouriteProductDTO
     */
    @Override
    @Transactional
    public List<FavouriteProductDTO> create(List<FavouriteProductDTO> products) {
        log.info("Creating FavoriteProducts with products: {}", products);
        SflUser user = userHelperService.getCurrentLoginUser();
        List<FavoriteProduct> favoriteProductList = favouriteProductMapper.toEntity(products);
        favoriteProductList.forEach(fp -> fp.setUser(user));
        favoriteProductList = favoriteProductRepository.saveAll(favoriteProductList);
        return favouriteProductMapper.toDto(favoriteProductList);

    }

    /**
     * Updates an existing FavoriteProduct.
     *
     * @param request the FavouriteProductDTO containing updated data
     * @return the updated FavouriteProductDTO
     * @throws RuntimeException if the FavoriteProduct with the given ID does not exist
     */
    @Override
    @Transactional
    public FavouriteProductDTO update(FavouriteProductDTO request) {
        log.info("Updating FavoriteProduct with id: {}", request.getId());
        FavoriteProduct existingFavoriteProduct = favoriteProductRepository.findById(request.getId())
            .orElseThrow(() -> new GlobalException(Constants.FAVORITE_NOT_FOUND_EXCEPTION_MESSAGE, Constants.FAVORITE_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND));
        existingFavoriteProduct.setProdId(request.getProdId());
        existingFavoriteProduct.setProdName(request.getProdName());
        existingFavoriteProduct.setProdScore(request.getProdScore());
        existingFavoriteProduct.setUrl(request.getUrl());
        existingFavoriteProduct = favoriteProductRepository.save(existingFavoriteProduct);
        return favouriteProductMapper.toDto(existingFavoriteProduct);
    }

    /**
     * Retrieves a FavoriteProduct by its ID.
     *
     * @param id the ID of the FavoriteProduct to retrieve
     * @return the FavoriteProduct
     * @throws RuntimeException if the FavoriteProduct with the given ID does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public FavouriteProductDTO getById(Long id) {
        log.info("Retrieving FavoriteProduct with id: {}", id);
        FavoriteProduct favoriteProduct = favoriteProductRepository.findById(id)
            .orElseThrow(() -> new GlobalException(Constants.FAVORITE_NOT_FOUND_EXCEPTION_MESSAGE, Constants.FAVORITE_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND));
        return favouriteProductMapper.toDto(favoriteProduct);
    }

    /**
     * Retrieves all FavouriteProductDTO for a specific user with pagination.
     *
     * @param userId   the ID of the user
     * @param pageable pagination information
     * @return a page of FavouriteProductDTO
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavouriteProductDTO> getAll(Long userId, Pageable pageable) {
        log.info("Retrieving all FavoriteProducts for userId: {}", userId);
        SflUser currentLoginUser = userHelperService.getCurrentLoginUser();
        Long resolvedUserId = (userId != null) ? userId : currentLoginUser.getId();
        Page<FavoriteProduct> page = favoriteProductRepository.findAllByUserId(resolvedUserId, pageable);
        return page.map(favouriteProductMapper::toDto);
    }


    /**
     * Deletes a FavoriteProduct by its ID.
     *
     * @param id the ID of the FavoriteProduct to delete
     * @throws RuntimeException if the FavoriteProduct with the given ID does not exist
     */
    @Override
    public void delete(Long id) {
        log.info("Deleting FavoriteProduct with id: {}", id);
        if (!favoriteProductRepository.existsById(id)) {
            log.error("FavoriteProduct with id {} not found for deletion", id);
            throw new GlobalException(
                Constants.FAVORITE_NOT_FOUND_EXCEPTION_MESSAGE,
                Constants.FAVORITE_NOT_FOUND_EXCEPTION_CODE,
                HttpStatus.NOT_FOUND
            );
        }
        favoriteProductRepository.deleteById(id);
    }

}

