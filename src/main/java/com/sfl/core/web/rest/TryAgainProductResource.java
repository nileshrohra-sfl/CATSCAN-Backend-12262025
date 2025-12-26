package com.sfl.core.web.rest;

import com.sfl.core.service.TryAgainProductService;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.RequestReportDTO;
import com.sfl.core.service.dto.TryAgainProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/api")
public class TryAgainProductResource {

    @Autowired
    private TryAgainProductService tryAgainProductService;

    private final Logger log = LoggerFactory.getLogger(TryAgainProductService.class);


    @GetMapping("/try-again-products")
    public Page<TryAgainProductDTO> getAllTryAgainProducts(@RequestParam(value = "pageNumber", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
                                                           @RequestParam(value = "pageSize", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return tryAgainProductService.findAll(pageable);
    }

    @GetMapping("/try-again-products/{id}")
    public TryAgainProductDTO getTryAgainProductById(@PathVariable Long id) {
        return tryAgainProductService.findById(id);
    }

    @PostMapping("/try-again-products")
    public List<TryAgainProductDTO> createTryAgainProduct(@RequestBody List<TryAgainProductDTO> tryAgainProductDTO) {
        return tryAgainProductService.save(tryAgainProductDTO);
    }

    @PutMapping("/try-again-products/{id}")
    public ResponseEntity<TryAgainProductDTO> updateTryAgainProduct(@PathVariable Long id, @RequestBody TryAgainProductDTO tryAgainProductDTO) {
        return ResponseEntity.ok(tryAgainProductService.update(id, tryAgainProductDTO));
    }

    @DeleteMapping("/try-again-products/{id}")
    public ResponseEntity<Void> deleteTryAgainProduct(@PathVariable Long id) {
        tryAgainProductService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/try-again-products/get-filter-data")
    public ResponseEntity<Page<TryAgainProductDTO>> getFilteredTheProduct(@RequestBody FilterRequestDTO dto) {
        log.info("Trying filter out try-again products.");
        return ResponseEntity.ok(tryAgainProductService.getFilteredTheProduct(dto));
    }

    /**
     * It is export try-again products.
     * @param requestDto
     * @param response
     */
    @PostMapping("/try-again-products/export")
    public void exportGenericData(@RequestBody RequestReportDTO requestDto, HttpServletResponse response) {
        try {
            log.info("Export Data for ###Request : {}", requestDto);
            tryAgainProductService.exportTryAgainProducts(requestDto, response);
        } catch (Exception ex) {

        }
    }
}
