package com.sfl.core.web.rest;

import com.sfl.core.security.jwt.JWTFilter;
import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.SocialOauthDTO;
import com.sfl.core.service.social.SocialLoginService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sfl.core.web.rest.customhandler.Constants.BEARER;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "javabaseproject")
public class SocialLoginController {

    private final SocialLoginService ssoLoginService;

    public SocialLoginController(SocialLoginService ssoLoginService) {
        this.ssoLoginService = ssoLoginService;
    }

    @PostMapping("/authenticate/sso")
    public ResponseEntity<UserJWTController.JWTToken> getAuthorizationTokenFromSso(@RequestBody SocialOauthDTO oAuthDTO) {

        JwtTokenDTO jwt = ssoLoginService.ssoLogin(oAuthDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, BEARER + jwt);
        return new ResponseEntity<>(new UserJWTController.JWTToken(jwt.getAccessToken(), jwt.getRefreshToken()), httpHeaders, HttpStatus.OK);
    }
}
