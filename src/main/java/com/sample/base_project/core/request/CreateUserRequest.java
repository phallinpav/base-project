package com.sample.base_project.core.request;

import com.sample.base_project.common.base.view.CreateBaseParam;
import com.sample.base_project.common.utils.validation.system.PhoneCode;
import com.sample.base_project.common.utils.validation.system.PhoneNum;
import com.sample.base_project.common.utils.validation.system.UserPassword;
import com.sample.base_project.core.mapper.UserMapper;
import com.sample.base_project.core.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest extends CreateBaseParam<User> {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank @Email
    private String email;
    @PhoneCode
    private String phoneCode;
    @PhoneNum
    private String phoneNum;

    @NotBlank
    @UserPassword
    private String password;

    @Override
    public User toEntity() {
        return UserMapper.INSTANCE.toEntity(this);
    }
}
