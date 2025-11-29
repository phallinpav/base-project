package com.sample.base_project.base.config;

import com.sample.base_project.base.interceptor.BaseInterceptor;
import com.sample.base_project.base.interceptor.SetIpInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final SetIpInterceptor setIpInterceptor;
    private final BaseInterceptor baseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(setIpInterceptor);
        registry.addInterceptor(baseInterceptor)
                .excludePathPatterns(BaseInterceptor.EXCLUDE_PATH_PATTERNS);
    }

}
