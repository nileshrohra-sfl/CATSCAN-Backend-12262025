package com.sfl.core.service.social;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sfl.core.config.social.SocialLoginProperties;
import com.sfl.core.domain.enumeration.OauthProvider;
import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.SocialLoginKeyValueDTO;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Service(value = "google")
@Transactional
public class GoogleLoginService implements SocialMediaLoginService {

    private static final Logger log = LoggerFactory.getLogger(GoogleLoginService.class);
    private final SocialTokenGenerationService ssoTokenGenerationService;
    private final SocialLoginProperties ssoLoginProperties;

    private static final String TOKEN_KEY = "idToken";

    public GoogleLoginService(SocialTokenGenerationService ssoTokenGenerationService, SocialLoginProperties ssoLoginProperties) {
        this.ssoTokenGenerationService = ssoTokenGenerationService;
        this.ssoLoginProperties = ssoLoginProperties;
    }

    @Override
    public JwtTokenDTO login(Object object) {
        log.debug("Request for register user using google");
        if (object instanceof GoogleIdToken idToken) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            return ssoTokenGenerationService.verifyUserAndGenerateJWT(payload.getEmail(),(String) payload.get("name"),OauthProvider.GOOGLE);
        } else {
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object verifyToken(List<SocialLoginKeyValueDTO> tokens) {
        log.debug("Request for verifying auth token for google: {}", tokens);
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
            .setAudience(Arrays.asList(ssoLoginProperties.getGoogle().getClientId(), ssoLoginProperties.getGoogle().getAppleClientId())).build();
        String idTokenString = "";
        for (SocialLoginKeyValueDTO object : tokens) {
            if (object.getKey().equals(TOKEN_KEY)) {
                idTokenString = object.getValue();
            }
        }

        if (StringUtils.isBlank(idTokenString))
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);

        try {
            return verifier.verify(idTokenString);
        } catch (IOException | GeneralSecurityException ex) {
            log.error("Error while login with google : {}", ex.getMessage());
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
    }
}
