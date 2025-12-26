package com.sfl.core.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DatabaseVersionResponseDTO {
    private boolean shouldUpdate;
    private int latestVersion;
    private String signedUrl;
    @JsonProperty("isMaster")
    private boolean isMaster;
}
