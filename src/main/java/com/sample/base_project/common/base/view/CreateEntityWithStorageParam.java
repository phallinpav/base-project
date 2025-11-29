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
import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class CreateEntityWithStorageParam<T extends BaseEntity, S extends BaseStorageEntity, ID> extends AuthParam implements Serializable {
    protected T newEntity;
    @Nullable
    protected BaseStorageService<S, ID> storageService;
    @Nullable
    protected List<EntityStorageParam<T, S>> entityStorageParamList;
    protected Function<T, T> saveFunction;

    public static <T extends BaseEntity, S extends BaseStorageEntity, ID> CreateEntityWithStorageParam<T, S, ID> of(
            T newEntity,
            BaseStorageService<S, ID> storageService,
            List<EntityStorageParam<T, S>> entityStorageParamList,
            Function<T, T> saveFunction) {
        CreateEntityWithStorageParam<T, S, ID> param = new CreateEntityWithStorageParam<>();
        param.setNewEntity(newEntity);
        param.setStorageService(storageService);
        param.setEntityStorageParamList(entityStorageParamList);
        param.setSaveFunction(saveFunction);
        return param;
    }

    public static <T extends BaseEntity, S extends BaseStorageEntity, ID> CreateEntityWithStorageParam<T, S, ID> of(
            T newEntity,
            Function<T, T> saveFunction) {
        return of(newEntity, null, null, saveFunction);
    }
}
