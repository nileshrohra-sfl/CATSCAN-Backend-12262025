package com.sfl.core.service.mapper;

import com.sfl.lib.core.aws.s3.service.S3ObjectService;
import com.sfl.lib.core.aws.s3.service.dto.UrlRequestDTO;
import com.sfl.lib.core.aws.s3.service.dto.UrlResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public class CommonAfterMapper {

    @Autowired
    private S3ObjectService s3ObjectService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${cloud.aws.s3.product.bucket.public-bucket}")
    String publicBucketUrl;

    @Named("fullUrl")
    public String getFullUrl(String url) {
        if (StringUtils.isNotBlank(url)) {
            try {
                UrlRequestDTO urlRequestDTO = new UrlRequestDTO(url).preferSignedUrl(true);
                UrlResponseDTO urlResponseDTO = s3ObjectService.getPreSignedUrl(urlRequestDTO);
                return urlResponseDTO.getUrl();
            }catch (Exception ex){
                log.error("File not found or fail to fetch from S3 path: {}", url);
            }
        }
        return null;
    }

    @Named("fullUrls")
    public List<String> getFullUrls(List<String> url) {
        if (Objects.nonNull(url)) {
            return url.stream().map(this::getFullUrl).toList();
        }
        return null;
    }

    /**
     * get the public bucket URL for the sponsor logo
     */
    @Named("publicUrl")
    public String getPublicUrl(String url) {
        if (StringUtils.isNotBlank(url)) {
            return publicBucketUrl + url;
        }
        return null;
    }
}

