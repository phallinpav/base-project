package com.sample.base_project.common.base.view;

import com.sample.base_project.common.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class BaseAuthParam<T extends BaseEntity> implements IAuthParam {
    private Long authUuid;
    private Enum<?> authType;
    private boolean checkAuth;
    private Map<String, Object> mapAuthObject;
    private Set<String> history;

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
