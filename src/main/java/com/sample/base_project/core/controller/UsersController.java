package com.sample.base_project.core.controller;

import com.sample.base_project.core.mapper.UserAccountMapper;
import com.sample.base_project.core.mapper.UserMapper;
import com.sample.base_project.core.request.CreateUserRequest;
import com.sample.base_project.core.response.UserAccountDto;
import com.sample.base_project.core.response.base.Result;
import com.sample.base_project.base.security.annotation.PermissionAuth;
import com.sample.base_project.common.base.view.GetBasePageParam;
import com.sample.base_project.common.utils.paging.PaginationResponse;
import com.sample.base_project.core.model.User;
import com.sample.base_project.core.response.UserDto;
import com.sample.base_project.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PermissionAuth(PermissionAuth.PermissionType.PUBLIC)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService service;

    @GetMapping
    public PaginationResponse<UserAccountDto> getUsers(@Valid GetBasePageParam<User> param) {
        param.setMapRef(true);
        return service.getPage(param).map(UserAccountMapper.INSTANCE::toDto);
    }

    @PostMapping
    public Result<UserDto> create(@RequestBody @Valid CreateUserRequest param) {
        return Result.success(UserMapper.INSTANCE.toDto(service.create(param)));
    }

}
