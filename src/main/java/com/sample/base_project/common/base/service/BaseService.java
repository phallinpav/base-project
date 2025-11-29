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

public interface BaseService<T extends BaseEntity, ID> {
    <P extends CreateBaseParam<T>> T create(P param);
    <P extends UpdateBaseParam<T, ID>> T update(P param);
    <R> void delete(ID id);
    <R> @Deprecated void delete(DeleteBaseParam<ID> param);
    <R> void delete(GetBaseParam<ID> param);
    <R> void delete(GetBaseOneParam<T> param);
    <R> void deleteAllByIds(Collection<ID> ids);
    <R> long deleteAll(GetBaseListParam<T> param);
    T get(GetBaseParam<ID> param);
    T get(GetBaseOneParam<T> param);
    T get(ID id);
    List<T> getList(GetBaseListParam<T> param);
    List<T> getList(Collection<ID> ids);
    PaginationResponse<T> getPage(GetBasePageParam<T> param);
    long count(GetBaseListParam<T> param);
}
