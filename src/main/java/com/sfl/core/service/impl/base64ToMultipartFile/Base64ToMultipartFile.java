package com.sfl.core.service.impl.base64ToMultipartFile;

import java.util.Base64;

import com.sfl.lib.core.aws.s3.service.S3ObjectService;
import com.sfl.lib.core.aws.s3.service.dto.UrlResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Base64ToMultipartFile {
    private final S3ObjectService s3ObjectService;

    public Base64ToMultipartFile(S3ObjectService s3ObjectService) {
        this.s3ObjectService = s3ObjectService;
    }

    public static MultipartFile convert(String base64String) {
        // Example base64 string header: "data:image/png;base64,"
        String[] parts = base64String.split(",");
        String header = parts[0];
        byte[] content = Base64.getDecoder().decode(parts[1]);

        return new Base64DecodedMultipartFile(content, header);
    }

    public UrlResponseDTO base64ToMultipartFile(String type, String base64String) {
        MultipartFile multipartFile = convert(base64String);
        UrlResponseDTO urlResponseDTO = new UrlResponseDTO();
        urlResponseDTO.setUrl(this.s3ObjectService.uploadMultipartFile(1L, multipartFile, type));
        return urlResponseDTO;
    }
}
