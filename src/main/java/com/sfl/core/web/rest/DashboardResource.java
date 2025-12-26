package com.sfl.core.web.rest;


import com.sfl.core.DashboardService;
import com.sfl.core.service.dto.CountPerOSDTO;
import com.sfl.core.service.dto.DashboardCountStatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardResource {

    private final DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/status-count")
    public ResponseEntity<DashboardCountStatusDTO> getAllMissingProducts() {
        return ResponseEntity.ok(dashboardService.getDashboardCountStatus());
    }

    @GetMapping("/user-count-by-os")
    public ResponseEntity<List<CountPerOSDTO>> getOSWiseDemographic() {
        return ResponseEntity.ok(dashboardService.getOSWiseDemographic());
    }

    @GetMapping("/user-installation-count-by-month")
    public ResponseEntity<List<Object[]>> getUserCountByMonth() {
        return ResponseEntity.ok(dashboardService.getUserCountByMonth());
    }
}
