package com.sfl.core.service.dto;

import javax.validation.constraints.NotNull;

public class AdminPasswordChangeDTO {
    @NotNull
    private Long userId;

    @NotNull
    private String newPassword;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    @Override
    public String toString() {
        return "AdminChangePasswordDTO{" +
            "userId='" + userId + '\'' +
            ", newPassword='" + newPassword + '\'' +
            '}';
    }
}
