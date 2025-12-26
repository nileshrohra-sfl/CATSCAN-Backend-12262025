package com.sfl.core.web.rest;

import com.sfl.core.service.FavoriteProductService;
import com.sfl.core.service.dto.FavouriteProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Slf4j
public class FavoriteProductController {

    private final FavoriteProductService favoriteProductService;


    /**
     * POST  /api/favorites : Create a new favorite list.
     *
     * @param products the list of favorite products to create
     * @return the ResponseEntity with status 200 (OK) and the created favorite products in body
     */
    @PostMapping
    public ResponseEntity<List<FavouriteProductDTO>> create(@RequestBody List<FavouriteProductDTO> products) {
        log.info("Request to create FavoriteList with products: {}", products);
        var response = favoriteProductService.create(products);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT  /api/favorites : Update an existing favorite product.
     *
     * @param request the favorite product data to update
     * @return the ResponseEntity with status 200 (OK) and the updated favorite product in body
     */
    @PutMapping
    public ResponseEntity<FavouriteProductDTO> update(@RequestBody FavouriteProductDTO request) {
        log.info("Request to update FavoriteProduct with id: {}", request.getId());
        var response = favoriteProductService.update(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET  /api/favorites/:id : get the "id" favorite product.
     *
     * @param id the id of the favorite product to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the favorite product, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<FavouriteProductDTO> getById(@PathVariable Long id) {
        log.info("Request to get FavoriteProduct by id: {}", id);
        var response = favoriteProductService.getById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET  /api/favorites : get all favorite product lists for a user with pagination.
     *
     * @param userId   the id of the user to filter favorite lists
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of favorite lists in body
     */
    @GetMapping
    public ResponseEntity<Page<FavouriteProductDTO>> getAll(@RequestParam(required = false) Long userId, Pageable pageable) {
        log.info("Request to get all FavoriteLists for userId: {}", userId);
        Page<FavouriteProductDTO> result = favoriteProductService.getAll(userId, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE  /api/favorites/:id : delete the "id" favorite product.
     *
     * @param id the id of the favorite product to delete
     * @return the ResponseEntity with status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request to delete FavoriteProduct by id: {}", id);
        favoriteProductService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
