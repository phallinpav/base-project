package com.sample.base_project.base.utils.common;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpRequestUtils {
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();

    public static HttpServletResponse getResponse() {
        return responseHolder.get();
    }

    public static void setResponse(HttpServletResponse response) {
        responseHolder.set(response);
    }

    public static HttpServletRequest getRequest() {
        return requestHolder.get();
    }

    public static void setRequest(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static void clear() {
        requestHolder.remove();
        responseHolder.remove();
    }


}
