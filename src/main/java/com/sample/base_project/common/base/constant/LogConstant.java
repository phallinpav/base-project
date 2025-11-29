package com.sample.base_project.common.base.constant;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LogConstant {
    public static Boolean DEBUG_LOG = false;
    public static Boolean DEBUG_LOG_CONTROLLER = false;
    public static Boolean DEBUG_LOG_SERVICE = false;
    public static Boolean DEBUG_LOG_REPOSITORY = false;

    @Value("${system.debugLog:false}")
    public void setDebugLog(Boolean debugLog) {
        DEBUG_LOG = debugLog;
    }

    @Value("${system.debugLogController:false}")
    public void setDebugLogController(Boolean debugLog) {
        DEBUG_LOG_CONTROLLER = debugLog;
    }

    @Value("${system.debugLogService:false}")
    public void setDebugLogService(Boolean debugLog) {
        DEBUG_LOG_SERVICE = debugLog;
    }

    @Value("${system.debugLogRepository:false}")
    public void setDebugLogRepository(Boolean debugLog) {
        DEBUG_LOG_REPOSITORY = debugLog;
    }
}
