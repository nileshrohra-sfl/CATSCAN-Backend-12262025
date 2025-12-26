package com.sfl.core.web.rest;

import com.sfl.core.service.ExportData;
import com.sfl.core.service.dto.RequestReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * This Resource help to export data
 */
@RestController
@RequestMapping("/api")
public class ExportDataResource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ExportData exportData;

    /**
     * It is a generic export function to export all types of simple data.
     * @param requestDto
     * @param response
     */
    @PostMapping("/export")
    public void exportGenericData(@RequestBody RequestReportDTO requestDto, HttpServletResponse response) {
        try {
            log.info("Export Data for ###Request : {}", requestDto.toString());
            exportData.exportGenericData(requestDto, response);
        } catch (Exception ex) {

        }
    }
}
