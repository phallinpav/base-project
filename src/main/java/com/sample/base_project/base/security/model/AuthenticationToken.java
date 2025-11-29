package com.sample.base_project.base.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;

import java.io.Serializable;
import java.util.List;

@Transient
@JsonIgnoreType
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class AuthenticationToken extends AbstractAuthenticationToken implements Serializable {
    private final AuthModel auth;
    private final String token;
    private final String errorMessage;

    public AuthenticationToken() {
        super(null);
        auth = null;
        token = null;
        errorMessage = null;
    }

    public AuthenticationToken(String errorMessage) {
        super(null);
        this.auth = null;
        this.token = null;
        this.errorMessage = errorMessage;
        setAuthenticated(false);
    }

    public AuthenticationToken(AuthModel auth, String token, List<GrantedAuthority> authorityList) {
        super(authorityList);
        this.auth = auth;
        this.token = token;
        this.errorMessage = null;
        setAuthenticated(true);
    }

    @Override
    @JsonIgnore
    public Object getCredentials() {
        return token;
    }

    @Override
    @JsonIgnore
    public Object getPrincipal() {
        return auth;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return super.getName();
    }
}
