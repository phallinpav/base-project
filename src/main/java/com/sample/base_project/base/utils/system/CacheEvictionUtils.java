package com.sample.base_project.base.utils.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CacheEvictionUtils {

    private static StringRedisTemplate redisTemplate;

    @Autowired
    public void init(StringRedisTemplate redisTemplate) {
        CacheEvictionUtils.redisTemplate = redisTemplate;
    }

    public static void evictCache(String cacheName) {
        redisTemplate.delete(cacheName);
    }

    public static void evictByPartialKey(String cacheName, String partialKey) {
        String pattern = cacheName + "::" + partialKey + "*";

        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
