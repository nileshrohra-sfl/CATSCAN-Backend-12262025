package com.sfl.core.security.jwt;

import com.sfl.core.service.util.EncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

import static com.sfl.core.config.Constants.BASE64_PATTERN;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
@Component
public class JWTFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${secret}")
    protected String appSecret;

    @Value("${app.mobile-login.value}")
    protected String appSecretValue;

    @Value("${app.mobile-login.base64.key}")
    protected String base64Key;

    @Value("${app.mobile-login.base64.iv}")
    protected String base64Iv;

    private final TokenProvider tokenProvider;
    private static final String USERNAME = "defaultUser";
    private static final String PASSWORD = "defaultPassword";
    private static final String MOBILE_USER_AGENT = "mobile";
    public static final String PLATFORM_TYPE = "platform";
    private final Logger log = LoggerFactory.getLogger(JWTFilter.class);

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        log.info("Start doFilter method.");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("Finished doFilter method.");
    }


    private void handleMobileUser(HttpServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        String secret = request.getHeader("Secret");
        try {
            //TODO: validate base64 string is valid
            if (!BASE64_PATTERN.matcher(secret).matches()) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid secret");
                return;
            }
            // TODO: The below static assign keys will be remove after @Value keys getting from application file will resolved.
            base64Key = "r6G9s6Ps8e4qY0cRxLQziw==";
            base64Iv = "U2FsdGVkX1+dZ0R/1R7s3Q==";
            appSecretValue = "c6074d59dfa3dec7";
            SecretKey key = EncryptionUtils.getKeyFromBase64String(base64Key);
            byte[] decodedIv = Base64.getDecoder().decode(base64Iv);
            IvParameterSpec iv = new IvParameterSpec(decodedIv);
            String decryptedValue = EncryptionUtils.decrypt(secret, key, iv);
            if (appSecretValue.equals(decryptedValue)) {
                //Set authentication for anonymous user
                UserDetails userDetails = User.withUsername(USERNAME).password(PASSWORD).authorities(Collections.singletonList(() -> "ROLE_USER")).build();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            } else {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid secret");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleWebUser(HttpServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        String jwt = resolveToken(request);
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText (bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean skipAuth(HttpServletRequest servletRequest) {
        String url = servletRequest.getRequestURL().toString();
        return (url.contains("/management") && servletRequest.getMethod().equalsIgnoreCase("GET")
                || url.contains("account/login/init") && servletRequest.getMethod().equalsIgnoreCase("POST")
        || url.contains("account/login/finish") && servletRequest.getMethod().equalsIgnoreCase("POST"));
    }
}
