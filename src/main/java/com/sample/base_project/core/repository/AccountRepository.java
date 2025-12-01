package com.sample.base_project.core.repository;

import com.sample.base_project.base.exception.ErrorModule;
import com.sample.base_project.common.base.repository.BaseJpaRepository;
import com.sample.base_project.common.exception.IErrorModule;
import com.sample.base_project.core.model.Account;
import com.sample.base_project.core.model.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends BaseJpaRepository<Account> {

    @Override
    default IErrorModule getErrorModule() {
        return ErrorModule.ACCOUNT;
    }

}
