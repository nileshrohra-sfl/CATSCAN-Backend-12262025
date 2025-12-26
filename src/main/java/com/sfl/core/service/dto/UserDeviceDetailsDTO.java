package com.sfl.core.service.dto;

public class UserDeviceDetailsDTO {

    private Long id;

    private Long userId;

    private String os;

    private String deviceName;

    private String deviceType;

    private String diskSpaceTotal;

    private String diskSpaceUsed;

    private String diskSpaceFree;

    private String ramMemTotal;

    private String appMem;

    private String ramMemFree;

    private String appDiskSpaceUsed;

    private String uniqueId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDiskSpaceTotal() {
        return diskSpaceTotal;
    }

    public void setDiskSpaceTotal(String diskSpaceTotal) {
        this.diskSpaceTotal = diskSpaceTotal;
    }

    public String getDiskSpaceUsed() {
        return diskSpaceUsed;
    }

    public void setDiskSpaceUsed(String diskSpaceUsed) {
        this.diskSpaceUsed = diskSpaceUsed;
    }

    public String getDiskSpaceFree() {
        return diskSpaceFree;
    }

    public void setDiskSpaceFree(String diskSpaceFree) {
        this.diskSpaceFree = diskSpaceFree;
    }

    public String getRamMemTotal() {
        return ramMemTotal;
    }

    public void setRamMemTotal(String ramMemTotal) {
        this.ramMemTotal = ramMemTotal;
    }

    public String getAppMem() {
        return appMem;
    }

    public void setAppMem(String appMem) {
        this.appMem = appMem;
    }

    public String getRamMemFree() {
        return ramMemFree;
    }

    public void setRamMemFree(String ramMemFree) {
        this.ramMemFree = ramMemFree;
    }

    public String getAppDiskSpaceUsed() {
        return appDiskSpaceUsed;
    }

    public void setAppDiskSpaceUsed(String appDiskSpaceUsed) {
        this.appDiskSpaceUsed = appDiskSpaceUsed;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString() {
        return "UserDeviceDetailsDTO{" +
            "id=" + id +
            ", userId=" + userId +
            ", os='" + os + '\'' +
            ", deviceName='" + deviceName + '\'' +
            ", deviceType='" + deviceType + '\'' +
            ", diskSpaceTotal='" + diskSpaceTotal + '\'' +
            ", diskSpaceUsed='" + diskSpaceUsed + '\'' +
            ", diskSpaceFree='" + diskSpaceFree + '\'' +
            ", ramMemTotal='" + ramMemTotal + '\'' +
            ", appMem='" + appMem + '\'' +
            ", ramMemFree='" + ramMemFree + '\'' +
            ", appDiskSpaceUsed='" + appDiskSpaceUsed + '\'' +
            ", uniqueId='" + uniqueId + '\'' +
            '}';
    }
}
