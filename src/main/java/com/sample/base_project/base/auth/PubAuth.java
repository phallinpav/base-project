package com.sample.base_project.base.auth;

import lombok.Data;

import java.io.Serializable;

@Data
public class PubAuth implements Serializable {

    private Object auth;

    private String authUuid;

    private String ipAddress;

    private String token;

}
