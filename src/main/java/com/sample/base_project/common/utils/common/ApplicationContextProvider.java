package com.sample.base_project.common.utils.common;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

// IMPORTANT: avoid using this unless really needed, or have no option
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> beanClass) {
        if (beanClass != null && applicationContext != null) {
            return applicationContext.getBeansOfType(beanClass);
        } else {
            return null;
        }
    }
}
