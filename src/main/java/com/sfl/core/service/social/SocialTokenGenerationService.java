package com.sfl.core.service.social;

import com.sfl.core.domain.SflUser;
import com.sfl.core.domain.enumeration.OauthProvider;
import com.sfl.core.security.jwt.TokenProvider;
import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.SocialLoginDTO;
import com.sfl.core.service.impl.SflUserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SocialTokenGenerationService {

    private final TokenProvider tokenProvider;

    private final SflUserServiceImpl sflUserServiceImpl;

    public SocialTokenGenerationService(TokenProvider tokenProvider, SflUserServiceImpl sflUserServiceImpl) {
        this.tokenProvider = tokenProvider;
        this.sflUserServiceImpl = sflUserServiceImpl;
    }

    public JwtTokenDTO verifyUserAndGenerateJWT(String email, String name, OauthProvider oauthProvider) {
        Optional<SflUser> optionalUser = sflUserServiceImpl.userByEmailOrUsername(email);
        SflUser user = optionalUser.orElseGet(() -> sflUserServiceImpl.createNewUserForSsoLogin(new SocialLoginDTO(name,email,oauthProvider)));
        return tokenProvider.generateJwtToken(user);
    }
}
