package com.sample.base_project.common.base.service;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.base.repository.BaseJpaRepository;
import com.sample.base_project.common.base.repository.BaseMongoRepository;
import com.sample.base_project.common.base.repository.BaseRepository;
import com.sample.base_project.common.base.utils.BaseEntityUtils;
import com.sample.base_project.common.base.view.CreateBaseParam;
import com.sample.base_project.common.base.view.CreateEntityWithStorageParam;
import com.sample.base_project.common.base.view.DeleteBaseParam;
import com.sample.base_project.common.base.view.GetBaseListParam;
import com.sample.base_project.common.base.view.GetBaseOneParam;
import com.sample.base_project.common.base.view.GetBasePageParam;
import com.sample.base_project.common.base.view.GetBaseParam;
import com.sample.base_project.common.base.view.IAuthParam;
import com.sample.base_project.common.base.view.UpdateBaseParam;
import com.sample.base_project.common.base.view.UpdateEntityWithStorageParam;
import com.sample.base_project.common.exception.ErrorMessageUtils;
import com.sample.base_project.common.utils.paging.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public abstract class BaseServiceImpl<T extends BaseEntity, ID> implements BaseService<T, ID> {

    protected abstract BaseRepository<T, ID> getRepository();

    protected T getOldCloneEntity() {
        return null;
    }

    protected <P extends UpdateBaseParam<T, ID>> Set<String> getUpdateNullFields(P param) {
        return param.getUpdateNullFields();
    }

    private boolean methodMapRef1IsOverride() {
        try {
            Method parentMethod = BaseServiceImpl.class.getDeclaredMethod("mapRef", List.class);
            Method childMethod = this.getClass().getDeclaredMethod("mapRef", List.class);
            parentMethod.setAccessible(true);
            childMethod.setAccessible(true);

            return !parentMethod.equals(childMethod);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private boolean methodMapRef3IsOverride() {
        try {
            Method parentMethod = BaseServiceImpl.class.getDeclaredMethod("mapRef", List.class, List.class, List.class);
            Method childMethod = this.getClass().getDeclaredMethod("mapRef", List.class, List.class, List.class);
            parentMethod.setAccessible(true);
            childMethod.setAccessible(true);

            return !parentMethod.equals(childMethod);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    protected Set<String> getExcludedSelfMapKey() {
        return new HashSet<>();
    }

    protected Set<String> combineMapKeyExcluded(Set<String> mapKeyExclude) {
        Set<String> combine = new HashSet<>();
        if (mapKeyExclude != null && !mapKeyExclude.isEmpty()) {
            combine.addAll(mapKeyExclude);
        }
        var excludedSelfMapKey = getExcludedSelfMapKey();
        if (excludedSelfMapKey != null && !excludedSelfMapKey.isEmpty()) {
            combine.addAll(excludedSelfMapKey);
        }
        return combine;
    }

    protected List<T> mapRef(List<T> entities, Set<String> mapKeyInclude, Set<String> mapKeyExclude) {
        boolean mapRef1 = methodMapRef1IsOverride();
        boolean mapRef3 = methodMapRef3IsOverride();
        if (mapRef1 && mapRef3) {
            return entities;
        } else if (mapRef1) {
            mapRef(entities);
        }
        return entities;
    }

    protected List<T> mapRef(List<T> entities) {
        boolean mapRef1 = methodMapRef1IsOverride();
        boolean mapRef3 = methodMapRef3IsOverride();
        if (mapRef1 && mapRef3) {
            return entities;
        } else if (mapRef1) {
            mapRef(entities, null, null);
        }
        return entities;
    }

    protected T mapRef(T entity, Set<String> mapKeyInclude, Set<String> mapKeyExclude) {
        if (entity == null) return null;
        return mapRef(List.of(entity), mapKeyInclude, mapKeyExclude).get(0);
    }

    protected T mapRef(T entity) {
        return mapRef(entity, null, null);
    }

    protected <P extends CreateBaseParam<T>> T preCreate(P param) {
        return param.toEntity();
    }

    protected <P extends CreateBaseParam<T>> T postCreate(T entity, P param) {
        var created = postCreate(entity);
        if (param.isMapRef()) {
            return mapRef(created, param.getMapKeyInclude(), combineMapKeyExcluded(param.getMapKeyExclude()));
        }
        return created;
    }

    protected T postCreate(T entity) {
        return entity;
    }

    protected <P extends UpdateBaseParam<T, ID>> T preUpdate(T currentEntity, P param) {
        return param.toEntity();
    }

    protected <P extends UpdateBaseParam<T, ID>> T postUpdate(T currentEntity, T updatedEntity, P param) {
        return postUpdate(currentEntity, updatedEntity);
    }

    protected T postUpdate(T currentEntity, T updatedEntity) {
        return currentEntity;
    }

    protected <R> R preDelete(T entity) {
        return null;
    }

    @Deprecated
    protected <P extends DeleteBaseParam<ID>, R> R preDelete(T entity, P param) {
        return preDelete(entity);
    }

    protected <P extends GetBaseParam<ID>, R> R preDelete(T entity, P param) {
        return preDelete(entity);
    }

    protected <P extends GetBaseOneParam<T>, R> R preDelete(T entity, P param) {
        return preDelete(entity);
    }

    protected <R> R preDeleteByIds(Collection<ID> ids) {
        return null;
    }

    protected <P extends GetBaseListParam<T>, R> R preDeleteAll(P param) {
        return null;
    }

    protected <R> void postDelete(T entity, R preDeleteResult) {
    }

    protected <R> void postDeleteByIds(Collection<ID> ids, R preDeleteResult) {
    }

    protected <P extends GetBaseListParam<T>, R> void postDeleteAll(P param, R preDeleteResult) {
    }

    protected <P extends GetBaseParam<ID>> void preGet(P param) {
    }

    protected <P extends GetBaseOneParam<T>> void preGetOne(P param) {
    }

    protected <P extends GetBaseListParam<T>> void preGetList(P param) {
    }

    protected <P extends GetBasePageParam<T>> void preGetPage(P param) {
    }

    protected <P extends GetBaseListParam<T>> void preCount(P param) {
    }

    protected <P extends GetBaseParam<ID>> T postGet(T entity, P param) {
        param.loopAuthPrevention("GET(%s)-(%s)".formatted(entity.getClass(), param.getUuid()));
        validateAuth(entity, param);
        return entity;
    }

    protected <P extends GetBaseOneParam<T>> T postGetOne(T entity, P param) {
        param.loopAuthPrevention("GET_ONE(%s)-(%s)".formatted(entity.getClass(), param.toSpecCom().toString()));
        validateAuth(entity, param);
        return entity;
    }

    protected <P extends GetBaseListParam<T>> List<T> postGetList(List<T> entities, P param) {
        return entities;
    }

    protected <P extends GetBasePageParam<T>> Page<T> postGetPage(Page<T> entities, P param) {
        return entities;
    }

    protected <P extends GetBaseListParam<T>> Long postCount(Long count, P param) {
        return count;
    }

    // this method is used in get info, update, delete
    protected void validateAuth(T entity, IAuthParam param) {
    }

    // this method in get list, get page, create, update, delete
    protected void validateAuth(IAuthParam param) {
    }

    @Override
    public <P extends CreateBaseParam<T>> T create(P param) {
        param.loopAuthPrevention("CREATE(%s)".formatted(param.toEntity().toString()));
        validateAuth(param);
        T entity = preCreate(param);
        BaseEntityUtils.createEntityWithStorage(CreateEntityWithStorageParam.of(
                entity,
                getSaveFunction()
        ));
        return postCreate(entity, param);
    }

    @Override
    public <P extends UpdateBaseParam<T, ID>> T update(P param) {
        T currentEntity = get(GetBaseParam.of(param.getUuid(), param, param.isMapRef(), false, param.getMapKeyInclude(), param.getMapKeyExclude()));
        T updateEntity = preUpdate(currentEntity, param);
        BaseEntityUtils.updateEntityWithStorage(UpdateEntityWithStorageParam.of(
                currentEntity,
                updateEntity,
                getOldCloneEntity(),
                getSaveFunction(),
                getUpdateNullFields(param)
        ));
        if (param.isMapRef()) {
            currentEntity = mapRef(currentEntity, param.getMapKeyInclude(), combineMapKeyExcluded(param.getMapKeyExclude()));
        }
        return postUpdate(currentEntity, updateEntity, param);
    }

    private Function<T, T> getSaveFunction() {
        if (getRepository() instanceof BaseJpaRepository) {
            BaseJpaRepository<T> repo = (BaseJpaRepository<T>) getRepository();
            return repo::saveAndFlush;
        }
        return getRepository()::save;
    }

    @Override
    public <R> void delete(ID id) {
        T entity = get(id);
        R preDeleteResult = preDelete(entity);
        getRepository().delete(entity);
        if (getRepository() instanceof BaseJpaRepository repo) {
            repo.flush();
        }
        postDelete(entity, preDeleteResult);
    }

    @Deprecated
    @Override
    public <R> void delete(DeleteBaseParam<ID> param) {
        T entity = get(GetBaseParam.of(param.getUuid(), param, false, false));
        R preDeleteResult = preDelete(entity, param);
        getRepository().delete(entity);
        if (getRepository() instanceof BaseJpaRepository repo) {
            repo.flush();
        }
        postDelete(entity, preDeleteResult);
    }

    @Override
    public <R> void delete(GetBaseParam<ID> param) {
        T entity = get(param);
        R preDeleteResult = preDelete(entity, param);
        getRepository().delete(entity);
        if (getRepository() instanceof BaseJpaRepository repo) {
            repo.flush();
        }
        postDelete(entity, preDeleteResult);
    }

    @Override
    public <R> void delete(GetBaseOneParam<T> param) {
        T entity = get(param);
        R preDeleteResult = preDelete(entity, param);
        getRepository().delete(entity);
        if (getRepository() instanceof BaseJpaRepository repo) {
            repo.flush();
        }
        postDelete(entity, preDeleteResult);
    }

    @Override
    public <R> void deleteAllByIds(Collection<ID> ids) {
        R preDeleteResult = preDeleteByIds(ids);
        getRepository().deleteAllById(ids);
        postDeleteByIds(ids, preDeleteResult);
    }

    @Override
    public <R> long deleteAll(GetBaseListParam<T> param) {
        param.loopAuthPrevention("DELETE_ALL(%s)".formatted(param.toSpecCom().toString()));
        validateAuth(param);
        R preDeleteResult = preDeleteAll(param);
        long deletedCount = 0;
        preDeleteAll(param);
        if (getRepository() instanceof BaseJpaRepository repo) {
            deletedCount = repo.delete(param.toSpec());
        } else if (getRepository() instanceof BaseMongoRepository repo) {
            var result = repo.getMongoTemplate().remove(param.toQuery(), repo.getEntityClass());
            deletedCount = result.getDeletedCount();
        }
        postDeleteAll(param, preDeleteResult);
        return deletedCount;
    }

    @Override
    @Transactional(readOnly = true)
    public T get(GetBaseParam<ID> param) {
        preGet(param);
        T entity;
        if (param.isNullable()) {
            entity = getRepository().findById(param.getUuid()).orElse(null);
        } else {
            entity = getRepository().findById(param.getUuid()).orElseThrow(() -> ErrorMessageUtils.notFound(getRepository().getErrorModule()));
        }
        if (param.isMapRef()) {
            entity = mapRef(entity, param.getMapKeyInclude(), combineMapKeyExcluded(param.getMapKeyExclude()));
        }
        return postGet(entity, param);
    }

    @Override
    @Transactional(readOnly = true)
    public T get(GetBaseOneParam<T> param) {
        preGetOne(param);
        T entity;
        if (getRepository() instanceof BaseJpaRepository) {
            BaseJpaRepository<T> repo = (BaseJpaRepository<T>) getRepository();
            entity = repo.findOne(param.toSpec()).orElse(null);
        } else if (getRepository() instanceof BaseMongoRepository) {
            BaseMongoRepository<T> repo = (BaseMongoRepository<T>) getRepository();
            entity = repo.getMongoTemplate().findOne(param.toQuery(), repo.getEntityClass());
        } else {
            throw ErrorMessageUtils.error("invalid.repository");
        }
        if (entity == null) {
            if (param.isNullable()) {
                return null;
            } else {
                throw ErrorMessageUtils.notFound(getRepository().getErrorModule());
            }
        }

        if (param.isMapRef()) {
            entity = mapRef(entity, param.getMapKeyInclude(), combineMapKeyExcluded(param.getMapKeyExclude()));
        }
        return postGetOne(entity, param);
    }

    @Override
    @Transactional(readOnly = true)
    public T get(ID id) {
        return getRepository().findById(id).orElseThrow(() -> ErrorMessageUtils.notFound(getRepository().getErrorModule()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getList(GetBaseListParam<T> param) {
        param.loopAuthPrevention("GET_LIST(%s)".formatted(param.toSpecCom().toString()));
        validateAuth(param);
        preGetList(param);
        List<T> entities = new ArrayList<>();
        if (param.isDisableSort()) {
            if (getRepository() instanceof BaseJpaRepository repo) {
                entities = repo.findAll(param.toSpec());
            } else if (getRepository() instanceof BaseMongoRepository repo) {
                entities = repo.getMongoTemplate().find(param.toQuery(), repo.getEntityClass());
            }
        } else {
            if (getRepository() instanceof BaseJpaRepository repo) {
                entities = repo.findAll(param.toSpec(), param.toSort());
            } else if (getRepository() instanceof BaseMongoRepository repo) {
                Query query = param.toQuery();
                query.with(param.toSort());
                entities = repo.getMongoTemplate().find(query, repo.getEntityClass());
            }
        }
        if (param.isMapRef()) {
            entities = mapRef(entities, param.getMapKeyInclude(), combineMapKeyExcluded(param.getMapKeyExclude()));
        }
        return postGetList(entities, param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getList(Collection<ID> uuids) {
        return getRepository().findAllById(uuids);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<T> getPage(GetBasePageParam<T> param) {
        param.loopAuthPrevention("GET_PAGE(%s)".formatted(param.toSpecCom().toString()));
        validateAuth(param);
        preGetPage(param);
        Page<T> pages;
        if (getRepository() instanceof BaseJpaRepository repo) {
            pages = repo.findAll(param.toSpec(), param.toPageable());
        } else if (getRepository() instanceof BaseMongoRepository repo) {
            var mongoTemplate = repo.getMongoTemplate();
            var pageRequest = param.toPageable();
            var query = param.toQuery();
            query.with(pageRequest);
            var clazz = repo.getEntityClass();
            List<T> entities = mongoTemplate.find(query, clazz);
            long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), clazz);
            pages = new PageImpl<>(entities, pageRequest, total);
        } else {
            throw ErrorMessageUtils.error("fail.getPage.invalid.repo");
        }

        if (param.isMapRef()) {
            pages = pages.map(val -> mapRef(val, param.getMapKeyInclude(), combineMapKeyExcluded(param.getMapKeyExclude())));
        }
        return PaginationResponse.of(postGetPage(pages, param));
    }

    @Override
    @Transactional(readOnly = true)
    public long count(GetBaseListParam<T> param) {
        param.loopAuthPrevention("COUNT(%s)".formatted(param.toSpecCom().toString()));
        validateAuth(param);
        preCount(param);
        long count = 0;
        if (getRepository() instanceof BaseJpaRepository repo) {
            count = repo.count(param.toSpec());
        } else if (getRepository() instanceof BaseMongoRepository repo) {
            count = repo.getMongoTemplate().count(param.toQuery(), repo.getEntityClass());
        }
        return postCount(count, param);
    }
}
