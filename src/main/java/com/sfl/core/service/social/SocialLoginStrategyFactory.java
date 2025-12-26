package com.sfl.core.service.social;

import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SocialLoginStrategyFactory {

    private static final Logger log = LoggerFactory.getLogger(SocialLoginStrategyFactory.class);

    private Map<String, SocialMediaLoginService> loginStrategies;

    public SocialLoginStrategyFactory(Map<String, SocialMediaLoginService> loginStrategies) {
        this.loginStrategies = loginStrategies;
    }

    public SocialMediaLoginService getServiceByType(String type) {
        log.debug("Request to get login strategy by type:{}", type);
        if (!loginStrategies.containsKey(type)) {
            log.error("The strategy type does not exist:{}",type);
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
        return loginStrategies.get(type);
    }
}
