package com.sfl.core.service;

import com.sfl.core.domain.SflUser;
import com.sfl.core.domain.TryAgainProduct;
import com.sfl.core.repository.TryAgainProductRepository;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.RequestReportDTO;
import com.sfl.core.service.dto.TryAgainProductDTO;
import com.sfl.core.service.dto.TryAgainProductResultJsonData;
import com.sfl.core.service.helper.CommonFilterSpecification;
import com.sfl.core.service.impl.base64ToMultipartFile.Base64ToMultipartFile;
import com.sfl.core.service.mapper.TryAgainProductMapper;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Service
public class TryAgainProductService {

    private final TryAgainProductRepository tryAgainProductRepository;
    private final TryAgainProductMapper tryAgainProductMapper;
    private final Base64ToMultipartFile base64ToMultipartFile;
    private final CommonFilterSpecification<TryAgainProduct> tryAgainProductCommonFilterSpecification;
    private final ExportData exportData;
    private final SflUserService sflUserService;
    private static final String CHOICE_PREFIX = "choice_";
    private static final String PROD_NAME = "_prod_name";
    private static final String TOKENS = "_tokens";
    private static final String MATCHING_TOKENS = "_tokens_matched";
    private static final String DISTANCE_MATCHING_TOKENS = "_tokens_levdist";
    private static final String SCORE = "_score";
    private final Logger log = LoggerFactory.getLogger(TryAgainProductService.class);


    public TryAgainProductService(TryAgainProductRepository tryAgainProductRepository, TryAgainProductMapper tryAgainProductMapper, Base64ToMultipartFile base64ToMultipartFile, CommonFilterSpecification<TryAgainProduct> tryAgainProductCommonFilterSpecification, ExportData exportData, SflUserService sflUserService) {
        this.tryAgainProductRepository = tryAgainProductRepository;
        this.tryAgainProductMapper = tryAgainProductMapper;
        this.base64ToMultipartFile = base64ToMultipartFile;
        this.tryAgainProductCommonFilterSpecification = tryAgainProductCommonFilterSpecification;
        this.exportData = exportData;
        this.sflUserService = sflUserService;
    }

    public Page<TryAgainProductDTO> findAll(Pageable pageable) {
        return tryAgainProductRepository.findAll(pageable).map(tryAgainProductMapper::toDto);
    }

    public TryAgainProductDTO findById(Long id) {
        TryAgainProduct tryAgainProduct = tryAgainProductRepository.findById(id).orElseThrow(() -> new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE,
            Constants.USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND));
        return tryAgainProductMapper.toDto(tryAgainProduct);
    }

    public List<TryAgainProductDTO> save(List<TryAgainProductDTO> tryAgainProductDTOs) {
        List<TryAgainProduct> tryAgainProducts = new ArrayList<>();
        Optional<SflUser> userOptional = sflUserService.getCurrentLoggedInUser();
        for (TryAgainProductDTO tryAgainProductDTO : tryAgainProductDTOs) {
            TryAgainProduct tryAgainProduct = tryAgainProductMapper.toEntity(tryAgainProductDTO);
            userOptional.ifPresent(tryAgainProduct::setUser);
            tryAgainProducts.add(tryAgainProduct);
        }
        tryAgainProductRepository.saveAll(tryAgainProducts);
        return tryAgainProductDTOs;
    }

    public TryAgainProductDTO update(Long id, TryAgainProductDTO tryAgainProductDTO) {
        TryAgainProduct tryAgainProduct = tryAgainProductRepository.findById(id).orElseThrow(() -> new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE,
            Constants.USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND));

        TryAgainProduct updatedTryAgainProduct = tryAgainProduct;
        updatedTryAgainProduct.setImage(tryAgainProductDTO.getImage());
        updatedTryAgainProduct.setOriginalOcrResult(tryAgainProductDTO.getOriginalOcrResult());
//        Set<TryAgainProductResult> set = new HashSet<TryAgainProductResult>(tryAgainProductDTO.getResult().);
//        updatedTryAgainProduct.setResult(set);
        return tryAgainProductMapper.toDto(tryAgainProductRepository.save(updatedTryAgainProduct));
    }

    public void deleteById(Long id) {
        tryAgainProductRepository.deleteById(id);
    }

    public Page<TryAgainProductDTO> getFilteredTheProduct(FilterRequestDTO filterRequestDTO) {
        log.info("Execute filter search service for try-again product");
        Pageable pageable = PageRequest.of(filterRequestDTO.getPageNumber(), filterRequestDTO.getPageSize());
        Specification<TryAgainProduct> specification = tryAgainProductCommonFilterSpecification.applyFilters(filterRequestDTO);
        log.info("Prepared specifications for try-again product filter");
        Page<TryAgainProduct> tryAgainProducts = tryAgainProductRepository.findAll(specification, pageable);
        return tryAgainProducts.map(product -> {
            TryAgainProductDTO dto = tryAgainProductMapper.toDto(product);
            dto.setDynamicOcrResult(dynamicOcrResult(product.getOcrResult()));
            return dto;
        });
    }
    /**
     * This method is used to convert the ocr result to map.
     */
    private Map<String, String> dynamicOcrResult(List<TryAgainProductResultJsonData> ocrResult) {
        log.info("Dynamic ocr result for try-again product");
        Map<String, String> dynamicOcrMap = new LinkedHashMap<>();
        // Added fieldCounter for FE cannot handle the dynamic field name in ordering.
        int fieldCounter = 1; // Global field counter
        if (ocrResult != null) {
            for (int i = 0; i < ocrResult.size(); i++) {
                TryAgainProductResultJsonData r = ocrResult.get(i);
                dynamicOcrMap.put(CHOICE_PREFIX + (i + 1) + PROD_NAME + "_" + fieldCounter++, safeStr(r.getProdName()));
                dynamicOcrMap.put(CHOICE_PREFIX + (i + 1) + TOKENS + "_" + fieldCounter++, safeStr(r.getOcrToken()));
                dynamicOcrMap.put(CHOICE_PREFIX + (i + 1) + MATCHING_TOKENS + "_" + fieldCounter++, safeStr(r.getExactMatches()));
                dynamicOcrMap.put(CHOICE_PREFIX + (i + 1) + DISTANCE_MATCHING_TOKENS + "_" + fieldCounter++, safeStr(r.getDistanceMatches()));
                dynamicOcrMap.put(CHOICE_PREFIX + (i + 1) + SCORE + "_" + fieldCounter++, safeStr(r.getMatchScores()));
            }
        }
        return dynamicOcrMap;
    }
    /**
     * This method is used to convert object to string.
     */
    private String safeStr(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    public long getTryAgainProductCount(){
        return tryAgainProductRepository.count();
    }

    public void exportTryAgainProducts(RequestReportDTO requestDto, HttpServletResponse response) {
        log.info("Export service for ###Request : {}", requestDto);
        exportData.exportTryAgainProductData(requestDto,response);
    }
}
