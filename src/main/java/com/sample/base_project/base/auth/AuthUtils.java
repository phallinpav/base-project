package com.sample.base_project.base.auth;

//import com.hound.microinterface.models.postgresql.entity.Developer;
//import com.hound.microinterface.models.postgresql.entity.DeveloperAppKey;
//import com.hound.microinterface.models.postgresql.entity.User;

public class AuthUtils {
    public static Long getAuthUuid() {
        return PubAuthContext.getAuthUuid().orElse(null);
    }
//    public static User getUser() {
//        return PubAuthContext.getAuth().map(User.class::cast).orElse(null);
//    }
//    public static Developer getDeveloper() {
//        return PubAuthContext.getAuth().map(Developer.class::cast).orElse(null);
//    }
//    public static DeveloperAppKey getDevAppKey() {
//        return PubAuthContext.getAuth().map(DeveloperAppKey.class::cast).orElse(null);
//    }
}
