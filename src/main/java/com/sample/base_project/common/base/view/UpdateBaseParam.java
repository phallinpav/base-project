package com.sample.base_project.common.base.view;

import com.sample.base_project.common.base.BaseEntity;

import java.io.Serializable;
import java.util.Set;

public abstract class UpdateBaseParam<T extends BaseEntity, ID> extends AuthParam implements Serializable {
    public abstract ID getUuid();
    public abstract T toEntity();
    public Set<String> getUpdateNullFields() {
        return null;
    }

    public boolean isMapRef() {
        return true;
    }

    public Set<String> getMapKeyInclude() {
        return null;
    }

    public Set<String> getMapKeyExclude() {
        return null;
    }
}
