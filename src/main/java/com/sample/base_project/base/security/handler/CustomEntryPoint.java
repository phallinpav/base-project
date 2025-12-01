package com.sample.base_project.base.security.handler;

import com.sample.base_project.core.response.base.Result;
import com.sample.base_project.base.auth.PubAuthContext;
import com.sample.base_project.common.utils.common.JsonUtils;
import com.sample.base_project.core.response.base.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Result<Object> result = Result.of(ResultCode.UNAUTHORIZE);
        response.getWriter().write(JsonUtils.toJson(Result.class, result));
        PubAuthContext.remove();
    }

}
