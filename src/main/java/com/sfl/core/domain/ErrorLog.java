package com.sfl.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "error_log")
public class ErrorLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "device_id")
    private String deviceId;
    @Column(name = "device_type")
    private String deviceType;
    @Column(name = "device_os_version")
    private String deviceOsVersion;
    @Column(name = "app_version")
    private String appVersion;
    @Column(name = "flavor")
    private String flavor;
    @Column(name = "error")
    private String error;
    @Column(name = "status_code")
    private String statusCode;
    @Column(name = "api_request")
    private String apiRequest;
    @Column(name = "api_response")
    private String apiResponse;
    @Column(name = "device_name")
    private String deviceName;
    @Column(name = "unique_device_id")
    private String uniqueDeviceId;
    @ManyToOne
    @JsonIgnoreProperties("user")
    @JoinColumn(name = "user_id")
    private SflUser user;
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceOsVersion() {
        return deviceOsVersion;
    }

    public void setDeviceOsVersion(String deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getApiRequest() {
        return apiRequest;
    }

    public void setApiRequest(String apiRequest) {
        this.apiRequest = apiRequest;
    }

    public String getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(String apiResponse) {
        this.apiResponse = apiResponse;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getUniqueDeviceId() {
        return uniqueDeviceId;
    }

    public void setUniqueDeviceId(String uniqueDeviceId) {
        this.uniqueDeviceId = uniqueDeviceId;
    }

    public SflUser getUser() {
        return user;
    }

    public void setUser(SflUser user) {
        this.user = user;
    }
}
