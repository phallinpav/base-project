package com.sample.base_project.common.base.service;

import com.sample.base_project.common.base.BaseStorageEntity;
import com.sample.base_project.common.base.utils.BaseOSSUtils;
import org.springframework.lang.Nullable;

public interface BaseStorageService<S extends BaseStorageEntity, ID> extends BaseService<S, ID> {
    S addFile(String path, BaseOSSUtils ossUtils);
    @Nullable S getUpdatedIcon(@Nullable String updatedIcon, @Nullable S currentIcon, BaseOSSUtils ossUtils);
}
