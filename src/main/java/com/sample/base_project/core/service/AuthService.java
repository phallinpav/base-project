package com.sample.base_project.core.service;

import com.sample.base_project.core.request.LoginRequest;
import com.sample.base_project.core.response.TokenDto;

public interface AuthService {
    TokenDto login(LoginRequest loginRequest);
}
