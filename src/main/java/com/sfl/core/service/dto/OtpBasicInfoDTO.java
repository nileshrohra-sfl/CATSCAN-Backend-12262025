package com.sfl.core.service.dto;

import com.sfl.lib.core.twlioutil.domain.enumeration.OtpChannelType;

import javax.validation.constraints.NotNull;

public class OtpBasicInfoDTO {

    @NotNull
    private String to;

    private OtpChannelType channel;

    private String countryDialCode;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public OtpChannelType getChannel() {
        return channel;
    }

    public void setChannel(OtpChannelType channel) {
        this.channel = channel;
    }

    public String getCountryDialCode() {
        return countryDialCode;
    }

    public void setCountryDialCode(String countryDialCode) {
        this.countryDialCode = countryDialCode;
    }

    public OtpBasicInfoDTO to(String to) {
        this.to = to;
        return this;
    }

    @Override
    public String toString() {
        return "OtpSendDTO{" +
            "to='" + to + '\'' +
            "channel='" + channel + '\'' +
            '}';
    }
}
