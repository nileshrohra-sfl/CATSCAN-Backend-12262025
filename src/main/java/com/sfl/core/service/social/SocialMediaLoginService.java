package com.sfl.core.service.social;

import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.SocialLoginKeyValueDTO;

import java.util.List;

public interface SocialMediaLoginService {

    JwtTokenDTO login(Object object);

    Object verifyToken(List<SocialLoginKeyValueDTO> tokens);
}
