package com.sfl.core.service;

import com.sfl.core.domain.SflUser;
import com.sfl.core.repository.SflUserRepository;
import com.sfl.core.web.rest.errors.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserStatusCheckInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(UserStatusCheckInterceptor.class);

    private static final String MOBILE_NUMBER_REGEX = "^\\+?[1-9]\\d{1,14}$";

    @Autowired
    private SflUserRepository sflUserRepository;
    public static final String PLATFORM_TYPE = "platform";


    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userAgent = request.getHeader(PLATFORM_TYPE);
        if (StringUtils.hasText(userAgent) && userAgent.equals("mobile")) {
            return Boolean.TRUE;
        }
        Principal principal = request.getUserPrincipal();
        if (Objects.nonNull(principal)) {
            log.info("login {}", principal.getName());
            String login = principal.getName();
            SflUser user = null;
            if(isMobileUser(login)){
                Optional<SflUser> optionalSflUser = sflUserRepository.findOneByPhoneNumber(Long.parseLong(login));
                if (optionalSflUser.isPresent()){
                    user = optionalSflUser.get();
                }
            }
            else if (sflUserRepository.existsByEmailOrUserNameAndStatus(login,login,true)) {
                user = sflUserRepository.findByEmailOrUserName(login,login);
            }

            if (user == null) {
                throw new UsernameNotFoundException(login +" User not found.");
            } else if (user.getStatus().equals(Boolean.FALSE)) {
                throw new UnauthorizedException("Your account has been deactivated! Please contact the administrator for more information!");
            }
        }else{

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            response.setHeader("Access-Control-Allow-Credentials", "true");

            // If it's a preflight request, we return OK immediately
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static boolean isMobileUser(String login) {
        Pattern pattern = Pattern.compile(MOBILE_NUMBER_REGEX);
        return pattern.matcher(login).matches();
    }
}
