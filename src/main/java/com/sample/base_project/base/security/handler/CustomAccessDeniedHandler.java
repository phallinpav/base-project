package com.sample.base_project.base.security.handler;

import com.sample.base_project.core.response.base.Result;
import com.sample.base_project.base.auth.PubAuthContext;
import com.sample.base_project.common.utils.common.JsonUtils;
import com.sample.base_project.core.response.base.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Result<Object> result = Result.of(ResultCode.FORBIDDEN);
        response.getWriter().write(JsonUtils.toJson(Result.class, result));
        PubAuthContext.remove();
    }
}
