package com.sample.base_project.base.auth;

import com.sample.base_project.core.model.User;

public class AuthUtils {
    public static String getAuthUuid() {
        return PubAuthContext.getAuthUuid().orElse(null);
    }
    public static User getUser() {
        return PubAuthContext.getAuth().map(User.class::cast).orElse(null);
    }
}
