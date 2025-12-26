package com.sfl.core.web.rest;

import com.sfl.core.service.ErrorLogService;
import com.sfl.core.service.dto.ErrorLogDTO;
import com.sfl.core.service.dto.FilterRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/error")
public class ErrorLogResource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ErrorLogService errorLogService;

    public ErrorLogResource(ErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    @GetMapping
    public ResponseEntity<Page<ErrorLogDTO>> getAllError(@RequestParam(value = "pageNumber", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
                                                         @RequestParam(value = "pageSize", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(errorLogService.getErrorLog(pageable));
    }

    @PostMapping("/get-filter-data")
    public ResponseEntity<Page<ErrorLogDTO>> getFilteredErrorLogs(@RequestBody FilterRequestDTO dto) {
        log.info("Execute filter search");
        return ResponseEntity.ok(errorLogService.filterErrorLog(dto));
    }

    @PostMapping
    public ResponseEntity<ErrorLogDTO> createMissingProduct(@RequestBody ErrorLogDTO dto) {
        return ResponseEntity.ok(errorLogService.persistError(dto));
    }

}
