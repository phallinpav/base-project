package com.sample.base_project.core.controller;

import com.sample.base_project.base.auth.AuthUtils;
import com.sample.base_project.common.base.view.GetBaseParam;
import com.sample.base_project.core.mapper.UserAccountMapper;
import com.sample.base_project.core.mapper.UserMapper;
import com.sample.base_project.core.request.UpdateUserRequest;
import com.sample.base_project.core.response.UserAccountDto;
import com.sample.base_project.core.response.UserDto;
import com.sample.base_project.core.response.base.Result;
import com.sample.base_project.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public UserAccountDto getInfo() {
        return UserAccountMapper.INSTANCE.toDto(service.get(GetBaseParam.of(AuthUtils.getAuthUuid(), true)));
    }

    @PatchMapping
    public Result<UserDto> update(@RequestBody @Valid UpdateUserRequest param) {
        return Result.success(UserMapper.INSTANCE.toDto(service.update(param)));
    }
}
