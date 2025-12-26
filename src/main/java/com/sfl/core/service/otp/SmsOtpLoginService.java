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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service(value = "smsLogin")
@Transactional
public class SmsOtpLoginService implements OneTimePasswordLoginService {
    @Value("${app.mobile-login.secret}")
    private String appSecret;
    private final Logger log = LoggerFactory.getLogger(SmsOtpLoginService.class);
    private final SflUserServiceImpl sflUserService;
    private final SendOtpService sendOtpService;
    private final TokenProvider tokenProvider;
    private final OtpLoginProperties otpLoginProperties;

    private final SmsOtpSignUpService otpSignUpService;

    public SmsOtpLoginService(SflUserServiceImpl sflUserService, SendOtpService sendOtpService, TokenProvider tokenProvider, OtpLoginProperties otpLoginProperties, SmsOtpSignUpService otpSignUpService) {
        this.sflUserService = sflUserService;
        this.sendOtpService = sendOtpService;
        this.tokenProvider = tokenProvider;
        this.otpLoginProperties = otpLoginProperties;
        this.otpSignUpService = otpSignUpService;
    }

    @Override
    public void sendOTP(OtpSendDTO otpSendDTO) {
        log.debug("Send OTP on login:{}", otpSendDTO);
        String countryCode = getCountryCode(otpSendDTO.getCountryDialCode());
        //Below check is not required for this project
//        checkUserExistsByPhone(otpSendDTO.getTo(), countryCode);

        sendOtpService.sendOtpToUser(otpSendDTO.to(StringUtil.concatString(countryCode, otpSendDTO.getTo())));
    }

    @Override
    public JwtTokenDTO verifyOTP(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify otp that was sent on login{}:", otpVerifyDTO);
        String countryCode = getCountryCode(otpVerifyDTO.getCountryDialCode());
        String oldReceiver = otpVerifyDTO.getTo();
        JwtTokenDTO tokenDTO = null;
        Optional<SflUser> user = sflUserService.findByPhoneNumberAndCountryCode(oldReceiver, otpVerifyDTO.getCountryDialCode());
        if (user.isPresent()) {
            sendOtpService.verifyOTP(otpVerifyDTO.to(StringUtil.concatString(countryCode, oldReceiver)));
            tokenDTO = tokenProvider.generateMobileJwtToken(user.get());
            tokenDTO.setIsProfileExist(Boolean.TRUE);
        } else {
            tokenDTO = otpSignUpService.verifyOTP(otpVerifyDTO);
            tokenDTO.setIsProfileExist(Boolean.FALSE);
        }
        return tokenDTO;

//        log.debug("Verify otp that was sent on login{}:", otpVerifyDTO);
//        String countryCode = getCountryCode(otpVerifyDTO.getCountryDialCode());
//        String oldReceiver = otpVerifyDTO.getTo();
//        sendOtpService.verifyOTP(otpVerifyDTO.to(StringUtil.concatString(countryCode, oldReceiver)));
//        Optional<SflUser> user = getUserExistByPhone(oldReceiver, otpVerifyDTO.getCountryDialCode());
//        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
//        jwtTokenDTO.setAccessToken(appSecret);
//        if (user.isPresent()) {
//            jwtTokenDTO.setIsProfileExist(Boolean.TRUE);
//        }
//        return jwtTokenDTO;
    }

    @Override
    public VerifyOtpDTO otpVerify(OtpVerifyDTO otpVerifyDTO) {
        log.debug("Verify otp that was sent on login{}:", otpVerifyDTO);
        String countryCode = getCountryCode(otpVerifyDTO.getCountryDialCode());
        String oldReceiver = otpVerifyDTO.getTo();
        sendOtpService.verifyOTP(otpVerifyDTO.to(StringUtil.concatString(countryCode, oldReceiver)));
        SflUser user = checkUserExistsByPhone(oldReceiver, otpVerifyDTO.getCountryDialCode());
        JwtTokenDTO tokens = tokenProvider.generateJwtToken(user);
        return new VerifyOtpDTO(tokens.getAccessToken(), tokens.getRefreshToken(), user.getId());
    }

    private SflUser checkUserExistsByPhone(String number, String countryDialCode) {
        return sflUserService.getUserByPhoneNumber(number, countryDialCode).orElseThrow(
            () -> new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE, Constants.USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND));
    }

    private Optional<SflUser> getUserExistByPhone(String number, String countryDialCode) {
        return sflUserService.getUserByPhoneNumber(number, countryDialCode);
    }

    private String getCountryCode(String countryDialCode) {
        return StringUtil.getCountryDialCode(countryDialCode, otpLoginProperties.getDefaultCountryCode());
    }
}
