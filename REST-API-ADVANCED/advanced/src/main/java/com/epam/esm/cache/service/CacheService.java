package com.epam.esm.cache.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class CacheService {

    private final Map<String, Object> cacheMap = new WeakHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getCache(String key) {
        return (T) cacheMap.get(key);
    }

    public void put(String key, Object cache) {
        cacheMap.put(key, cache);
    }
}
