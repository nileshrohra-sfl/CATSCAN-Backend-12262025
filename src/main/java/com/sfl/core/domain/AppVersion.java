package com.sfl.core.domain;

import javax.persistence.*;


@Entity
@Table(name = "app_versions")
public class AppVersion extends AbstractAuditingEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ios_build_number")
    private String iosBuildNumber;

    @Column(name = "android_build_number")
    private String androidBuildNumber;

    @Column(name = "ios_version")
    private String iosVersion;

    @Column(name = "android_version")
    private String androidVersion;

    @Column(name = "ios_force_update")
    private boolean iosForceUpdate;

    @Column(name = "android_force_update")
    private boolean androidForceUpdate;

    @Column(name = "play_store_url")
    private String playStoreURL;

    @Column(name = "app_store_url")
    private String appStoreURL;

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
}
