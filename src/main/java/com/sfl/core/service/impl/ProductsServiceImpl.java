package com.sfl.core.service.impl;

import com.sfl.core.domain.Products;
import com.sfl.core.repository.ProductsRepository;
import com.sfl.core.service.ProductsService;
import com.sfl.core.service.dto.KeywordDTO;
import com.sfl.core.service.dto.ProductDTO;
import com.sfl.core.service.dto.SearchRequestDTO;
import com.sfl.core.service.dto.SearchResponseDTO;
import com.sfl.core.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * A Service Implementation for {@link com.sfl.core.domain.Products}
 */
@Service
public class ProductsServiceImpl implements ProductsService {

    private final Logger log = LoggerFactory.getLogger(ProductsServiceImpl.class);

    private final ProductsRepository productsRepository;

    private final ProductMapper productMapper;

    public ProductsServiceImpl(ProductsRepository productsRepository, ProductMapper productMapper) {
        this.productsRepository = productsRepository;
        this.productMapper = productMapper;
    }

    @Override
    public SearchResponseDTO getSearchData(SearchRequestDTO searchRequestDTO) {
        log.debug("Request to get search data for : {} ", searchRequestDTO);
        SearchResponseDTO searchResponseDTO = new SearchResponseDTO();
        if (Objects.isNull(searchRequestDTO)) {
            searchResponseDTO.setRespDataList(getListOfProductDTO(productsRepository.findAll(), ""));
            return searchResponseDTO;
        }
        List<KeywordDTO> keywordList = searchRequestDTO.getKeywordList();
        if (CollectionUtils.isEmpty(keywordList)) {
            searchResponseDTO.setRespDataList(getListOfProductDTO(productsRepository.findAll(), ""));
            return searchResponseDTO;
        }
        List<ProductDTO> respDataList = searchResponseDTO.getRespDataList();
        keywordList.forEach(keywordDTO -> {
            String keyword = keywordDTO.getKeyword();
            respDataList.addAll(getListOfProductDTO(productsRepository.findBySearchKeyWord(keyword), keyword));
        });
        return searchResponseDTO;
    }

    /**
     * Get list of productDTO from product and keyword
     *
     * @param products the products
     * @param keyword  the keyword
     * @return the list of {@link ProductDTO}
     */
    private List<ProductDTO> getListOfProductDTO(List<Products> products, String keyword) {
        log.debug("Get list of productDTO from product : {} and keyword : {} ", products, keyword);
        return products.stream().map(product -> {
            ProductDTO productDTO = productMapper.toDto(product);
            productDTO.setKeyword(keyword);
            return productDTO;
        }).toList();
    }
}
