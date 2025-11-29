package com.sample.base_project.common.base.view;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AuthParam implements IAuthParam {
    protected Long authUuid;
    protected Enum<?> authType;
    protected boolean checkAuth;
    protected Map<String, Object> mapAuthObject;
    protected Set<String> history;

    public void setAuthParam(IAuthParam param) {
        if (param != null) {
            this.authUuid = param.getAuthUuid();
            this.authType = param.getAuthType();
            this.checkAuth = param.isCheckAuth();
            this.mapAuthObject = param.getMapAuthObject();
            this.history = param.getHistory();
        }
    }
}
