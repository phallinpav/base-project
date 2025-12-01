package com.sample.base_project.base.auth;


import java.util.Optional;

public class PubAuthContext {
    private static final ThreadLocal<PubAuth> context = new ThreadLocal<>();

    public static void set(PubAuth user) {
        context.set(user);
    }

    public static PubAuth get() {
        return Optional.ofNullable(context.get()).orElseGet(() -> {
            PubAuth auth = new PubAuth();
            set(auth);
            return auth;
        });
    }

    public static void remove() {
        context.remove();
    }

    public static void setAuth(Object auth) {
        get().setAuth(auth);
    }

    public static Optional<Object> getAuth() {
        return Optional.ofNullable(get().getAuth());
    }

    public static void setAuthUuid(String authUuid) {
        get().setAuthUuid(authUuid);
    }

    public static Optional<String> getAuthUuid() {
        return Optional.ofNullable(get().getAuthUuid());
    }

    public static void setIpAddress(String ipAddress) {
        get().setIpAddress(ipAddress);
    }
    public static Optional<String> getIpAddress() {
        return Optional.ofNullable(get().getIpAddress());
    }

    public static void setToken(String token) {
        get().setToken(token);
    }
    public static Optional<String> getToken() {
        return Optional.ofNullable(get().getToken());
    }
}
