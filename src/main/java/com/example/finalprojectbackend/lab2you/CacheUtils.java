package com.example.finalprojectbackend.lab2you;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public class CacheUtils {

    private final CacheManager cacheManager;

    public CacheUtils(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clearCacheByName(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            System.out.println("Cleared cache: " + cacheName);
        } else {
            System.out.println("Cache not found: " + cacheName);
        }
    }
}
