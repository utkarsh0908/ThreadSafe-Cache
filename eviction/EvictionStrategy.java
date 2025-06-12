package com.cache.eviction;

// interface for evictionStrategy - Strategy Pattern
public interface EvictionStrategy {
    void recordAccess(String key);
    void onInsert(String key);
    void onDelete(String key);
    String evict();
}
