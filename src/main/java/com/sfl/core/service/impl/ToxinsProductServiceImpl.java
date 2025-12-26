package com.sfl.core.service.impl;

import com.sfl.core.domain.ToxinsProduct;
import com.sfl.core.repository.ToxinsProductRepository;
import com.sfl.core.service.ToxinsProductService;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.ToxinsProductDTO;
import com.sfl.core.service.helper.CommonFilterSpecification;
import com.sfl.core.service.mapper.ToxinsProductMapper;
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
public class ToxinsProductServiceImpl implements ToxinsProductService {

    private final ToxinsProductMapper mapper;

    private final CommonFilterSpecification<ToxinsProduct> catScanProductFilterSpecification;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ToxinsProductRepository toxinsProductRepository;

    public ToxinsProductServiceImpl(ToxinsProductMapper mapper, CommonFilterSpecification<ToxinsProduct> catScanProductFilterSpecification, ToxinsProductRepository toxinsProductRepository) {
        this.mapper = mapper;
        this.catScanProductFilterSpecification = catScanProductFilterSpecification;
        this.toxinsProductRepository = toxinsProductRepository;
    }

    @Override
    public ToxinsProductDTO persist(ToxinsProductDTO toxinsProductDTO) {
        ToxinsProduct catScanProduct = mapper.toEntity(toxinsProductDTO);
        return mapper.toDto(toxinsProductRepository.save(catScanProduct));
    }

    @Override
    public Page<ToxinsProductDTO> getFilteredProduct(FilterRequestDTO filterRequestDTO) {
        logger.info("Execute filter search service for Toxin products");
        Pageable pageable = PageRequest.of(filterRequestDTO.getPageNumber(), filterRequestDTO.getPageSize());
        filterRequestDTO.getFilter().put("deleted",false);
        Specification<ToxinsProduct> specification = catScanProductFilterSpecification.applyFilters(filterRequestDTO);
        logger.info("Prepared specifications filter for toxins products");
        Page<ToxinsProduct> toxinsProducts = toxinsProductRepository.findAll(specification, pageable);
        logger.info("specifications are executed.");
        return toxinsProducts.map(mapper::toDto);
    }

    @Override
    public Page<ToxinsProductDTO> getAllProducts(Pageable pageable) {
        return toxinsProductRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public void deleteProduct(Long id) {
        toxinsProductRepository.updateDeleteStatus(true,id);
    }
}
