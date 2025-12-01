package com.sample.base_project.common.base.repository;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.exception.ErrorMessageUtils;
import com.sample.base_project.common.exception.IErrorModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BaseJpaRepository<T extends BaseEntity> extends JpaRepository<T, String>, JpaSpecificationExecutor<T>, BaseRepository<T, String> {

    Optional<T> findByUuid(String uuid);

    IErrorModule getErrorModule();
}
