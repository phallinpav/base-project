package com.sample.base_project.core.request;

import com.sample.base_project.common.utils.validation.common.AllNullOrNotBlank;
import com.sample.base_project.common.utils.validation.common.MustHaveOnlyOne;
import com.sample.base_project.common.utils.validation.system.PhoneCode;
import com.sample.base_project.common.utils.validation.system.PhoneNum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MustHaveOnlyOne(fields = {"email", "phoneNum"})
@AllNullOrNotBlank(fields = {"phoneCode", "phoneNum"})
public class LoginRequest {

    @Email
    private String email;
    @PhoneCode
    private String phoneCode;
    @PhoneNum
    private String phoneNum;
    @NotBlank
    private String password;

    public GetUserRequest toGetUserRequest() {
        return GetUserRequest.builder()
                .email(email)
                .phoneCode(phoneCode)
                .phoneNum(phoneNum)
                .build();
    }
}
