package com.sfl.core.service.social;

import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.SocialOauthDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SocialLoginService {

    private static final Logger log = LoggerFactory.getLogger(SocialLoginService.class);

    private final SocialLoginStrategyFactory ssoLoginStrategyFactory;

    public SocialLoginService(SocialLoginStrategyFactory ssoLoginStrategyFactory) {
        this.ssoLoginStrategyFactory = ssoLoginStrategyFactory;
    }

    public JwtTokenDTO ssoLogin(SocialOauthDTO oAuthDTO) {
        log.info("login request for : {}", oAuthDTO);
        SocialMediaLoginService socialMediaLoginService = ssoLoginStrategyFactory.getServiceByType(oAuthDTO.getAuthProvider().toString());
        Object object = socialMediaLoginService.verifyToken(oAuthDTO.getTokens());
        return socialMediaLoginService.login(object);
    }
}
