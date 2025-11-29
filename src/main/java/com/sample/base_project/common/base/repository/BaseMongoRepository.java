package com.sample.base_project.common.base.repository;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.exception.ErrorMessageUtils;
import com.sample.base_project.common.exception.IErrorModule;
import com.sample.base_project.common.utils.common.ApplicationContextProvider;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BaseMongoRepository<T extends BaseEntity> extends MongoRepository<T, String>, BaseRepository<T, String> {

    default T findByIdNotNull(String id) {
        return findById(id).orElseThrow(() -> ErrorMessageUtils.notFound(getErrorModule()));
    }

    default MongoTemplate getMongoTemplate() {
        return ApplicationContextProvider.getBean(MongoTemplate.class);
    }

    Class<T> getEntityClass();

    IErrorModule getErrorModule();
}
