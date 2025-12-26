package com.sfl.core.service;

import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.ToxinsProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ToxinsProductService {

    public ToxinsProductDTO persist(ToxinsProductDTO toxinsProductDTO);

    public Page<ToxinsProductDTO> getFilteredProduct(FilterRequestDTO filterRequestDTO);

    public Page<ToxinsProductDTO> getAllProducts(Pageable pageable);

    public void deleteProduct(Long id);
}
