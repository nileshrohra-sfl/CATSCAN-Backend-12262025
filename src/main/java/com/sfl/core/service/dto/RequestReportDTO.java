package com.sfl.core.service.dto;

import com.sfl.core.service.enums.ExportFileType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestReportDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private final Map<String, Object> filter = new HashMap<>();

    private String tableName;

    private String sortBy;

    private String sortOrder;

    private List<String> fields = new ArrayList<>();

    private List<String> headerFields = new ArrayList<>();

    private ExportFileType exportFileType;

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public ExportFileType getExportFileType() {
        return exportFileType;
    }

    public List<String> getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(List<String> headerFields) {
        this.headerFields = headerFields;
    }

    public void setExportFileType(ExportFileType exportFileType) {
        this.exportFileType = exportFileType;
    }

    @Override
    public String toString() {
        return "RequestReportDTO{" +
            "startDate=" + startDate +
            ", endDate=" + endDate +
            ", filter=" + filter +
            ", tableName='" + tableName + '\'' +
            ", sortBy='" + sortBy + '\'' +
            ", sortOrder='" + sortOrder + '\'' +
            ", fields=" + fields +
            ", headerFields=" + headerFields +
            ", exportFileType=" + exportFileType +
            '}';
    }
}
