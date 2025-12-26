package com.sfl.core.web.rest;

import com.sfl.core.service.MissingProductService;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.MissingProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missing-products")
public class MissingProductResource {

    private final Logger log = LoggerFactory.getLogger(MissingProductResource.class);

    private final MissingProductService service;

    public MissingProductResource(MissingProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<MissingProductDTO>> getAllMissingProducts(@RequestParam(value = "pageNumber", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
                                                                         @RequestParam(value = "pageSize", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(service.getAllMissingProducts(pageable));
    }

    @PostMapping("/get-filter-data")
    public ResponseEntity<Page<MissingProductDTO>> getFilteredMissingProduct(@RequestBody FilterRequestDTO dto) {
        log.info("Execute filter search");
        return ResponseEntity.ok(service.getFilteredMissingProduct(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissingProductDTO> getMissingProductById(@PathVariable Long id) {
        MissingProductDTO dto = service.getMissingProductById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<MissingProductDTO> createMissingProduct(@RequestBody MissingProductDTO dto,
                                                                  @RequestHeader(value = com.sfl.core.config.Constants.PLATFORM_TYPE) String platform) {
        return ResponseEntity.ok(service.createMissingProduct(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MissingProductDTO> updateMissingProduct(@PathVariable Long id, @RequestBody MissingProductDTO dto) {
        MissingProductDTO updatedDto = service.updateMissingProduct(id, dto);
        return updatedDto != null ? ResponseEntity.ok(updatedDto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMissingProduct(@PathVariable Long id) {
        service.deleteMissingProduct(id);
        return ResponseEntity.noContent().build();
    }
}
