package com.sfl.core.service.otp;

import com.sfl.core.config.otp.OtpLoginProperties;
import com.sfl.core.domain.SflUser;
import com.sfl.core.security.jwt.TokenProvider;
import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.OtpSendDTO;
import com.sfl.core.service.dto.OtpVerifyDTO;
import com.sfl.core.service.dto.VerifyOtpDTO;
import com.sfl.core.service.impl.SflUserServiceImpl;
import com.sfl.core.util.StringUtil;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service(value = "smsSignUp")
@Transactional
public class SmsOtpSignUpService implements OneTimePasswordSignUpService {

    private final Logger log = LoggerFactory.getLogger(SmsOtpSignUpService.class);
    private final SflUserServiceImpl sflUserService;
    private final SendOtpService sendOtpService;
    private final TokenProvider tokenProvider;
    private final OtpLoginProperties otpLoginProperties;

    public SmsOtpSignUpService(SflUserServiceImpl sflUserService, SendOtpService sendOtpService, TokenProvider tokenProvider, OtpLoginProperties otpLoginProperties) {
        this.sflUserService = sflUserService;
        this.sendOtpService = sendOtpService;
        this.tokenProvider = tokenProvider;
        this.otpLoginProperties = otpLoginProperties;
    }

    @Override
    public void sendOTP(OtpSendDTO otpSendDTO) {
        log.debug("Send OTP on sms on sign-up:{}", otpSendDTO);
        String countryCode = getCountryCode(otpSendDTO.getCountryDialCode());
        Optional<SflUser> sflUser = sflUserService.getUserByPhoneNumber(otpSendDTO.getTo(), countryCode);
        if (sflUser.isPresent()) {
            throw new GlobalException(Constants.USER_ALREADY_EXIST_EXCEPTION_MESSAGE, Constants.USER_ALREADY_EXIST_EXCEPTION_CODE,
                HttpStatus.BAD_REQUEST);
        }
        sendOtpService.sendOtpToUser(otpSendDTO.to(StringUtil.concatString(countryCode, otpSendDTO.getTo())));
    }

    @Override
    public JwtTokenDTO verifyOTP(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify otp that was sent on sms on sign-up{}:", otpVerifyDTO);
        String countryCode = getCountryCode(otpVerifyDTO.getCountryDialCode());
        String receiver = otpVerifyDTO.getTo();
        sendOtpService.verifyOTP(otpVerifyDTO.to(StringUtil.concatString(countryCode, receiver)));
        otpVerifyDTO.setTo(receiver);
        SflUser sflUser = sflUserService.createNewUserForOtpLogin(otpVerifyDTO);
        return tokenProvider.generateMobileJwtToken(sflUser);
    }

    @Override
    public VerifyOtpDTO otpVerify(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify otp that was sent on sms on sign-up{}:", otpVerifyDTO);
        String countryCode = getCountryCode(otpVerifyDTO.getCountryDialCode());
        String receiver = otpVerifyDTO.getTo();
        sendOtpService.verifyOTP(otpVerifyDTO.to(StringUtil.concatString(countryCode, receiver)));
        otpVerifyDTO.setTo(receiver);
        SflUser sflUser = sflUserService.createNewUserForOtpLogin(otpVerifyDTO);
        JwtTokenDTO tokens = tokenProvider.generateJwtToken(sflUser);
        return new VerifyOtpDTO(tokens.getAccessToken(), tokens.getRefreshToken(), sflUser.getId());
    }

    private String getCountryCode(String countryDialCode) {
        return StringUtil.getCountryDialCode(countryDialCode, otpLoginProperties.getDefaultCountryCode());
    }
}
