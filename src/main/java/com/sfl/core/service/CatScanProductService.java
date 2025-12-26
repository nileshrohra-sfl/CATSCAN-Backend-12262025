package com.sfl.core.service;

import com.sfl.core.domain.CatScanProduct;
import com.sfl.core.service.dto.CatScanProductDTO;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.MissingProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CatScanProductService{

    public CatScanProductDTO persist(CatScanProductDTO catScanProductDTO);

    public Page<CatScanProductDTO> getFilteredProduct(FilterRequestDTO filterRequestDTO);

    public Page<CatScanProductDTO> getAllProducts(Pageable pageable);

    public void deleteProduct(Long id);

}
