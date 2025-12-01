package com.sample.base_project.base.interceptor;

import com.sample.base_project.base.constant.BaseConstant;
import com.sample.base_project.base.utils.common.HttpRequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BaseInterceptor implements HandlerInterceptor {
    private final BaseConstant baseConstant;

    public static final List<String> EXCLUDE_PATH_PATTERNS = new ArrayList<>();

    static {
        EXCLUDE_PATH_PATTERNS.add("/error/**");
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        // Anything that want to do here before go to controller
        HttpRequestUtils.setRequest(request);
        HttpRequestUtils.setResponse(response);
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           ModelAndView modelAndView) {
        HttpRequestUtils.clear();
        // Anything that want to do here after everything process is complete
    }
}
