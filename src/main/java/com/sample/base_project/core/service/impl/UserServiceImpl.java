package com.sample.base_project.core.service.impl;

import com.sample.base_project.common.base.repository.BaseRepository;
import com.sample.base_project.common.base.service.BaseServiceImpl;
import com.sample.base_project.common.utils.common.MapEntityUtils;
import com.sample.base_project.core.constants.CurrencyEnum;
import com.sample.base_project.core.model.Account;
import com.sample.base_project.core.model.User;
import com.sample.base_project.core.repository.UserRepository;
import com.sample.base_project.core.request.CreateAccountRequest;
import com.sample.base_project.core.request.GetAccountRequest;
import com.sample.base_project.core.service.AccountService;
import com.sample.base_project.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {
    private final UserRepository repository;
    private final AccountService accountService;

    @Override
    protected BaseRepository<User, String> getRepository() {
        return repository;
    }

    @Override
    protected List<User> mapRef(List<User> entities) {
        MapEntityUtils.setMapAllParam(entities,
                MapEntityUtils.MapParam.ofList(
                        entities,
                        (userUuids) -> accountService.getList(GetAccountRequest.builder()
                                        .userUuids(userUuids)
                                .build()),
                        User::getUuid,
                        User::setAccounts,
                        Account::getUserUuid)
        );
        return entities;
    }

    @Override
    protected User postCreate(User entity) {
        var accountKHR = CreateAccountRequest.builder()
                .userUuid(entity.getUuid())
                .currency(CurrencyEnum.KHR.getValue())
                .build();
        var accountUSD = CreateAccountRequest.builder()
                .userUuid(entity.getUuid())
                .currency(CurrencyEnum.USD.getValue())
                .build();
        accountService.create(accountKHR);
        accountService.create(accountUSD);
        return super.postCreate(entity);
    }
}
