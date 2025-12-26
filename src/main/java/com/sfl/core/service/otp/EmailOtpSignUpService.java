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

import java.util.Optional;

@Service(value = "emailSignUp")
@Transactional
public class EmailOtpSignUpService implements OneTimePasswordSignUpService {

    private final Logger log = LoggerFactory.getLogger(EmailOtpSignUpService.class);

    private final SflUserServiceImpl sflUserService;
    private final SendOtpService sendOtpService;
    private final TokenProvider tokenProvider;

    public EmailOtpSignUpService(SflUserServiceImpl sflUserService, SendOtpService sendOtpService, TokenProvider tokenProvider) {
        this.sflUserService = sflUserService;
        this.sendOtpService = sendOtpService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void sendOTP(OtpSendDTO otpSendDTO) {
        log.debug("Send OTP on email on sign-up:{}", otpSendDTO);
        Optional<SflUser> sflUser = sflUserService.getUserByEmail(otpSendDTO.getTo());
        if (sflUser.isPresent()) {
            throw new GlobalException(Constants.USER_ALREADY_EXIST_EXCEPTION_MESSAGE, Constants.USER_ALREADY_EXIST_EXCEPTION_CODE,
                HttpStatus.BAD_REQUEST);
        }
        sendOtpService.sendOtpToUser(otpSendDTO);
    }

    @Override
    public JwtTokenDTO verifyOTP(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify otp that was sent on email on sign-up{}:", otpVerifyDTO);
        sendOtpService.verifyOTP(otpVerifyDTO);
        SflUser sflUser = sflUserService.createNewUserForOtpLogin(otpVerifyDTO);
        return tokenProvider.generateJwtToken(sflUser);
    }

    @Override
    public VerifyOtpDTO otpVerify(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify otp that was sent on email on sign-up{}:", otpVerifyDTO);
        sendOtpService.verifyOTP(otpVerifyDTO);
        SflUser sflUser = sflUserService.createNewUserForOtpLogin(otpVerifyDTO);
        JwtTokenDTO tokens = tokenProvider.generateJwtToken(sflUser);
        return new VerifyOtpDTO(tokens.getAccessToken(), tokens.getRefreshToken(), sflUser.getId()) ;
    }
}

