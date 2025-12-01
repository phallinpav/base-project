package com.sample.base_project.core.controller;

import com.sample.base_project.base.security.annotation.PermissionAuth;
import com.sample.base_project.core.request.LoginRequest;
import com.sample.base_project.core.response.TokenDto;
import com.sample.base_project.core.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PermissionAuth(PermissionAuth.PermissionType.PUBLIC)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public TokenDto login(@RequestBody @Valid LoginRequest request) {
        return service.login(request);
    }

}
