package com.sfl.core.service;

import com.sfl.core.domain.MissingProduct;
import com.sfl.core.domain.SflUser;
import com.sfl.core.repository.MissingProductRepository;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.MissingProductDTO;
import com.sfl.core.service.helper.CommonFilterSpecification;
import com.sfl.core.service.mapper.MissingProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MissingProductService {

    private final Logger log = LoggerFactory.getLogger(MissingProductService.class);

    @Autowired
    private MissingProductRepository repository;

    @Autowired
    private MissingProductMapper mapper;

    private final CommonFilterSpecification<MissingProduct> missingProductCommonFilterSpecification;

    private final SflUserService sflUserService;

    public MissingProductService(CommonFilterSpecification<MissingProduct> missingProductCommonFilterSpecification, SflUserService sflUserService) {
        this.missingProductCommonFilterSpecification = missingProductCommonFilterSpecification;
        this.sflUserService = sflUserService;
    }

    public Page<MissingProductDTO> getAllMissingProducts(Pageable pageable) {
        return repository.findAll(pageable)
            .map(mapper::toDto);
    }

    public MissingProductDTO getMissingProductById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElse(null);
    }

    public MissingProductDTO createMissingProduct(MissingProductDTO dto) {
        Optional<SflUser> sflUser = sflUserService.getUserWithAuthorities();
        sflUser.ifPresent(user -> dto.setUserId(user.getId()));
        MissingProduct missingProduct = mapper.toEntity(dto);
        missingProduct = repository.save(missingProduct);
        return mapper.toDto(missingProduct);
    }

    public MissingProductDTO updateMissingProduct(Long id, MissingProductDTO dto) {
        if (!repository.existsById(id)) {
            return null;
        }
        MissingProduct missingProduct = mapper.toEntity(dto);
        missingProduct.setId(id);
        missingProduct = repository.save(missingProduct);
        return mapper.toDto(missingProduct);
    }

    public void deleteMissingProduct(Long id) {
        repository.deleteById(id);
    }

    public Page<MissingProductDTO> getFilteredMissingProduct(FilterRequestDTO filterRequestDTO) {
        log.info("Execute filter search service");
        Pageable pageable = PageRequest.of(filterRequestDTO.getPageNumber(), filterRequestDTO.getPageSize());
        Specification<MissingProduct> specification = missingProductCommonFilterSpecification.applyFilters(filterRequestDTO);
        log.info("Prepared specifications for filter");
        Page<MissingProduct> missingProducts = repository.findAll(specification, pageable);
        log.info("specifications are executed.");
        return missingProducts.map(mapper::toDto);
    }

    public long getTotalMissingProductCount(){
        return repository.count();
    }
}

