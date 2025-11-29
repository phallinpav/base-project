package com.sample.base_project.common.base.view;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.base.BaseStorageEntity;
import com.sample.base_project.common.base.service.BaseStorageService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class UpdateEntityWithStorageParam<T extends BaseEntity, S extends BaseStorageEntity, ID> extends AuthParam implements Serializable {
    protected T currentEntity;
    protected T updateEntity;
    @Nullable
    protected T oldCloneEntity;
    protected Class<S> storageClass;
    protected BaseStorageService<S, ID> storageService;
    protected List<EntityStorageParam<T, S>> entityStorageParamList;
    protected Function<T, T> saveFunction;

    // Fields to be updated to null when updating
    // By default all updateEntity field that is null will keep the currentEntity value
    protected Set<String> updateNullFields;

    public static <T extends BaseEntity, S extends BaseStorageEntity, ID> UpdateEntityWithStorageParam<T, S, ID> of(
            T currentEntity,
            T updateEntity,
            T oldEntity,
            Class<S> storageClass,
            BaseStorageService<S, ID> storageService,
            List<EntityStorageParam<T, S>> entityStorageParamList,
            Function<T, T> saveFunction,
            Set<String> updateNullFields) {
        UpdateEntityWithStorageParam<T, S, ID> param = new UpdateEntityWithStorageParam<>();
        param.setCurrentEntity(currentEntity);
        param.setUpdateEntity(updateEntity);
        param.setOldCloneEntity(oldEntity);
        param.setStorageClass(storageClass);
        param.setStorageService(storageService);
        param.setEntityStorageParamList(entityStorageParamList);
        param.setSaveFunction(saveFunction);
        param.setUpdateNullFields(updateNullFields);
        return param;
    }

    public static <T extends BaseEntity, S extends BaseStorageEntity, ID> UpdateEntityWithStorageParam<T, S, ID> of(
            T currentEntity,
            T updateEntity,
            T oldEntity,
            Function<T, T> saveFunction,
            Set<String> updateNullFields) {
        return of(currentEntity, updateEntity, oldEntity, null, null, null, saveFunction, updateNullFields);
    }

    public static <T extends BaseEntity, S extends BaseStorageEntity, ID> UpdateEntityWithStorageParam<T, S, ID> of(
            T currentEntity,
            T updateEntity,
            Class<S> storageClass,
            BaseStorageService<S, ID> storageService,
            List<EntityStorageParam<T, S>> entityStorageParamList,
            Function<T, T> saveFunction) {
        return of(currentEntity, updateEntity, null, storageClass, storageService, entityStorageParamList, saveFunction, null);
    }

    public static <T extends BaseEntity, S extends BaseStorageEntity, ID> UpdateEntityWithStorageParam<T, S, ID> of(
            T currentEntity,
            T updateEntity,
            Function<T, T> saveFunction) {
        return of(currentEntity, updateEntity, null, null, null, null, saveFunction, null);
    }

    public static <T extends BaseEntity, S extends BaseStorageEntity, ID> UpdateEntityWithStorageParam<T, S, ID> of(
            T currentEntity,
            T updateEntity,
            Function<T, T> saveFunction,
            Set<String> updateNullFields) {
        return of(currentEntity, updateEntity, null, null, null, null, saveFunction, updateNullFields);
    }
}
