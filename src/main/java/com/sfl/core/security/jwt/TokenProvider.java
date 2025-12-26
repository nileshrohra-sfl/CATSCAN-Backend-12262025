package com.sfl.core.security.jwt;

import com.sfl.core.domain.Authority;
import com.sfl.core.domain.SflUser;
import com.sfl.core.security.ApplicationUserDetails;
import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.util.StringUtil;
import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private Key key;

    /**
     * Validity for access token in milliseconds
     */
    private long tokenValidityInMilliseconds;

    private long mobileTokenValidityInMS;

    /**
     * Validity for refresh token in milliseconds
     */
    private long tokenValidityInMillisecondsForRememberMe;

    private final JHipsterProperties jHipsterProperties;

    public TokenProvider(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        JHipsterProperties.Security.Authentication.Jwt jwtProperties = jHipsterProperties.getSecurity().getAuthentication().getJwt();
        String secret = jwtProperties.getSecret();
        if (!StringUtils.isEmpty(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jwtProperties.getBase64Secret());
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds =
            1000 * jwtProperties.getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * jwtProperties
                .getTokenValidityInSecondsForRememberMe();
    }

    /**
     * Create JWT token
     *
     * @param authentication
     * @return
     */
    public JwtTokenDTO createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        ApplicationUserDetails applicationUserDetails = (ApplicationUserDetails) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("id",applicationUserDetails.getId());
        return generateJwtTokens(claims, authorities);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromToken(token);
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .toList();
        String subject = (claims.getSubject() != null && !claims.getSubject().trim().isEmpty()) ? claims.getSubject() : claims.get("phoneNumber").toString();
        ApplicationUserDetails principal = new ApplicationUserDetails((Integer) claims.get("id"), subject, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    public JwtTokenDTO generateJwtToken(SflUser user) {
        String authorities = user.getAuthorities().stream().map(Authority::getName)
            .collect(Collectors.joining(","));
        String subject = StringUtil.isNotBlank(user.getPhoneNumber()) ? user.getPhoneNumber().toString() : user.getEmail();
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("email", user.getEmail() + "");
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("id",user.getId());
        return generateJwtTokens(claims, authorities);
    }

    public JwtTokenDTO generateMobileJwtToken(SflUser user) {
        String authorities = user.getAuthorities().stream().map(Authority::getName)
            .collect(Collectors.joining(","));
        String subject = (user.getPhoneNumber() != null && user.getPhoneNumber() > 0) ? user.getPhoneNumber().toString() : user.getEmail();
        Claims claims = Jwts.claims().setSubject(subject);
            claims.put("email", user.getEmail() + "");
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("id",user.getId());
        return generateJwtMobileTokens(claims, authorities);
    }

    private String generateToken(Claims claims, String authorities, long validityMs) {
        return Jwts.builder()
            .setClaims(claims)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(new Date((new Date()).getTime() + validityMs))
            .compact();
    }

    private String generateNeverExpireToken(Claims claims, String authorities) {
        return Jwts.builder()
            .setClaims(claims)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact(); // No expiration set
    }

    /**
     * Create JWT tokens(access & refresh tokens) from authority and claims
     *
     * @param claims
     * @param authorities
     * @return
     */
    private JwtTokenDTO generateJwtTokens(Claims claims, String authorities) {
        String accessToken = generateToken(claims, authorities, this.tokenValidityInMilliseconds);
        String refreshToken = generateToken(claims, authorities, this.tokenValidityInMillisecondsForRememberMe);

        return new JwtTokenDTO(accessToken, refreshToken);
    }

    /**
     * Refresh JWT access token
     *
     * @param refreshToken JWT refresh token
     * @return
     */
    public JwtTokenDTO refreshToken(String refreshToken) {

        Claims claims = getClaimsFromToken(refreshToken);
        String authority = claims.get(AUTHORITIES_KEY).toString();

        String accessToken = generateToken(claims, authority, this.tokenValidityInMilliseconds);
        //refresh token will be same only, need to reset access token only
        return new JwtTokenDTO(accessToken, refreshToken);
    }

    /**
     * Create JWT tokens for mobile(access & refresh tokens) from authority and claims
     *
     * @param claims
     * @param authorities
     * @return
     */
    private JwtTokenDTO generateJwtMobileTokens(Claims claims, String authorities) {
        String accessToken = generateNeverExpireToken(claims, authorities);
        String refreshToken = generateNeverExpireToken(claims, authorities);

        return new JwtTokenDTO(accessToken, refreshToken);
    }


}
