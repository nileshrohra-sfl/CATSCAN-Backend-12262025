package com.sfl.core.service.otp;

import com.sfl.core.service.dto.OtpSendDTO;
import com.sfl.core.service.dto.OtpVerifyDTO;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import com.sfl.lib.core.twlioutil.expection.TwilioUtilException;
import com.sfl.lib.core.twlioutil.service.OtpService;
import com.sfl.lib.core.twlioutil.service.dto.SendOtpDTO;
import com.sfl.lib.core.twlioutil.service.dto.VerifyOtpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SendOtpService {

    private final Logger log = LoggerFactory.getLogger(SendOtpService.class);
    private final OtpService otpService;

    public SendOtpService(OtpService otpService) {
        this.otpService = otpService;
    }

    @Value("${twilio.util.features.defaultOtp:}")
    private Integer defaultOtp;

    public void sendOtpToUser(OtpSendDTO otpSendDTO) {
        log.debug("Send OTP to user:{}", otpSendDTO);
        if (Objects.nonNull(defaultOtp)) {
            log.debug("Default OTP : {}", defaultOtp);
        } else {
            SendOtpDTO sendOtpDTO = new SendOtpDTO();
            BeanUtils.copyProperties(otpSendDTO, sendOtpDTO);
            try {
                otpService.sendOTP(sendOtpDTO);
            } catch (TwilioUtilException ex) {
                throw new GlobalException(ex.getMessage(), ex.getApplicationStatusCode(), ex.getHttpStatus());
            }
        }
    }

    public void verifyOTP(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify OTP {}", otpVerifyDTO);
        if (Objects.nonNull(defaultOtp)) {
            verifyDefaultOtp(otpVerifyDTO);
        } else {
            VerifyOtpDTO verifyOtpDTO = new VerifyOtpDTO();
            BeanUtils.copyProperties(otpVerifyDTO, verifyOtpDTO);
            try {
                otpService.verifyOTP(verifyOtpDTO);
            } catch (TwilioUtilException ex) {
                throw new GlobalException(ex.getMessage(), ex.getApplicationStatusCode(), ex.getHttpStatus());
            }
        }
    }

    private void verifyDefaultOtp(OtpVerifyDTO otpVerifyDTO) {
        if (!otpVerifyDTO.getOtp().equals(String.valueOf(defaultOtp)))
            throw new GlobalException(Constants.INVALID_OTP_ERROR_MESSAGE, Constants.INVALID_OTP_ERROR_CODE, HttpStatus.BAD_REQUEST);

    }
}
