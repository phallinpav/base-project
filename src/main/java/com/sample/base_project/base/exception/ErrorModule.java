package com.sample.base_project.base.exception;

import com.sample.base_project.common.exception.IErrorModule;
import lombok.Getter;

@Getter
public enum ErrorModule implements IErrorModule {

    TOKEN("token"),
    NAME("name"),
    EMAIL("email"),
    PHONE("phone"),

    APP_VERSION("app.version"),

    OSS_FILE("oss.file"),
    VERIFY_CODE("verify.code"),
    VERIFICATION_ID("verification.id"),
    CAPTCHA("captcha"),
    STORAGE("storage"),
    BROADCAST("broadcast"),
    BROADCAST_DETAIL("broadcast.detail"),
    UUID("uuid"),
    USER("user"),
    KYC("kyc"),
    VERIFY_KYC("verify.kyc"),
    KYC_FACE("kyc.face"),
    KYC_BASIC("kyc.basic"),
    USER_TOTP("user.totp"),
    DEV("dev"),
    DEV_TOTP("dev.totp"),
    DEV_APP("dev.app"),
    DEV_APP_KEY("dev.app.key"),
    DEV_TOKEN("dev.token"),
    USER_TOKEN("user.token"),
    USER_EMAIL("user.email"),
    USER_PHONE("user.phone"),
    USER_PASSWORD("user.password"),
    ;
    private final String value;

    ErrorModule(String value) {
        this.value = value;
    }

}
