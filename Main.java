package com.cache;

import com.cache.eviction.LRUEvictionStrategy;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CacheManager cache = new CacheManager(3, 5000, new LRUEvictionStrategy(), true);

        cache.put("a", "alpha", 2000L);
        cache.put("b", "beta", null);  // uses default TTL
        cache.put("c", "gamma", null);
//        cache.put("d", "zeta", null);

        Thread.sleep(2500); // let "a" expire
        String valA = cache.get("a");
        String valB = cache.get("b");
        String valC = cache.get("c");
//        String valD = cache.get("d");

        String currStats = cache.getStats();
        cache.shutdown();
    }
}
