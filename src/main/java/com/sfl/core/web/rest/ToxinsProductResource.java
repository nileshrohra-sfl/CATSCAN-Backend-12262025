package com.sfl.core.web.rest;

import com.sfl.core.service.ToxinsProductService;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.ToxinsProductDTO;
import io.github.jhipster.web.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/toxins-product")
public class ToxinsProductResource {

    private static final String ENTITY_NAME = "toxins_product";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToxinsProductService toxinsProductService;

    public ToxinsProductResource(ToxinsProductService toxinsProductService) {
        this.toxinsProductService = toxinsProductService;
    }


    @GetMapping
    public ResponseEntity<Page<ToxinsProductDTO>> getAllProducts(@RequestParam(value = "pageNumber", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(toxinsProductService.getAllProducts(pageable));
    }

    @PostMapping
    public ResponseEntity<ToxinsProductDTO> persist(@RequestBody ToxinsProductDTO catScanProductDTO) {
        return ResponseEntity.ok(toxinsProductService.persist(catScanProductDTO));
    }

    @PostMapping("/get-filter-data")
    public ResponseEntity<Page<ToxinsProductDTO>> getAllFilterProducts(@RequestBody FilterRequestDTO dto) {
        return ResponseEntity.ok(toxinsProductService.getFilteredProduct(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Page<ToxinsProductDTO>> getAllFilterProducts(@PathVariable Long id) {
        toxinsProductService.deleteProduct(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
