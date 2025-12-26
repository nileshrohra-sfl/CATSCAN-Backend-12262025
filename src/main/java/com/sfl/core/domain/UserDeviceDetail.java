package com.sfl.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "user_device_details")
public class UserDeviceDetail extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("user")
    @JoinColumn(name = "user_id")
    private SflUserExtend user;
    @Column(name = "operating_system")
    private String os;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "disk_space_total")
    private String diskSpaceTotal;

    @Column(name = "disk_space_used")
    private String diskSpaceUsed;

    @Column(name= "disk_space_free")
    private String diskSpaceFree;

    @Column(name = "ram_mem_total")
    private String ramMemTotal;

    @Column(name = "app_mem")
    private String appMem;

    @Column(name = "ram_mem_free")
    private String ramMemFree;

    @Column(name = "app_disk_space_used")
    private String appDiskSpaceUsed;

    @Column(name = "unique_id")
    private String uniqueId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SflUserExtend getUser() {
        return user;
    }

    public void setUser(SflUserExtend user) {
        this.user = user;
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
}
