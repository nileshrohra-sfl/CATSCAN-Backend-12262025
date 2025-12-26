package com.sfl.core.service;

import com.sfl.core.service.dto.SearchRequestDTO;
import com.sfl.core.service.dto.SearchResponseDTO;

/**
 * A Service for {@link com.sfl.core.domain.Products}
 */
public interface ProductsService {

    /**
     * Get search data
     *
     * @param searchRequestDTO the {@link SearchRequestDTO}
     * @return the {@link SearchResponseDTO}
     */
    SearchResponseDTO getSearchData(SearchRequestDTO searchRequestDTO);
}
