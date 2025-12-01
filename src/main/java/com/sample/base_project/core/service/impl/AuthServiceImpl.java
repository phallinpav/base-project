package com.sample.base_project.core.service.impl;

import com.sample.base_project.base.utils.system.LoginTokenUtils;
import com.sample.base_project.common.exception.ErrorMessageUtils;
import com.sample.base_project.common.exception.ErrorType;
import com.sample.base_project.common.utils.common.PasswordUtils;
import com.sample.base_project.common.utils.common.RedisUtils;
import com.sample.base_project.core.model.User;
import com.sample.base_project.core.request.LoginRequest;
import com.sample.base_project.core.response.TokenDto;
import com.sample.base_project.core.service.AuthService;
import com.sample.base_project.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final RedisUtils redisUtils;
    private final LoginTokenUtils loginTokenUtils;

    @Override
    public TokenDto login(LoginRequest loginRequest) {
        User user = userService.get(loginRequest.toGetUserRequest());
        if (PasswordUtils.isMatch(loginRequest.getPassword(), user.getPassword())) {
            String token = loginTokenUtils.generateToken(user.getUuid());
            redisUtils.setString(token, token);
            return new TokenDto(token);
        } else {
            throw ErrorMessageUtils.error(ErrorType.UNAUTHORIZED, "unauthorized.user");
        }
    }
}
