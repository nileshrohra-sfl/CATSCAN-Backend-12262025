package com.sfl.core.service.dto;

public class OtpSendDTO extends OtpBasicInfoDTO {

    @Override
    public OtpSendDTO to(String to) {
        super.to(to);
        return this;
    }
}
