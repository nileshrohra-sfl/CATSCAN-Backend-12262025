package com.sfl.core.service.impl;

import com.sfl.core.domain.CatScanProduct;
import com.sfl.core.repository.CatScanProductRepository;
import com.sfl.core.service.CatScanProductService;
import com.sfl.core.service.dto.CatScanProductDTO;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.helper.CommonFilterSpecification;
import com.sfl.core.service.mapper.CatScanProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CatScanProductServiceImpl implements CatScanProductService {


    private final CatScanProductRepository catScanProductRepository;

    private final CommonFilterSpecification<CatScanProduct> catScanProductFilterSpecification;

    private final CatScanProductMapper catScanProductMapper;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    public CatScanProductServiceImpl(CatScanProductRepository catScanProductRepository, CommonFilterSpecification<CatScanProduct> catScanProductFilterSpecification, CatScanProductMapper catScanProductMapper) {
        this.catScanProductRepository = catScanProductRepository;
        this.catScanProductFilterSpecification = catScanProductFilterSpecification;
        this.catScanProductMapper = catScanProductMapper;
    }


    @Override
    public CatScanProductDTO persist(CatScanProductDTO catScanProductDTO) {
        CatScanProduct catScanProduct = catScanProductMapper.toEntity(catScanProductDTO);
        return catScanProductMapper.toDto(catScanProductRepository.save(catScanProduct));
    }


    @Override
    public Page<CatScanProductDTO> getFilteredProduct(FilterRequestDTO filterRequestDTO) {
        logger.info("Execute filter search service for cat-scan products");
        Pageable pageable = PageRequest.of(filterRequestDTO.getPageNumber(), filterRequestDTO.getPageSize());
        filterRequestDTO.getFilter().put("deleted",false);
        Specification<CatScanProduct> specification = catScanProductFilterSpecification.applyFilters(filterRequestDTO);
        logger.info("Prepared specifications filter for cat-scan products");
        Page<CatScanProduct> catScanProducts = catScanProductRepository.findAll(specification, pageable);
        logger.info("specifications are executed.");
        return catScanProducts.map(catScanProductMapper::toDto);
    }

    @Override
    public Page<CatScanProductDTO> getAllProducts(Pageable pageable) {
        return catScanProductRepository.findAll(pageable).map(catScanProductMapper::toDto);
    }

    @Override
    public void deleteProduct(Long id) {
        catScanProductRepository.updateDeleteStatus(true,id);
    }

}
