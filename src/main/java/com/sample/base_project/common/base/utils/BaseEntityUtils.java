package com.sample.base_project.common.base.utils;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.base.BaseStorageEntity;
import com.sample.base_project.common.base.view.CreateEntityWithStorageParam;
import com.sample.base_project.common.base.view.UpdateEntityWithStorageParam;
import com.sample.base_project.common.exception.ErrorMessageUtils;
import com.sample.base_project.common.utils.common.CustomBeanUtils;
import com.sample.base_project.common.utils.common.ObjectUtils;
import com.sample.base_project.common.utils.common.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BaseEntityUtils {

    public static <T extends BaseEntity, S extends BaseStorageEntity, ID> void createEntityWithStorage(CreateEntityWithStorageParam<T, S, ID> param) {
        var entityStorageParamList = param.getEntityStorageParamList();
        var storageService = param.getStorageService();
        var newEntity = param.getNewEntity();
        var saveFunction = param.getSaveFunction();

        if (entityStorageParamList == null) {
            entityStorageParamList = new ArrayList<>();
        }

        if (!entityStorageParamList.isEmpty() && storageService == null) {
            System.out.println("updateEntityWithStorage method argument storageService is null");
            throw ErrorMessageUtils.error("create.entity.fail");
        }

        for (var val : entityStorageParamList) {
            if (StringUtils.isNotBlank(val.getStoragePath())) {
                S icon = storageService.addFile(val.getStoragePath(), val.getOssUtils());
                val.getSetStorageFunction().accept(newEntity, icon);
            }
        }

        saveFunction.apply(newEntity);

        try {
            for (var val : entityStorageParamList) {
                if (StringUtils.isNotBlank(val.getStoragePath())) {
//                    val.getOssUtils().updateObjectTagToDone(val.getStoragePath());
                }
            }
        } catch (Exception e) {
            throw ErrorMessageUtils.error("create.entity.fail", e);
        }
    }

    public static <T extends BaseEntity, S extends BaseStorageEntity, ID> void updateEntityWithStorage(UpdateEntityWithStorageParam<T, S, ID> param) {
        var getSetUpdateStorageFunctions = param.getEntityStorageParamList();
        var storageService = param.getStorageService();
        var storageClass = param.getStorageClass();
        var currentEntity = param.getCurrentEntity();
        var updateEntity = param.getUpdateEntity();
        var oldCloneEntity = param.getOldCloneEntity();
        var updateNullFields = param.getUpdateNullFields();
        var saveFunction = param.getSaveFunction();

        if (getSetUpdateStorageFunctions == null) {
            getSetUpdateStorageFunctions = new ArrayList<>();
        }
        if (updateNullFields == null) {
            updateNullFields = new HashSet<>();
        }

        if (!getSetUpdateStorageFunctions.isEmpty() && (storageService == null || storageClass == null)) {
            System.out.println("updateEntityWithStorage method argument storageService or storageClass is null");
            throw ErrorMessageUtils.error("update.entity.fail");
        }
        List<SetStoragePathEntity<T, S>> listCurrent = new ArrayList<>();
        for (var func : getSetUpdateStorageFunctions) {
            S currentStorage = ObjectUtils.getNullSafe(func.getGetStorageFunction().apply(currentEntity));

            // Step here is to make sure storage is not linked to the current entity
            if (currentStorage != null) {
                try {
                    S storage = storageClass.getDeclaredConstructor().newInstance();
                    BeanUtils.copyProperties(currentStorage, storage);
                    currentStorage = storage;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            func.getSetStorageFunction().accept(currentEntity, currentStorage);
            listCurrent.add(new SetStoragePathEntity<>(func.getSetStorageFunction(), currentStorage, func.getStoragePath(), func.getOssUtils()));
        }

        Map<BiConsumer<T, S>, S> mapSetUpdatedIcons = new HashMap<>();
        for (var val : listCurrent) {
            S icon = storageService.getUpdatedIcon(val.storagePath(), val.storage(), val.ossUtils());
            val.setStorageFunction().accept(updateEntity, icon);
            mapSetUpdatedIcons.put(val.setStorageFunction(), icon);
        }

        try {
            // FIXME: still not the best solution but usable
            CustomBeanUtils.copyFieldNotNull(updateEntity, currentEntity, oldCloneEntity, updateNullFields);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw ErrorMessageUtils.error("copy.field.update.entity.fail", e);
        }

        for (var set : mapSetUpdatedIcons.entrySet()) {
            Optional.ofNullable(set.getValue()).ifPresent(val -> {
                if (val.get_id() == null) set.getKey().accept(currentEntity, null);
            });
        }

        saveFunction.apply(currentEntity);

        try {
            for (var val : listCurrent) {
                S currentIcon = val.storage();
//                val.ossUtils().handleUpdateObject(val.storagePath(), ObjectUtils.getNullSafe(currentIcon, S::getPath));
            }
        } catch (Exception e) {
            throw ErrorMessageUtils.error("update.entity.fail", e);
        }
    }

    record SetStoragePathEntity<T extends BaseEntity, S extends BaseStorageEntity>(
            BiConsumer<T, S> setStorageFunction,
            S storage,
            String storagePath,
            BaseOSSUtils ossUtils) {
    }
}
