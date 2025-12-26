package com.sfl.core.security;

import com.sfl.core.domain.SflUser;
import com.sfl.core.repository.SflUserRepository;
import com.sfl.core.service.exception.UserNotActivatedException;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final SflUserRepository sflUserRepository;

    public DomainUserDetailsService(SflUserRepository sflUserRepository) {
        this.sflUserRepository = sflUserRepository;
    }

    @Override
    @Transactional
    public ApplicationUserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return sflUserRepository.findOneWithAuthoritiesByEmailIgnoreCase(login)
                .map(user -> createSpringSecurityUser(login, user, true))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return sflUserRepository.findOneWithAuthoritiesByUserName(lowercaseLogin)
            .map(user -> createSpringSecurityUser(lowercaseLogin, user, false))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));

    }

    private ApplicationUserDetails createSpringSecurityUser(String lowercaseLogin, SflUser sflUser, Boolean isEmail) {
        if (!sflUser.getStatus().booleanValue()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = sflUser.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());
        if(isEmail.booleanValue()) { // If login is by email then email is set
            return new ApplicationUserDetails(sflUser.getId().intValue() ,sflUser.getEmail(),
                sflUser.getPassword(),
                grantedAuthorities);
        } else {
            String userName = (sflUser.getUserName() != null) ? sflUser.getPhoneNumber().toString() : null;
            return new ApplicationUserDetails(sflUser.getId().intValue() , userName,
                sflUser.getPassword(),
                grantedAuthorities);
        }

    }

}
