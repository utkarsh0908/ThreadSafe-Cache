package com.cache;

// class to store the cache value
public class CacheEntry {
    private final String value;
    private final long expiresAt;

    CacheEntry(String value, long expiresAt) {
        this.value = value;
        this.expiresAt = expiresAt;
    }

    // getters
    public String getValue() {
        return value;
    }
    public long getExpiresAt() {
        return expiresAt;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiresAt;
    }
}