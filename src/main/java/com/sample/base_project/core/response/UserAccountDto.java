package com.sample.base_project.core.response;

import com.sample.base_project.core.response.base.EnumDto;
import lombok.Data;

import java.util.List;

@Data
public class UserAccountDto {
    private String uuid;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private EnumDto status;
    private List<AccountDto> accounts;
}
