package com.sample.base_project.core.security.filter;

import com.sample.base_project.base.security.constant.EndpointConstant;
import com.sample.base_project.base.security.filter.TokenAuthFilter;
import com.sample.base_project.base.security.provider.TokenAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@DependsOn("endpointConstant")
public class UserTokenAuthFilter extends TokenAuthFilter {
    private static final String HEADER_TOKEN = "S-User-Token";

    @Autowired
    public UserTokenAuthFilter(TokenAuthenticationProvider provider) {
        super(provider);
    }

    @Override
    public String getHeaderToken() {
        return HEADER_TOKEN;
    }

    @Override
    public RequestMatcher getExcludeRequestMatcher() {
        List<RequestMatcher> listExclude = new ArrayList<>();
        for (var path : EndpointConstant.PUBLIC_LIST) {
            listExclude.add(new AntPathRequestMatcher(path));
        }
        for (var path : EndpointConstant.CUSTOM_PUBLIC_LIST) {
            listExclude.add(new AntPathRequestMatcher(path));
        }
        return new OrRequestMatcher(listExclude);
    }

    @Override
    public RequestMatcher getPubOrAuthRequestMatcher() {
        List<RequestMatcher> listMatcher = new ArrayList<>();
        for (var path : EndpointConstant.PUBLIC_OR_AUTH_LIST) {
            listMatcher.add(new AntPathRequestMatcher(path));
        }
        if (listMatcher.isEmpty()) {
            // FIXME: This is a temporary solution to avoid empty list
            listMatcher.add(new AntPathRequestMatcher("/empty"));
        }
        return new OrRequestMatcher(listMatcher);
    }
}
