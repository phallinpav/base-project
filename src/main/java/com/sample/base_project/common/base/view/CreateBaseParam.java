package com.sample.base_project.common.base.view;

import com.sample.base_project.common.base.BaseEntity;

import java.io.Serializable;
import java.util.Set;

public abstract class CreateBaseParam<T extends BaseEntity> extends AuthParam implements Serializable {

    public abstract T toEntity();

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
