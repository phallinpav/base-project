package com.sample.base_project.base.security.filter;

import com.sample.base_project.core.response.base.Result;
import com.sample.base_project.common.utils.common.JsonUtils;
import com.sample.base_project.base.security.model.TokenAuth;
import com.sample.base_project.base.security.provider.TokenAuthenticationProvider;
import com.sample.base_project.common.exception.BusinessException;
import com.sample.base_project.core.response.base.ResultCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

public abstract class TokenAuthFilter extends AbstractPreAuthenticatedProcessingFilter {
    private final RequestMatcher excludeRequestMatcher;
    private final RequestMatcher pubOrAuthRequestMatcher;

    public abstract String getHeaderToken();

    public abstract RequestMatcher getExcludeRequestMatcher();

    public abstract RequestMatcher getPubOrAuthRequestMatcher();

    @Autowired
    public TokenAuthFilter(TokenAuthenticationProvider provider) {
        this.setAuthenticationManager(new ProviderManager(provider));
        this.excludeRequestMatcher = getExcludeRequestMatcher();
        this.pubOrAuthRequestMatcher = getPubOrAuthRequestMatcher();
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String token = request.getHeader(getHeaderToken());
        if (pubOrAuthRequestMatcher.matches(request)) {
            return new TokenAuth(token, true);
        } else {
            return new TokenAuth(token);
        }
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            if (!excludeRequestMatcher.matches(httpRequest)) {
                super.doFilter(httpRequest, httpResponse, chain);
            } else {
                chain.doFilter(httpRequest, httpResponse);
            }
        } catch (BusinessException e) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            Result<Object> result = Result.of(ResultCode.UNAUTHORIZE);
            httpResponse.getWriter().write(JsonUtils.toJson(Result.class, result));
        }
    }
}
