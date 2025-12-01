package com.sample.base_project.core.response;

import com.sample.base_project.core.response.base.EnumDto;
import lombok.Data;

@Data
public class UserDto {
    private String uuid;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneCode;
    private String phoneNum;
    private EnumDto status;
}
