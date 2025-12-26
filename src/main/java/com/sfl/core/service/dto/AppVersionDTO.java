package com.sfl.core.service.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class AppVersionDTO {

    private Long id;

    private String iosBuildNumber;

    private String androidBuildNumber;

    private String iosVersion;

    private String androidVersion;

    private boolean iosForceUpdate;

    private boolean androidForceUpdate;

    private String playStoreURL;

    private String appStoreURL;

    private String createdBy;

    private String createdDate;

    private String lastModifiedBy;

    private String lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIosBuildNumber() {
        return iosBuildNumber;
    }

    public void setIosBuildNumber(String iosBuildNumber) {
        this.iosBuildNumber = iosBuildNumber;
    }

    public String getAndroidBuildNumber() {
        return androidBuildNumber;
    }

    public void setAndroidBuildNumber(String androidBuildNumber) {
        this.androidBuildNumber = androidBuildNumber;
    }

    public String getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(String iosVersion) {
        this.iosVersion = iosVersion;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public boolean isIosForceUpdate() {
        return iosForceUpdate;
    }

    public void setIosForceUpdate(boolean iosForceUpdate) {
        this.iosForceUpdate = iosForceUpdate;
    }

    public boolean isAndroidForceUpdate() {
        return androidForceUpdate;
    }

    public void setAndroidForceUpdate(boolean androidForceUpdate) {
        this.androidForceUpdate = androidForceUpdate;
    }

    public String getPlayStoreURL() {
        return playStoreURL;
    }

    public void setPlayStoreURL(String playStoreURL) {
        this.playStoreURL = playStoreURL;
    }

    public String getAppStoreURL() {
        return appStoreURL;
    }

    public void setAppStoreURL(String appStoreURL) {
        this.appStoreURL = appStoreURL;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
