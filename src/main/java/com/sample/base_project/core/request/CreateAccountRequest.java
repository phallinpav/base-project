package com.sample.base_project.core.request;

import com.sample.base_project.common.base.view.CreateBaseParam;
import com.sample.base_project.core.mapper.AccountMapper;
import com.sample.base_project.core.model.Account;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest extends CreateBaseParam<Account> {

    @NotBlank
    private String userUuid;
    @NotBlank
    private String currency;

    @Override
    public Account toEntity() {
        return AccountMapper.INSTANCE.toEntity(this);
    }
}
