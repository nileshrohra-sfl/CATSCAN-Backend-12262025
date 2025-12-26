package com.sfl.core.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.sfl.core.service.dto.DatabaseVersionRequestDTO;
import com.sfl.core.service.dto.DatabaseVersionResponseDTO;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import com.sfl.lib.core.aws.s3.service.dto.UrlRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DatabaseUpdateService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.database.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.s3.database.bucket.url}")
    private String bucketUrl;

    @Value("${cloud.aws.s3.database.bucket.master-path}")
    private String masterPath;

    @Value("${cloud.aws.s3.database.bucket.update-path}")
    private String updatePath;

    @Value("${cloud.aws.s3.database.bucket.expiry-time-in-minutes}")
    private int expiryTimeInMinutes;

    @Value("${cloud.aws.s3.database.bucket.prefer-signed-url}")
    private boolean preferSignedUrl;

    @Value("${cloud.aws.s3.database.bucket.extensions}")
    private String extensions;

    /**
     * Check if there is a database update available for the client.
     * If currentVersion is 0, always return the master database.
     * If there is an update available, return the signed URL for the next version.
     * If no update is available, indicate that no update is needed.
     *
     * @param request DatabaseVersionRequestDTO containing the current version of the database
     * @return DatabaseVersionResponseDTO indicating whether an update is needed and providing the signed URL if applicable
     */
    public DatabaseVersionResponseDTO checkDatabaseVersion(DatabaseVersionRequestDTO request) {
        int currentVersion = request.getCurrentVersion();
        log.info("Checking DB update for current version: {}", currentVersion);
        if (currentVersion < 0) {
            log.info("Current version is less than 0, returning master database.");
            throw new GlobalException(Constants.DATABASE_VERSION_INVALID_EXCEPTION_MESSAGE, Constants.DATABASE_VERSION_INVALID_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
        }
        String key;
        int latestVersion;
        boolean shouldUpdate = false;
        String signedUrl = null;
        boolean isMaster = false;

        if (currentVersion == 0) {
            key = masterPath;
            isMaster = true;
            latestVersion = currentVersion + 1;
            log.info("Current version is 0, checking for master database...");
        } else {
            latestVersion = currentVersion + 1;
            key = updatePath + "/" + latestVersion + extensions;
            log.info("Checking for update version: {}", latestVersion);
        }

        // Concat / as got doesObjectExist needs key without /
        // Don't add this in YAML as might lead to issues in other places
        String s3Key = key;

        if (doesObjectExist(bucketName, key)) {
            log.info("Database found in S3 at key: {}", key);
            signedUrl = getSignedUrl(s3Key);
            shouldUpdate = true;
        } else {
            log.warn("Database not found in S3 at key: {}", key);
            // Revert to current version if update not found
            latestVersion = currentVersion;
        }

        return DatabaseVersionResponseDTO.builder()
            .shouldUpdate(shouldUpdate)
            .latestVersion(latestVersion)
            .signedUrl(signedUrl)
            .isMaster(isMaster)
            .build();
    }

    /**
     * Generate a signed URL for the given S3 key.
     *
     * @param key The S3 key for which to generate the signed URL
     * @return The signed URL, or null if generation fails
     */
    private String getSignedUrl(String key) {
        log.info("Generating signed URL for S3 key: {}", key);
        try {
            log.info("Using bucket: {}", bucketName);
            UrlRequestDTO urlRequestDTO = new UrlRequestDTO(key)
                .preferSignedUrl(preferSignedUrl)
                .urlExpiryTimeInMinutes(expiryTimeInMinutes);
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += (long) ('\uea60' * urlRequestDTO.getUrlExpiryTimeInMinutes());
            expiration.setTime(expTimeMillis);
            GeneratePresignedUrlRequest generatePresignedUrlRequest = (
                new GeneratePresignedUrlRequest(bucketName, urlRequestDTO.getObjectKey()))
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
            return this.amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (Exception ex) {
            log.error("Failed to generate signed URL for S3 key: {}", key, ex);
            return null;
        }
    }

    /**
     * Check if an object exists in the specified S3 bucket.
     *
     * @param bucketName The name of the S3 bucket
     * @param key        The key of the object to check
     * @return true if the object exists, false otherwise
     */
    public boolean doesObjectExist(String bucketName, String key) {
        log.info("Checking if object exists in S3: {} / {}", bucketName, key);
        try {
            log.info("Using bucket: {}", bucketName);
            return amazonS3.doesObjectExist(bucketName, key);
        } catch (AmazonS3Exception e) {
            log.error("Error checking if object exists in S3: {} / {}", bucketName, key, e);
            return false;
        }
    }
}
