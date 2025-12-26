package com.sfl.core.web.rest.vm;

public class VerifyCustomMessageVM extends CustomMessageVM {

    private Long id;
    private Boolean isProfileComplete;
    private String accessToken;
    private String refreshToken;
    private int applicationStatusCode;

    public VerifyCustomMessageVM(Long id, String message, String accessToken, String refreshToken,
                                 boolean isProfileComplete, int applicationStatusCode) {
        super(message);
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isProfileComplete = isProfileComplete;
        this.applicationStatusCode = applicationStatusCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getProfileComplete() {
        return isProfileComplete;
    }

    public void setProfileComplete(Boolean profileComplete) {
        isProfileComplete = profileComplete;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getApplicationStatusCode() {
        return applicationStatusCode;
    }

    public void setApplicationStatusCode(int applicationStatusCode) {
        this.applicationStatusCode = applicationStatusCode;
    }
}
