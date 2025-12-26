package com.sfl.core.service.social;

import com.google.gson.Gson;
import com.sfl.core.config.social.SocialLoginProperties;
import com.sfl.core.domain.enumeration.OauthProvider;
import com.sfl.core.service.dto.AppleTokenPayloadDTO;
import com.sfl.core.service.dto.AppleTokenResponseDTO;
import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.SocialLoginKeyValueDTO;
import com.sfl.core.service.helper.AppleLoginHelperService;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import io.jsonwebtoken.io.Decoders;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service(value = "apple")
@Transactional
public class AppleLoginService implements SocialMediaLoginService {

    private static final Logger log = LoggerFactory.getLogger(AppleLoginService.class);

    private final AppleLoginHelperService appleLoginHelperService;
    private final SocialTokenGenerationService ssoTokenGenerationService;
    private final SocialLoginProperties ssoLoginProperties;
    private String name;
    private static final String TOKEN_KEY = "authorizationCode";
    private static final String USER_NAME = "name";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String GRANT_TYPE = "grant_type";
    private static final String GRANT_TYPE_VALUE = "authorization_code";
    private static final String AUTH_CODE = "code";
    private static final String HEADER_CONTENT = "Content-Type";
    private static final String HEADER_CONTENT_TYPE = "application/x-www-form-urlencoded";

    public AppleLoginService(AppleLoginHelperService appleLoginHelperService, SocialTokenGenerationService ssoTokenGenerationService, SocialLoginProperties ssoLoginProperties) {
        this.appleLoginHelperService = appleLoginHelperService;
        this.ssoTokenGenerationService = ssoTokenGenerationService;
        this.ssoLoginProperties = ssoLoginProperties;
    }

    //Testing is remaining
    @Override
    public JwtTokenDTO login(Object object) {
        log.debug("Request to register user using apple");

        if (object instanceof AppleTokenResponseDTO tokenResponse) {

            // Parse id token from Token
            String idToken = tokenResponse.getIdToken();
            String payload = idToken.split("\\.")[1];// 0 is header we ignore it for now
            String decoded = new String(Decoders.BASE64.decode(payload));
            AppleTokenPayloadDTO idTokenPayload = new Gson().fromJson(decoded, AppleTokenPayloadDTO.class);
            if (idTokenPayload == null) {
                throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
            }
            return ssoTokenGenerationService.verifyUserAndGenerateJWT(idTokenPayload.getEmail(), name, OauthProvider.APPLE);
        } else {
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Object verifyToken(List<SocialLoginKeyValueDTO> tokens) {
        log.debug("Request for verifying auth token for apple: {}", tokens);
        String authCode ="";
        SocialLoginProperties.Apple appleProperties = ssoLoginProperties.getApple();
        String clientSecret = appleLoginHelperService.generateClientSecret(appleProperties.getAppId(), appleProperties.getKeyId(), appleProperties.getTeamId(), appleProperties.getAudienceUrl());
        for (SocialLoginKeyValueDTO object : tokens) {
            if (object.getKey().equals(TOKEN_KEY)) {
                authCode = object.getValue();
            } else if (object.getKey().equals(USER_NAME)) {
                name = object.getValue();
            }
        }
        long httpTimeOut = SocialLoginProperties.Apple.APPLE_HTTP_REQUEST_TIME_OUT;
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(httpTimeOut, TimeUnit.SECONDS)
            .writeTimeout(httpTimeOut, TimeUnit.SECONDS)
            .readTimeout(httpTimeOut, TimeUnit.SECONDS)
            .build();

        RequestBody requestBody = new FormBody
            .Builder().add(CLIENT_ID, appleProperties.getAppId()).add(CLIENT_SECRET, clientSecret).add(GRANT_TYPE, GRANT_TYPE_VALUE)
            .add(AUTH_CODE, authCode).build();

        // Prepare rest request
        Request request = new Request.Builder().url(appleProperties.getAuthUrl()).post(requestBody)
            .header(HEADER_CONTENT, HEADER_CONTENT_TYPE).build();

        try {
            Response resp = okHttpClient.newCall(request).execute();
            String response = Objects.requireNonNull(resp.body()).string();

            // Parse response as DTO
            return new ObjectMapper().readValue(response, AppleTokenResponseDTO.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
    }
}
