package com.sfl.core.service;

import com.sfl.core.service.dto.FavouriteProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteProductService {

    /**
     * Create a new Favourite product.
     *
     * @param products the list of FavouriteProductDTO containing data for the new FavouriteProduct
     * @return the created list of FavouriteProductDTO
     */
    List<FavouriteProductDTO> create(List<FavouriteProductDTO> products);

    /**
     * Update an existing FavoriteProduct
     *
     * @param request the FavouriteProductDTO containing updated data
     * @return the updated FavouriteProductDTO
     */
    FavouriteProductDTO update(FavouriteProductDTO request);

    /**
     * Retrieve a FavoriteProduct by its ID
     *
     * @param id the ID of the FavoriteProduct
     * @return the FavouriteProductDTO
     */
    FavouriteProductDTO getById(Long id);

    /**
     * Retrieve all FavouriteProductDTO for a specific user with pagination
     *
     * @param userId   the ID of the user
     * @param pageable pagination information
     * @return a page of FavouriteProductDTO
     */
    Page<FavouriteProductDTO> getAll(Long userId, Pageable pageable);

    /**
     * Delete a FavoriteProduct by its ID
     *
     * @param id the ID of the FavoriteProduct to delete
     */
    void delete(Long id);

}

