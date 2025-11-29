package com.sample.base_project.base.constant;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemConstant {
    public static Boolean DEBUG = false;
    public static Boolean DEBUG_LOG = false;
    public static Boolean DEBUG_LOG_CONTROLLER = false;
    public static Boolean DEBUG_LOG_SERVICE = false;
    public static Boolean DEBUG_LOG_REPOSITORY = false;
    public static String VERIFY_CODE;

    @Value("${system.debug:false}")
    public void setDebug(Boolean debug) {
        DEBUG = debug;
    }

    @Value("${system.debugLog:false}")
    public void setDebugLog(Boolean debugLog) {
        DEBUG_LOG = debugLog;
    }

    @Value("${system.debugLogController:false}")
    public void setDebugLogController(Boolean debugLogController) {
        DEBUG_LOG_CONTROLLER = debugLogController;
    }

    @Value("${system.debugLogService:false}")
    public void setDebugLogService(Boolean debugLogService) {
        DEBUG_LOG_SERVICE = debugLogService;
    }

    @Value("${system.debugLogRepository:false}")
    public void setDebugLogRepository(Boolean debugLogRepository) {
        DEBUG_LOG_REPOSITORY = debugLogRepository;
    }

    @Value("${system.defaultVerifyCode:123456}")
    public void setVerifyCode(String defaultVerifyCode) {
        VERIFY_CODE = defaultVerifyCode;
    }

}
