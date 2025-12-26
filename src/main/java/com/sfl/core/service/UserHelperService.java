package com.sfl.core.service;

import com.sfl.core.domain.SflUser;
import com.sfl.core.repository.SflUserRepository;
import com.sfl.core.security.SecurityUtils;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserHelperService {

    private final SflUserRepository sflUserRepository;

    public SflUser getCurrentLoginUser() {
        log.debug("Fetching current login user details");

        String login = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new GlobalException(
                Constants.USER_NOT_LOGGED_IN_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_USER_NOT_LOGGED_IN_EXCEPTION_CODE,
                HttpStatus.FORBIDDEN
            ));
        log.debug("Current login user: {}", login);

        return sflUserRepository.findByLogin(login)
            .orElseThrow(() -> new GlobalException(
                Constants.USER_NOT_LOGGED_IN_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_USER_NOT_LOGGED_IN_EXCEPTION_CODE,
                HttpStatus.FORBIDDEN
            ));
    }
}
