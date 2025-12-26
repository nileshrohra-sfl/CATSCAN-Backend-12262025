package com.sfl.core.web.rest;

import com.sfl.core.service.CatScanProductService;
import com.sfl.core.service.dto.CatScanProductDTO;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.MissingProductDTO;
import io.github.jhipster.web.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catScan-product")
public class CatScanProductResource {

    private static final String ENTITY_NAME = "cat_scan_product";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatScanProductService catScanProductService;

    public CatScanProductResource(CatScanProductService catScanProductService) {
        this.catScanProductService = catScanProductService;
    }

    @GetMapping
    public ResponseEntity<Page<CatScanProductDTO>> getAllProducts(@RequestParam(value = "pageNumber", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
                                                                         @RequestParam(value = "pageSize", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(catScanProductService.getAllProducts(pageable));
    }

    @PostMapping
    public ResponseEntity<CatScanProductDTO> persist(@RequestBody CatScanProductDTO catScanProductDTO) {
        return ResponseEntity.ok(catScanProductService.persist(catScanProductDTO));
    }

    @PostMapping("/get-filter-data")
    public ResponseEntity<Page<CatScanProductDTO>> getAllFilterProducts(@RequestBody FilterRequestDTO dto) {
        return ResponseEntity.ok(catScanProductService.getFilteredProduct(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Page<CatScanProductDTO>> getAllFilterProducts(@PathVariable Long id) {
        catScanProductService.deleteProduct(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
