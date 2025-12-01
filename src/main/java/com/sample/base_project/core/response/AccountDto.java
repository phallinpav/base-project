package com.sample.base_project.core.response;

import lombok.Data;

@Data
public class AccountDto {
    private String uuid;
    private String currency;
    private String balance;
}
