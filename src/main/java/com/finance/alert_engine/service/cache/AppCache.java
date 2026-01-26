package com.finance.alert_engine.service.cache;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class AppCache {

    private final int maxSize = 100; // optional max size
    private final LinkedHashMap<String, Object> cache = new LinkedHashMap<>();

    // Add or update object
    public synchronized <T> void put(String key, T value) {
        cache.put(key, value);

        // Optional: remove oldest if exceeds max size
        if (cache.size() > maxSize) {
            String firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
        }
    }

    // Get object by key
    @SuppressWarnings("unchecked")
    public synchronized <T> T get(String key, Class<T> clazz) {
        Object value = cache.get(key);
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    // Get the last N inserted objects
    @SuppressWarnings("unchecked")
    public synchronized <T> T[] getLastN(int n, Class<T> clazz) {
        return cache.values()
                .stream()
                .skip(Math.max(0, cache.size() - n))
                .map(clazz::cast)
                .toArray(size -> (T[]) new Object[size]);
    }
}
