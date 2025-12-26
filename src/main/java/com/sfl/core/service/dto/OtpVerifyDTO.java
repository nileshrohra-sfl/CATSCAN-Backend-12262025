package com.sfl.core.service.dto;

public class OtpVerifyDTO extends OtpBasicInfoDTO {

    private String otp;

    public String getOtp() { return otp; }

    public void setOtp(String otp) { this.otp = otp; }

    @Override
    public OtpVerifyDTO to(String to) {
        super.to(to);
        return this;
    }

}
