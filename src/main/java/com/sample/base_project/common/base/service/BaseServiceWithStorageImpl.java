package com.sample.base_project.common.base.service;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.base.BaseStorageEntity;
import com.sample.base_project.common.base.utils.BaseEntityUtils;
import com.sample.base_project.common.base.view.CreateBaseParam;
import com.sample.base_project.common.base.view.CreateEntityWithStorageParam;
import com.sample.base_project.common.base.view.EntityStorageParam;
import com.sample.base_project.common.base.view.GetBaseParam;
import com.sample.base_project.common.base.view.UpdateBaseParam;
import com.sample.base_project.common.base.view.UpdateEntityWithStorageParam;
import com.sample.base_project.common.exception.ErrorMessageUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseServiceWithStorageImpl<T extends BaseEntity, S extends BaseStorageEntity, ID> extends BaseServiceImpl<T, ID> {

    protected abstract BaseStorageService<S, ID> getStorageService();

    protected abstract <P extends CreateBaseParam<T>> List<EntityStorageParam<T, S>> getCreateEntityStorageParamList(P param);
    protected abstract <P extends UpdateBaseParam<T, ID>> List<EntityStorageParam<T, S>> getUpdateEntityStorageParamList(P param);

    protected abstract Class<S> getStorageClass();
    protected abstract Set<String> getStorageMapKeyList();

    @Override
    public <P extends CreateBaseParam<T>> T create(P param) {
        param.loopAuthPrevention("CREATE(%s)".formatted(param.toEntity().toString()));
        validateAuth(param);
        T entity = preCreate(param);

        if (getStorageService() == null) throw ErrorMessageUtils.error("storage service is null");

        BaseEntityUtils.createEntityWithStorage(CreateEntityWithStorageParam.of(
                entity,
                getStorageService(),
                getCreateEntityStorageParamList(param),
                getRepository()::save
        ));
        return postCreate(entity, param);
    }

    @Override
    public <P extends UpdateBaseParam<T, ID>> T update(P param) {
        Set<String> mapKeyIncludeList = new HashSet<>();
        var storageMapKeyList = getStorageMapKeyList();
        var paramMapKeyInclude = param.getMapKeyInclude();
        if (storageMapKeyList != null && !storageMapKeyList.isEmpty()) {
            mapKeyIncludeList.addAll(storageMapKeyList);
        }
        if (paramMapKeyInclude != null && !paramMapKeyInclude.isEmpty()) {
            mapKeyIncludeList.addAll(paramMapKeyInclude);
        }

        T currentEntity = get(GetBaseParam.of(param.getUuid(), param, true, false, mapKeyIncludeList, param.getMapKeyExclude()));
        T updateEntity = preUpdate(currentEntity, param);

        if (getStorageService() == null) throw ErrorMessageUtils.error("storage service is null");
        if (getStorageClass() == null) throw ErrorMessageUtils.error("storage class is null");

        BaseEntityUtils.updateEntityWithStorage(UpdateEntityWithStorageParam.of(
                currentEntity,
                updateEntity,
                getOldCloneEntity(),
                getStorageClass(),
                getStorageService(),
                getUpdateEntityStorageParamList(param),
                getRepository()::save,
                getUpdateNullFields(param)
        ));
        if (param.isMapRef()) {
            currentEntity = mapRef(currentEntity, param.getMapKeyInclude(), combineMapKeyExcluded(param.getMapKeyExclude()));
        }
        return postUpdate(currentEntity, updateEntity, param);
    }
}
