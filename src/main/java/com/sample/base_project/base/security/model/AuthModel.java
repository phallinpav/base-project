package com.sample.base_project.base.security.model;


import com.sample.base_project.base.auth.AuthType;

public record AuthModel(Long uuid, AuthType authType) {
}
