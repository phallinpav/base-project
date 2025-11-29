package com.sample.base_project.common.base.repository;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.exception.IErrorModule;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface BaseRepository<T extends BaseEntity, ID> extends ListCrudRepository<T, ID>, ListPagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    IErrorModule getErrorModule();
}
