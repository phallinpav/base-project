package com.sample.base_project.common.base;

import com.sample.base_project.common.base.utils.BaseOSSUtils;

public interface BaseStorageEntity extends BaseEntity {
    String getPath();
    BaseOSSUtils ossUtils();
}
