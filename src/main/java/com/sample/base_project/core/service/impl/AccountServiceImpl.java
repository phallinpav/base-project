package com.sample.base_project.core.service.impl;

import com.sample.base_project.common.base.repository.BaseRepository;
import com.sample.base_project.common.base.service.BaseServiceImpl;
import com.sample.base_project.core.model.Account;
import com.sample.base_project.core.model.User;
import com.sample.base_project.core.repository.AccountRepository;
import com.sample.base_project.core.repository.UserRepository;
import com.sample.base_project.core.service.AccountService;
import com.sample.base_project.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AccountServiceImpl extends BaseServiceImpl<Account, String> implements AccountService {
    private final AccountRepository repository;

    @Override
    protected BaseRepository<Account, String> getRepository() {
        return repository;
    }
}
