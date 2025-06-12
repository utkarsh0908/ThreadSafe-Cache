package com.cache;

import com.cache.eviction.EvictionStrategy;
import java.util.HashMap;

public class InMemoryCache {
    private final int maxSize;
    private final long defaultTTL;
    private final EvictionStrategy evictionStrategy;
    private final HashMap<String, CacheEntry> store = new HashMap<>();

    public InMemoryCache(int maxSize, long defaultTTL, EvictionStrategy evictionStrategy) {
        this.maxSize = maxSize;
        this.defaultTTL = defaultTTL;
        this.evictionStrategy = evictionStrategy;
    }

    // put key value with ttl in store - returns true if some key is evicted
    public boolean put(String key, String value, Long ttl) {
        long expiresAt = System.currentTimeMillis() + (ttl != null ? ttl : defaultTTL);
        boolean isEvicted = false;

        if (store.size() >= maxSize) {
            String evictedKey = evictionStrategy.evict();
            if (evictedKey != null) store.remove(evictedKey);
            isEvicted = true;
        }

        store.put(key, new CacheEntry(value, expiresAt));
        evictionStrategy.onInsert(key);
        return isEvicted;
    }

    // get value by passing the key from the store - returns value
    public String get(String key) {
        if (!store.containsKey(key)) return null;

        CacheEntry entry = store.get(key);
        if (entry.isExpired()) {
            store.remove(key);
            evictionStrategy.onDelete(key);
            return null;
        }

        evictionStrategy.recordAccess(key);
        return entry.getValue();
    }

    // delete an entry from the store
    public void delete(String key) {
        store.remove(key);
        evictionStrategy.onDelete(key);
    }

    // clear the store - remove all the entries
    public void clear() {
        store.clear();
    }

    // get the internal hashMap
    public HashMap<String, CacheEntry> getStore() {
        return store;
    }

    // cleanup expired entries in the store
    public void cleanupExpiredEntries() {
        store.entrySet().removeIf(entry -> {
            if (entry.getValue().isExpired()) {
                evictionStrategy.onDelete(entry.getKey());
                return true;
            }
            return false;
        });
    }
}
