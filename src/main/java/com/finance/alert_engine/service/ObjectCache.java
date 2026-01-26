package com.finance.alert_engine.service;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ObjectCache<T> {

    private final LinkedList<T> cache = new LinkedList<>();
    private final int maxSize;

    public ObjectCache() {
        this.maxSize = 10; // default max size
    }

    public ObjectCache(int maxSize) {
        this.maxSize = maxSize;
    }

    // Add new item
    public synchronized void add(T item) {
        cache.addLast(item);

        // Remove oldest if exceeds max size
        if (cache.size() > maxSize) {
            cache.removeFirst();
        }
    }

    // Get last N items
    public synchronized List<T> getLastN(int n) {
        int start = Math.max(0, cache.size() - n);
        return new LinkedList<>(cache.subList(start, cache.size()));
    }

    // Get all items
    public synchronized List<T> getAll() {
        return new LinkedList<>(cache);
    }
}
