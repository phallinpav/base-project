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
public class LogRepositoryAspect extends BaseLog {

    @Around("execution(* *(..)) && target(com.sample.base_project.common.base.repository.BaseRepository)")
    public Object logAroundBaseRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!LogConstant.DEBUG_LOG_REPOSITORY) {
            return joinPoint.proceed(joinPoint.getArgs());
        }
        return proceedingLog(joinPoint, log);
    }
}
