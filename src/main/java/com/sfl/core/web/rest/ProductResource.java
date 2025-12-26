package com.sfl.core.web.rest;

import com.sfl.core.service.ProductsService;
import com.sfl.core.service.dto.SearchRequestDTO;
import com.sfl.core.service.dto.SearchResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A REST Controller for managing {@link com.sfl.core.domain.Products}
 */
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "javabaseproject")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private final ProductsService productsService;

    public ProductResource(ProductsService productsService) {
        this.productsService = productsService;
    }

    /**
     * {@code POST - /api/search} : To get search data
     *
     * @param searchRequestDTO the {@link SearchRequestDTO}
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body {@link SearchResponseDTO}.
     */
    @PostMapping("/search")
    public ResponseEntity<SearchResponseDTO> getSearchData(@RequestBody SearchRequestDTO searchRequestDTO) {
        log.debug("REST request to get search data for : {} ", searchRequestDTO);
        return ResponseEntity.ok(productsService.getSearchData(searchRequestDTO));
    }
}
