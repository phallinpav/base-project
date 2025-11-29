package com.sample.base_project.common.base.service;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.base.view.CreateBaseParam;
import com.sample.base_project.common.base.view.DeleteBaseParam;
import com.sample.base_project.common.base.view.GetBaseListParam;
import com.sample.base_project.common.base.view.GetBaseOneParam;
import com.sample.base_project.common.base.view.GetBasePageParam;
import com.sample.base_project.common.base.view.GetBaseParam;
import com.sample.base_project.common.base.view.UpdateBaseParam;
import com.sample.base_project.common.utils.paging.PaginationResponse;

import java.util.Collection;
import java.util.List;

public abstract class PubBaseServiceImpl<T extends BaseEntity, ID> implements BaseService<T, ID> {

    protected abstract <BS extends BaseService<T, ID>> BS getBaseService();

    @Override
    public <P extends CreateBaseParam<T>> T create(P param) {
        return getBaseService().create(param);
    }

    @Override
    public <P extends UpdateBaseParam<T, ID>> T update(P param) {
        return getBaseService().update(param);
    }

    @Override
    public <R> void delete(ID uuid) {
        getBaseService().delete(uuid);
    }

    @Deprecated
    @Override
    public <R> void delete(DeleteBaseParam<ID> param) {
        getBaseService().delete(param);
    }

    @Override
    public <R> void delete(GetBaseParam<ID> param) {
        getBaseService().delete(param);
    }

    @Override
    public <R> void delete(GetBaseOneParam<T> param) {
        getBaseService().delete(param);
    }

    @Override
    public <R> void deleteAllByIds(Collection<ID> ids) {
        getBaseService().deleteAllByIds(ids);
    }

    @Override
    public <R> long deleteAll(GetBaseListParam<T> param) {
        return getBaseService().deleteAll(param);
    }

    @Override
    public T get(GetBaseParam<ID> param) {
        return getBaseService().get(param);
    }

    @Override
    public T get(GetBaseOneParam<T> param) {
        return getBaseService().get(param);
    }

    @Override
    public T get(ID uuid) {
        return getBaseService().get(uuid);
    }

    @Override
    public List<T> getList(GetBaseListParam<T> param) {
        return getBaseService().getList(param);
    }

    @Override
    public List<T> getList(Collection<ID> uuids) {
        return getBaseService().getList(uuids);
    }

    @Override
    public PaginationResponse<T> getPage(GetBasePageParam<T> param) {
        return getBaseService().getPage(param);
    }

    @Override
    public long count(GetBaseListParam<T> param) {
        return getBaseService().count(param);
    }
}
