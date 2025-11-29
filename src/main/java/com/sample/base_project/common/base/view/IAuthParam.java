package com.sample.base_project.common.base.view;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface IAuthParam {
    Long getAuthUuid();
    Enum<?> getAuthType();
    boolean isCheckAuth();
    Map<String, Object> getMapAuthObject();
    Set<String> getHistory();


    void setAuthUuid(Long val);
    void setAuthType(Enum<?> val);
    void setCheckAuth(boolean val);
    void setMapAuthObject(Map<String, Object> map);
    void setHistory(Set<String> history);

    default String toAuthString() {
        return "IAuthParam{authUuid=%s, authType=%s, checkAuth=%s, mapAuthObject=%s}"
                .formatted(getAuthUuid(), getAuthType(), isCheckAuth(), getMapAuthObject());
    }

    default void loopAuthPrevention(String key) {
        if (isCheckAuth()) {
            var history = getHistory();
            if (history == null) {
                history = new HashSet<>();
                setHistory(history);
            }
            var item = key + this.getClass().getName() + ":" + toAuthString();
            if (!history.add(item)) {
                setCheckAuth(false);
            }
        }
    }
}
