package com.sample.base_project.controller;

import com.sample.base_project.base.security.annotation.PermissionAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PermissionAuth(PermissionAuth.PermissionType.PUBLIC)
@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public void getContactUs() {

    }
}
