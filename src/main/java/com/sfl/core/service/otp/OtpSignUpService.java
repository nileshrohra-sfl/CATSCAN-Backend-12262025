package com.sfl.core.service.otp;

import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.OtpSendDTO;
import com.sfl.core.service.dto.OtpVerifyDTO;
import com.sfl.core.service.dto.VerifyOtpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OtpSignUpService {

    private final Logger log = LoggerFactory.getLogger(OtpSignUpService.class);
    private final OtpSignUpFactory otpSignUpFactory;

    public OtpSignUpService(OtpSignUpFactory otpSignUpFactory) {
        this.otpSignUpFactory = otpSignUpFactory;
    }

    public void sendOTP(OtpSendDTO otpSendDTO) {
        log.debug("Send OTP on sign-up:{}", otpSendDTO);
        OneTimePasswordSignUpService oneTimePasswordSignUpService = otpSignUpFactory.getServiceByType(otpSendDTO.getChannel());
        oneTimePasswordSignUpService.sendOTP(otpSendDTO);
    }

    public JwtTokenDTO verifyOTP(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify OTP on sign-up:{}", otpVerifyDTO);
        OneTimePasswordSignUpService oneTimePasswordSignUpService = otpSignUpFactory.getServiceByType(otpVerifyDTO.getChannel());
        return oneTimePasswordSignUpService.verifyOTP(otpVerifyDTO);
    }

    /**
     * Verify the user provided OTP which was sent on email, sms or on call
     *
     * @param otpVerifyDTO the DTO which will contain OTp.
     * @return VerifyOtpDTO
     */
    public VerifyOtpDTO otpVerify(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify OTP on sign-up:{}", otpVerifyDTO);
        OneTimePasswordSignUpService oneTimePasswordSignUpService = otpSignUpFactory.getServiceByType(otpVerifyDTO.getChannel());
        return oneTimePasswordSignUpService.otpVerify(otpVerifyDTO);
    }
}
