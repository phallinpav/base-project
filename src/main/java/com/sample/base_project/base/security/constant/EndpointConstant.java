package com.sample.base_project.base.security.constant;

import com.sample.base_project.common.utils.common.ApplicationContextProvider;
import com.sample.base_project.base.security.annotation.PermissionAuth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@DependsOn("applicationContextProvider")
public class EndpointConstant {
    public static Map<String, List<Pair<String, String>>> PERMISSION_LIST = new LinkedHashMap<>();
    public static List<String> PUBLIC_LIST = new ArrayList<>();
    public static List<String> PUBLIC_OR_AUTH_LIST = new ArrayList<>();
    public final static List<String> CUSTOM_PUBLIC_LIST = List.of("/ws/**");

    @Autowired
    public void init() {
        RequestMappingHandlerMapping mapping = ApplicationContextProvider.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        for (var val : handlerMethods.entrySet()) {
            RequestMappingInfo requestMappingInfo = val.getKey();
            HandlerMethod handlerMethod = val.getValue();

            Set<String> paths = requestMappingInfo.getPathPatternsCondition().getDirectPaths();
            for (String path : paths) {
                PermissionAuth permissionMethod = handlerMethod.getMethodAnnotation(PermissionAuth.class);
                if (permissionMethod != null) {
                    setupAuthorizePath(permissionMethod, path);
                } else {
                    PermissionAuth permissionClass = handlerMethod.getBeanType().getAnnotation(PermissionAuth.class);
                    if (permissionClass != null) {
                        setupAuthorizePath(permissionClass, path);
                    }
                }
            }
        }
    }

    private void setupAuthorizePath(PermissionAuth permission, String path) {
        switch (permission.value()) {
            case PUBLIC -> {
                PUBLIC_LIST.add(path);
            }
            case PUBLIC_OR_AUTH -> {
                PUBLIC_OR_AUTH_LIST.add(path);
            }
            case AUTHORITY -> {
                String halfPath = path.replace("/", "");
                String[] split = halfPath.split("/");
                String authority = permission.authority();
                if (StringUtils.isBlank(authority)) {
                    authority = split[split.length - 1];
                }
                String parent = permission.parent();
                if (StringUtils.isBlank(parent)) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < split.length - 1; i++) {
                        String tmp = split[i];
                        tmp = tmp.substring(0, 1).toUpperCase() + tmp.substring(1);
                        builder.append(tmp);
                    }
                    parent = builder.toString();
                }

                authority = parent + "." + authority;

                List<Pair<String, String>> list = PERMISSION_LIST.get(parent);
                if(list == null || list.isEmpty()) {
                    List<Pair<String, String>> authList = new ArrayList<>();
                    authList.add(Pair.of(path, authority));
                    PERMISSION_LIST.put(parent, authList);
                } else {
                    list.add(Pair.of(path, authority));
                }
            }
        }
    }
}
