package com.sfl.core.web.rest;

import com.sfl.core.service.dto.DatabaseVersionRequestDTO;
import com.sfl.core.service.dto.DatabaseVersionResponseDTO;
import com.sfl.core.service.impl.DatabaseUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/database")
@RequiredArgsConstructor
@Slf4j
public class DatabaseUpdateController {

    private final DatabaseUpdateService databaseUpdateService;

    /**
     * Endpoint to check for database updates.
     * Accepts a DatabaseVersionRequestDTO containing the current version of the database.
     * Returns a DatabaseVersionResponseDTO indicating whether an update is needed and providing the signed URL if applicable.
     *
     * @param request DatabaseVersionRequestDTO
     * @return ResponseEntity containing DatabaseVersionResponseDTO
     */
    @PostMapping("/check-update")
    public ResponseEntity<DatabaseVersionResponseDTO> checkDatabaseUpdate(
        @RequestBody DatabaseVersionRequestDTO request) {
        log.info("Received request to check database update: {}", request);
        DatabaseVersionResponseDTO response = databaseUpdateService.checkDatabaseVersion(request);
        return ResponseEntity.ok(response);
    }
}
