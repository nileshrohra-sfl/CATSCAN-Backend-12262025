package com.sfl.core.service.social;

import com.sfl.core.config.social.SocialLoginProperties;
import com.sfl.core.domain.enumeration.OauthProvider;
import com.sfl.core.service.dto.FacebookUserDTO;
import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.SocialLoginKeyValueDTO;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "facebook")
@Transactional
public class FacebookLoginService implements SocialMediaLoginService {

    private static final Logger log = LoggerFactory.getLogger(FacebookLoginService.class);

    private final SocialTokenGenerationService ssoTokenGenerationService;
    private final SocialLoginProperties ssoLoginProperties;
    private final RestTemplate restTemplate;
    private static final String TOKEN_KEY = "token";
    private static final String ACCESS_TOKEN ="accessToken";
    private static final String INPUT_TOKEN = "input_token";
    private static final String APP_TOKEN = "access_token";
    private static final String HEADER_ACCEPT  = "Accept";

    public FacebookLoginService(SocialTokenGenerationService ssoTokenGenerationService, SocialLoginProperties ssoLoginProperties, RestTemplate restTemplate) {
        this.ssoTokenGenerationService = ssoTokenGenerationService;
        this.ssoLoginProperties = ssoLoginProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    public JwtTokenDTO login(Object object) {
        log.debug("Request for register user using facebook");
        if (object instanceof FacebookUserDTO facebookUserDTO) {
            return ssoTokenGenerationService.verifyUserAndGenerateJWT(facebookUserDTO.getEmail(), facebookUserDTO.getName(),
                OauthProvider.FACEBOOK);

        } else {
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object verifyToken(List<SocialLoginKeyValueDTO> tokens) {
        log.debug("Request for verifying auth token for facebook: {}", tokens);
        String idTokenString = "";
        for (SocialLoginKeyValueDTO object : tokens) {
            if (object.getKey().equals(TOKEN_KEY)) {
                idTokenString = object.getValue();
            }
        }

        if (StringUtils.isBlank(idTokenString))
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);

        Map<String, String> params = new HashMap<>();
        params.put(ACCESS_TOKEN, idTokenString);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        SocialLoginProperties.Facebook facebookProperties = ssoLoginProperties.getFacebook();
        String appToken = facebookProperties.getAppId() + "|" + facebookProperties.getAppSecret();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(facebookProperties.getVerifyTokenUrl())
            .queryParam(INPUT_TOKEN, idTokenString)
            .queryParam(APP_TOKEN, appToken);

        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers), String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
        return restTemplate.getForObject(facebookProperties.getProfileDetailUrl(), FacebookUserDTO.class, params);
    }
}
