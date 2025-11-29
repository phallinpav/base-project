package com.sample.base_project.common.base.aspect;

import com.sample.base_project.common.base.constant.LogConstant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BaseLog {
    protected Object proceedingLog(ProceedingJoinPoint joinPoint, Logger log) throws Throwable {
        if (!LogConstant.DEBUG_LOG) {
            return joinPoint.proceed(joinPoint.getArgs());
        }

        Object[] args = joinPoint.getArgs();
        String argsString = convertToString(args);
        Signature signature = joinPoint.getSignature();
        String format = "\033[0m\n\t\033[3;33m{}.{}(\033[0m\n\t\t\033[1;32m{}\033[0m\n\t\033[3;33m)\033[0m";
        String formatBefore = "BEFORE:" + format;
        log.info(formatBefore,
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                argsString);
        Object result = joinPoint.proceed(args);

        String formatAfter = "AFTER:" + format + " =\n\t\033[1;32m{}\033[0m";
        log.info(formatAfter,
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                args,
                result);

        return result;
    }

    private String convertToString(Object[] args) {
        String argsString;
        if (args == null) {
            argsString = null;
        } else if (args.length == 0) {
            argsString = "[]";
        } else {
            argsString = Arrays.stream(args)
                    .map(String::valueOf)
                    .collect(Collectors.joining(",\n\t\t\t"));
            argsString = "[\n\t\t\t" + argsString + "\n\t\t]";
        }
        return argsString;
    }
}
