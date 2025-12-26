package com.sfl.core.service.dto;

import com.sfl.lib.core.aws.s3.service.dto.UrlResponseDTO;

public class FileUrlResponseDTO extends UrlResponseDTO {

    private String fullUrl;

    public FileUrlResponseDTO() {
    }

    public FileUrlResponseDTO(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }
}
