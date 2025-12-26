package com.sfl.core.security;

import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String userName) {
            return userName;
        }
        return null;
    }


    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
            getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean isCurrentUserInRole(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
            getAuthorities(authentication).anyMatch(authority::equals);
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority);
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static String getUserLogin(){
        Optional<String> login =  getCurrentUserLogin();
        return login.orElseThrow(() -> new GlobalException(Constants.ERR_MESSAGE_INVALID_JWT, Constants.UNAUTHORIZED,
            HttpStatus.UNAUTHORIZED));
    }

    /**
     * As per spring auth flow get the user id claim from token principle
     * @return
     */
    public static Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // Cast to your custom `UserDetails` implementation if needed
            ApplicationUserDetails customUserDetails = (ApplicationUserDetails) authentication.getPrincipal();;
            return customUserDetails.getId(); // Replace with the method to get the ID
        }

        return null; // Handle anonymous or unauthenticated cases
    }

    /**
     * As per spring auth flow get the user id claim from token principle
     * @return
     */
    public static Optional<ApplicationUserDetails> getCurrentUserDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // Cast to your custom `UserDetails` implementation if needed
            ApplicationUserDetails customUserDetails = (ApplicationUserDetails) authentication.getPrincipal();;
            return Optional.of(customUserDetails); // Replace with the method to get the ID
        }

        return Optional.empty(); // Handle anonymous or unauthenticated cases
    }
}
