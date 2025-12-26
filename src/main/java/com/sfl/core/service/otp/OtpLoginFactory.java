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
public class OtpLoginFactory {

    private final Logger log = LoggerFactory.getLogger(OtpLoginFactory.class);
    private Map<String, OneTimePasswordLoginService> loginStrategies;
    private static final String LOGIN = "Login";

    public OtpLoginFactory(Map<String, OneTimePasswordLoginService> loginStrategies) {
        this.loginStrategies = loginStrategies;
    }

    public OneTimePasswordLoginService getServiceByType(OtpChannelType type) {
        log.debug("Request to get login strategy by type:{}", type);
        String serviceName = getServiceNameByType(type);
        if (!loginStrategies.containsKey(serviceName)) {
            log.error("The strategy type does not exist:{}", serviceName);
            throw new GlobalException(Constants.LOGIN_FAILED_MESSAGE, Constants.LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
        return loginStrategies.get(serviceName);
    }

    private String getServiceNameByType(OtpChannelType otpChannelType) {
        return otpChannelType.getOtpChannelValue().concat(LOGIN);
    }
}
