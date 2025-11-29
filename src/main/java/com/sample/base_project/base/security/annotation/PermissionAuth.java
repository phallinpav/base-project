package com.sample.base_project.base.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionAuth {
    PermissionType value() default PermissionType.PRIVATE;
    String parent() default ""; // use this with custom
    String authority() default ""; // use this with custom

    enum PermissionType {
        PUBLIC,
        PRIVATE,
        AUTHORITY,
        PUBLIC_OR_AUTH,
    }
}
