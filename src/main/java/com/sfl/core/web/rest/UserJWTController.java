package com.sfl.core.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfl.core.security.jwt.JWTFilter;
import com.sfl.core.security.jwt.TokenProvider;
import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.web.rest.vm.LoginVM;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.sfl.core.web.rest.customhandler.Constants.BEARER;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "javabaseproject")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return getJwtTokenResponseEntity(tokenProvider.createToken(authentication));
    }

    /**
     * Refresh expired access token using refresh token
     *
     * @param request Http request
     * @return JWT tokens: Access token + refresh token
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<JWTToken> authorize(@Valid HttpServletRequest request) {

        String refreshToken = request.getHeader(JWTFilter.AUTHORIZATION_HEADER);
        refreshToken = refreshToken.substring(BEARER.length());

        return getJwtTokenResponseEntity(tokenProvider.refreshToken(refreshToken));
    }

    /**
     * Generate JWT response entity
     *
     * @param tokens generated JWT tokens
     * @return response entity
     */
    @NotNull
    private ResponseEntity<JWTToken> getJwtTokenResponseEntity(JwtTokenDTO tokens) {
        String accessToken = tokens.getAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, BEARER + accessToken);
        return new ResponseEntity<>(new JWTToken(accessToken, tokens.getRefreshToken()), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String accessToken;

        private String refreshToken;

        public JWTToken(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        @JsonProperty("access_token")
        String getAccessToken() {
            return accessToken;
        }

        void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @JsonProperty("refresh_token")
        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
