package com.sample.base_project.common.utils.common;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.util.UriEncoder;

import java.util.Map;

public class HttpClientUtils {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static <T> ResponseEntity<T> get(String url, @Nullable HttpHeaders headers, ParameterizedTypeReference<T> clazz) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), clazz);
    }

    public static <T> ResponseEntity<T> get(String url, @Nullable HttpHeaders headers, Class<T> clazz) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), clazz);
    }

    public static <T> ResponseEntity<T> get(String url, Map<String, String> uriVariables, @Nullable HttpHeaders headers, Class<T> clazz) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), clazz, uriVariables);
    }

    public static ResponseEntity<Map> get(String url, @Nullable HttpHeaders headers) {
        return get(url, headers, Map.class);
    }

    public static ResponseEntity<Map> get(String url, Map<String, String> params, @Nullable HttpHeaders headers) {
        return get(url, params, headers, Map.class);
    }

    public static <T, V> ResponseEntity<T> post(String url, @Nullable HttpHeaders headers, @Nullable V body, Class<T> clazz) {
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), clazz);
    }

    public static <T, V> ResponseEntity<T> delete(String url, @Nullable HttpHeaders headers, @Nullable V body, Class<T> clazz) {
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(body, headers), clazz);
    }

    public static <T, V> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpHeaders headers, @Nullable V body, Class<T> clazz) {
        return restTemplate.exchange(url, method, new HttpEntity<>(body, headers), clazz);
    }

    public static String buildUrlQueryParam(String url, Map<String, String> queryParams) {
        boolean isFirst = !url.contains("?");
        StringBuilder urlBuilder = new StringBuilder(url);
        if (queryParams != null) {
            for (var val : queryParams.entrySet()) {
                String delimiter = isFirst ? "?" : "&";
                isFirst = false;
                String encodedValue = UriEncoder.encode(val.getValue());
                urlBuilder.append(delimiter).append(val.getKey()).append("=").append(encodedValue);
            }
        }
        return urlBuilder.toString();
    }
}
