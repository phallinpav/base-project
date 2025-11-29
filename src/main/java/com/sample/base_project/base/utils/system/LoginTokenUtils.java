package com.sample.base_project.base.utils.system;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginTokenUtils {

    public String generateToken(String uuid) {
        return System.currentTimeMillis() % 1000 + "|" + uuid;
    }

    public String getUserSessionKey(String token) {
        return "user:" + token;
    }

    public String getDevSessionKey(String token) {
        return "dev:" + token;
    }

    public enum SessionType {
        USER,
        DEV,
    }

    public record AuthSession(String authUuid) {}

    public @Nullable AuthSession getUserAuthSession(String token) {
        return getAuthSession(token, SessionType.USER);
    }

    public @Nullable AuthSession getDevAuthSession(String token) {
        return getAuthSession(token, SessionType.DEV);
    }

    public @Nullable AuthSession getAuthSession(String token, SessionType type) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String[] split = token.split("\\|");
        if (split.length < 2) {
            return null;
        }

        String authUuid;
        try {
            authUuid = split[1];
//            String session = switch (type) {
//                case USER -> userSessionUtils.get(getUserSessionKey(token));
//                case DEV -> userSessionUtils.get(getDevSessionKey(token));
//            };
//            if (session == null) {
//                return null;
//            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return new AuthSession(authUuid);
    }
}
