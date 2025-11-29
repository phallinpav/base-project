package com.sample.base_project.common.base.view;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.base.BaseStorageEntity;
import com.sample.base_project.common.base.utils.BaseOSSUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Data
@SuperBuilder
@NoArgsConstructor
public class EntityStorageParam<T extends BaseEntity, S extends BaseStorageEntity> implements Serializable {
    protected Function<T, S> getStorageFunction;
    protected BiConsumer<T, S> setStorageFunction;
    protected String storagePath;

    protected BaseOSSUtils ossUtils;

    public static <T extends BaseEntity, S extends BaseStorageEntity> EntityStorageParam<T, S> of(
            Function<T, S> getStorageFunction,
            BiConsumer<T, S> setStorageFunction,
            String storagePath,
            BaseOSSUtils ossUtils
    ) {
        return EntityStorageParam.<T, S>builder()
                .getStorageFunction(getStorageFunction)
                .setStorageFunction(setStorageFunction)
                .storagePath(storagePath)
                .ossUtils(ossUtils)
                .build();
    }
}
