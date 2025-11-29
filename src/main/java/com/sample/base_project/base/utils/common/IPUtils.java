package com.sample.base_project.base.utils.common;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Stream;

public class IPUtils {
    private static final List<String> IP_HEAD_LIST = Stream.of("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "X-Real_IP", "X-Real-IP").toList();

    public static String getIpAddress(HttpServletRequest request) {
        for (String ipHead : IP_HEAD_LIST) {
            if (checkIP(request.getHeader(ipHead))) {
                return request.getHeader(ipHead).split(",")[0];
            }
        }
        return "0:0:0:0:0:0:0:1".equals(request.getRemoteAddr()) ? "127.0.0.1" : request.getRemoteAddr();
    }

    private static boolean checkIP(String ip) {
        return !(null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip));
    }
}
