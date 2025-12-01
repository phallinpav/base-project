package com.sample.base_project.core.mapper;

import com.sample.base_project.common.exception.ErrorMessageUtils;
import com.sample.base_project.common.exception.ErrorType;
import com.sample.base_project.common.utils.common.FormatUtils;
import com.sample.base_project.core.constants.CurrencyEnum;
import com.sample.base_project.core.constants.base.BaseEnum;
import com.sample.base_project.core.model.Account;
import com.sample.base_project.core.model.User;
import com.sample.base_project.core.request.CreateAccountRequest;
import com.sample.base_project.core.request.CreateUserRequest;
import com.sample.base_project.core.request.UpdateUserRequest;
import com.sample.base_project.core.response.AccountDto;
import com.sample.base_project.core.response.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account toEntity(CreateAccountRequest request);

    @Mappings({
            @Mapping(source = ".", target = "balance", qualifiedByName = "convertBalance"),
    })
    AccountDto toDto(Account account);

    @Named("convertBalance")
    default String convertBalance(Account account) {
        CurrencyEnum currencyEnum = BaseEnum.fromValue(CurrencyEnum.class, account.getCurrency());
        return FormatUtils.formatCurrency(currencyEnum.getSymbol(), 2, account.getBalance());
    }
}
