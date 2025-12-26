package com.sfl.core.service.otp;

import com.sfl.core.domain.SflUser;
import com.sfl.core.security.jwt.TokenProvider;
import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.OtpSendDTO;
import com.sfl.core.service.dto.OtpVerifyDTO;
import com.sfl.core.service.dto.VerifyOtpDTO;
import com.sfl.core.service.impl.SflUserServiceImpl;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "emailLogin")
@Transactional
public class EmailOtpLoginService implements OneTimePasswordLoginService {

    private final Logger log = LoggerFactory.getLogger(EmailOtpLoginService.class);
    private final SflUserServiceImpl sflUserService;
    private final SendOtpService sendOtpService;
    private final TokenProvider tokenProvider;

    public EmailOtpLoginService(SflUserServiceImpl sflUserService, SendOtpService sendOtpService, TokenProvider tokenProvider) {
        this.sflUserService = sflUserService;
        this.sendOtpService = sendOtpService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void sendOTP(OtpSendDTO otpSendDTO) {
        log.debug("Send OTP on email on login:{}", otpSendDTO);
        checkUserExists(otpSendDTO.getTo());
        sendOtpService.sendOtpToUser(otpSendDTO);
    }

    @Override
    public JwtTokenDTO verifyOTP(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify otp that was sent on email on login{}:", otpVerifyDTO);
        sendOtpService.verifyOTP(otpVerifyDTO);
        SflUser user = checkUserExists(otpVerifyDTO.getTo());
        return tokenProvider.generateMobileJwtToken(user);
    }

    @Override
    public VerifyOtpDTO otpVerify(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify otp that was sent on email on login{}:", otpVerifyDTO);
        sendOtpService.verifyOTP(otpVerifyDTO);
        SflUser user = checkUserExists(otpVerifyDTO.getTo());
        JwtTokenDTO tokens = tokenProvider.generateJwtToken(user);
        return new VerifyOtpDTO(tokens.getAccessToken(), tokens.getRefreshToken(), user.getId());
    }

    private SflUser checkUserExists(String email) {
        return sflUserService.getUserByEmail(email).orElseThrow(
            () -> new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE, Constants.USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND));
    }
}
