package com.sample.base_project.core.security.provider;

import com.sample.base_project.base.auth.AuthType;
import com.sample.base_project.base.auth.PubAuthContext;
import com.sample.base_project.base.security.model.AuthModel;
import com.sample.base_project.base.security.model.AuthenticationToken;
import com.sample.base_project.base.security.model.TokenAuth;
import com.sample.base_project.base.security.provider.TokenAuthenticationProvider;
import com.sample.base_project.base.utils.system.LoginTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserTokenAuthenticationProvider extends TokenAuthenticationProvider {
//    private final BaseUserService userService;

    @Override
    public LoginTokenUtils.SessionType sessionType() {
        return LoginTokenUtils.SessionType.USER;
    }

    @Override
    public AuthenticationToken customAuthenticateToken(LoginTokenUtils.AuthSession authSession, TokenAuth tokenAuth) {
//        if (authSession == null || StringUtils.isBlank(authSession.authUuid()) || authSession.authUuid().equals("null")) {
//            if (tokenAuth != null && tokenAuth.allowAnonymous()) {
//                return new AuthenticationToken(new AuthModel(null, AuthType.ANONYMOUS), tokenAuth.token(), new ArrayList<>());
//            }
//            return new AuthenticationToken("invalid token");
//        }
//        PubAuthContext.setAuthUuid(authSession.authUuid());
//        long userUuid = Long.parseLong(authSession.authUuid());
//        User user = userService.get(userUuid);

        List<GrantedAuthority> authorities = new ArrayList<>();

//        PubAuthContext.setAuth(user);
        PubAuthContext.setToken(tokenAuth.token());
        return new AuthenticationToken(new AuthModel(1L, AuthType.USER), tokenAuth.token(), authorities);
    }

}
