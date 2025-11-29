package com.sample.base_project.webConfig;

import com.sample.base_project.base.security.constant.EndpointConstant;
import com.sample.base_project.base.security.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@DependsOn("endpointConstant")
@EnableWebSecurity
public class UserSecurityConfig extends SecurityConfig {
    @Override
    public void setAuthorizeEndpoint(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            for (var set : EndpointConstant.PERMISSION_LIST.entrySet()) {
                for (var val : set.getValue()) {
                    auth.requestMatchers(val.getFirst()).hasAuthority(val.getSecond());
                }
            }

            for (var path : EndpointConstant.PUBLIC_LIST) {
                auth.requestMatchers(path).permitAll();
            }

            for (var path : EndpointConstant.CUSTOM_PUBLIC_LIST) {
                auth.requestMatchers(path).permitAll();
            }
        });
    }
}
