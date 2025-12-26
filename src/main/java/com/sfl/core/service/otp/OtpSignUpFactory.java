package com.sfl.core.service.otp;

import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import com.sfl.lib.core.twlioutil.domain.enumeration.OtpChannelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OtpSignUpFactory {

    private final Logger log = LoggerFactory.getLogger(OtpSignUpFactory.class);
    private Map<String, OneTimePasswordSignUpService> loginStrategies;
    private static final String SIGN_UP = "SignUp";

    public OtpSignUpFactory(Map<String, OneTimePasswordSignUpService> loginStrategies) {
        this.loginStrategies = loginStrategies;
    }

    public OneTimePasswordSignUpService getServiceByType(OtpChannelType type) {
        log.debug("Request to get login strategy by type:{}", type);
        String serviceName = getServiceNameByType(type);
        if (!loginStrategies.containsKey(serviceName)) {
            log.error("The strategy type does not exist:{}", serviceName);
            throw new GlobalException(Constants.SIGN_UP_FAILED_MESSAGE, Constants.SIGN_UP_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
        return loginStrategies.get(serviceName);
    }

    private String getServiceNameByType(OtpChannelType otpChannelType) {
        return otpChannelType.getOtpChannelValue().concat(SIGN_UP);
    }
}
