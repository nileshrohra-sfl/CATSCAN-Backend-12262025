package com.sfl.core.web.rest;

import com.sfl.core.service.dto.FileUrlResponseDTO;
import com.sfl.core.service.mapper.CommonAfterMapper;
import com.sfl.lib.core.aws.s3.service.S3ObjectService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


/***
 * @author Henil Mistry
 *
 * Overrride Utility FileAPI's
 */
@RestController
@RequestMapping("/api")
public class CustomS3StorageAPI {

    private final Logger log = LoggerFactory.getLogger(CustomS3StorageAPI.class);

    private final S3ObjectService s3ObjectService;

    private final CommonAfterMapper commonAfterMapper;

    public CustomS3StorageAPI(S3ObjectService s3ObjectService, CommonAfterMapper commonAfterMapper) {
        this.s3ObjectService = s3ObjectService;
        this.commonAfterMapper = commonAfterMapper;
    }

    /**
     * Upload files for application users
     *
     * @param userId
     * @param multipartFiles
     * @param type
     * @return
     */
    @PostMapping({"/storage/files/{userId}"})
    public ResponseEntity<Map<String,List<FileUrlResponseDTO>>> filesUpload(@PathVariable Long userId, @RequestParam MultipartFile[] multipartFiles, @RequestParam String type) {
        this.log.info("Request to upload files for user id {} ", userId);
        List<FileUrlResponseDTO> objectKeys = new ArrayList<>();
        Map<String,  List<FileUrlResponseDTO>> response = new HashMap<>();
        Arrays.asList(multipartFiles).forEach((multipartFile) -> {
            FileUrlResponseDTO urlResponseDTO = new FileUrlResponseDTO();
            String uploadPath = this.s3ObjectService.uploadMultipartFileSync(userId, multipartFile, type);
            urlResponseDTO.setFullUrl(commonAfterMapper.getFullUrl(uploadPath));
            urlResponseDTO.setUrl(uploadPath);
            objectKeys.add(urlResponseDTO);
        });
        response.put("responses", objectKeys);
        this.log.info("Files uploaded successfully: {}", objectKeys);
        return ResponseUtil.wrapOrNotFound(Optional.of(response));
    }
}
