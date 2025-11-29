package com.sample.base_project.common.base.aspect.validation;

import com.sample.base_project.common.base.view.IAuthParam;
import com.sample.base_project.common.utils.validation.utils.ValidationUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
public class PubServiceValidationAspect {

    @Around("execution(* *(..)) && (target(com.sample.base_project.common.base.service.BaseServiceImpl) || target(com.sample.base_project.common.base.service.BaseServiceWithStorageImpl))")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        Object[] args = joinPoint.getArgs();

        boolean validated = false;
        boolean skip = false;
        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation.annotationType().equals(ValidateParam.class)) {
                    ValidationUtils.validate(args[i]);
                    validated = true;
                    break;
                } else if (annotation.annotationType().equals(NoValidateParam.class)) {
                    skip = true;
                    break;
                }
            }
            if (validated || skip) {
                continue;
            }
            if (args[i] instanceof IAuthParam) {
                ValidationUtils.validate(args[i]);
            }
        }
        return joinPoint.proceed(args);
    }
}
