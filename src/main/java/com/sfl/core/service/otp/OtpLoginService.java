package com.sfl.core.service.otp;

import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.OtpSendDTO;
import com.sfl.core.service.dto.OtpVerifyDTO;
import com.sfl.core.service.dto.VerifyOtpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OtpLoginService {

    private final Logger log = LoggerFactory.getLogger(OtpLoginService.class);
    private final OtpLoginFactory otpLoginFactory;

    public OtpLoginService(OtpLoginFactory otpLoginFactory) {
        this.otpLoginFactory = otpLoginFactory;
    }

    public void sendOTP(OtpSendDTO otpSendDTO) {
        log.debug("Send OTP on login:{}", otpSendDTO);
        OneTimePasswordLoginService oneTimePasswordLoginService = otpLoginFactory.getServiceByType(otpSendDTO.getChannel());
        oneTimePasswordLoginService.sendOTP(otpSendDTO);
    }

    public JwtTokenDTO verifyOTP(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Send OTP on sign-up:{}", otpVerifyDTO);
        OneTimePasswordLoginService oneTimePasswordLoginService = otpLoginFactory.getServiceByType(otpVerifyDTO.getChannel());
        return oneTimePasswordLoginService.verifyOTP(otpVerifyDTO);
    }

    public VerifyOtpDTO otpVerify(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Send OTP on sign-up:{}", otpVerifyDTO);
        OneTimePasswordLoginService oneTimePasswordLoginService = otpLoginFactory.getServiceByType(otpVerifyDTO.getChannel());
        return oneTimePasswordLoginService.otpVerify(otpVerifyDTO);
    }
}
