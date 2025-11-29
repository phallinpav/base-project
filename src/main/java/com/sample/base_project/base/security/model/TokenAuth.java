package com.sample.base_project.base.security.model;

public record TokenAuth(String token, boolean allowAnonymous) {
    public TokenAuth(String token) {
        this(token, false);
    }
}
