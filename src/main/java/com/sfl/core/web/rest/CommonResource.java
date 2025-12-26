package com.sfl.core.web.rest;

import com.sfl.core.service.CommonService;
import com.sfl.core.service.dto.AppVersionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app")
public class CommonResource {

    private final CommonService commonService;

    public CommonResource(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/latest-version")
    public ResponseEntity<AppVersionDTO> getCurrentVersion(){
        return ResponseEntity.ok(commonService.getCurrentAppVersion());
    }

        @PostMapping("/update-version")
    public ResponseEntity<AppVersionDTO> persist(@RequestBody AppVersionDTO appVersionDTO) {
        return ResponseEntity.ok(commonService.saveOrUpdate(appVersionDTO));
    }
}
