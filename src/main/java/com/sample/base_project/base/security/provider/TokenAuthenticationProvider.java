package com.sample.base_project.base.security.provider;

import com.sample.base_project.base.security.model.AuthenticationToken;
import com.sample.base_project.base.security.model.TokenAuth;
import com.sample.base_project.base.utils.system.LoginTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Slf4j
public abstract class TokenAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private LoginTokenUtils loginTokenUtils;

    public abstract AuthenticationToken customAuthenticateToken(LoginTokenUtils.AuthSession authSession, TokenAuth tokenAuth);

    public abstract LoginTokenUtils.SessionType sessionType();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            if (authentication.getPrincipal() instanceof TokenAuth tokenAuth) {
                LoginTokenUtils.AuthSession authSession = null;
                switch (sessionType()) {
                    case USER -> authSession = loginTokenUtils.getUserAuthSession(tokenAuth.token());
                    case DEV -> authSession = loginTokenUtils.getDevAuthSession(tokenAuth.token());
                }
                if (authSession == null) {
                    if (tokenAuth.allowAnonymous()) {
                        return customAuthenticateToken(null, tokenAuth);
                    }
                    return new AuthenticationToken("invalid token");
                }
                try {
                    return customAuthenticateToken(authSession, tokenAuth);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return new AuthenticationToken("invalid token");
                }
            } else {
                return new AuthenticationToken("invalid token");
            }
        } else if (authentication instanceof AuthenticationToken authToken) {
            if (!authToken.isAuthenticated()) {
                throw new BadCredentialsException(authToken.getErrorMessage());
            } else {
                return authToken;
            }
        } else {
            throw new BadCredentialsException("invalid.token");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
