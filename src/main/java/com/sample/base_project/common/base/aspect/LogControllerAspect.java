package com.sample.base_project.common.base.aspect;

import com.sample.base_project.common.base.constant.LogConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogControllerAspect extends BaseLog {

    @Around("execution(* *(..)) && @within(org.springframework.web.bind.annotation.RestController)")
    public Object logAroundRestController(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!LogConstant.DEBUG_LOG_CONTROLLER) {
            return joinPoint.proceed(joinPoint.getArgs());
        }
        return proceedingLog(joinPoint, log);
    }
}
