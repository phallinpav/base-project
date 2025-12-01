package com.sample.base_project.core.repository;

import com.sample.base_project.base.exception.ErrorModule;
import com.sample.base_project.common.base.repository.BaseJpaRepository;
import com.sample.base_project.common.exception.IErrorModule;
import com.sample.base_project.core.model.User;

public interface UserRepository extends BaseJpaRepository<User> {

    @Override
    default IErrorModule getErrorModule() {
        return ErrorModule.USER;
    }
}
