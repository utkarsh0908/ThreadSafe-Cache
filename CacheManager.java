package com.cache;

import com.cache.eviction.EvictionStrategy;

import java.util.concurrent.locks.ReentrantLock;

public class CacheManager {
    private final InMemoryCache cache;
    private final Stats stats;
    private final CleanupWorker cleanupWorker;
    private final ReentrantLock lock = new ReentrantLock();
    private final Logger logger;

    public CacheManager(int maxSize, long defaultTTL, EvictionStrategy strategy, boolean isLoggerOn) {
        this.cache = new InMemoryCache(maxSize, defaultTTL, strategy);
        this.stats = new Stats();
        this.cleanupWorker = new CleanupWorker(cache, 10000); // 10 seconds
        this.cleanupWorker.start();
        logger = new Logger(isLoggerOn);
    }
    public CacheManager(int maxSize, long defaultTTL, EvictionStrategy strategy) {
        this(maxSize, defaultTTL, strategy, false);
    }

    public void put(String key, String value, Long ttl) {
        lock.lock();
        try {
            boolean isEvicted = cache.put(key, value, ttl);
            if (isEvicted) this.stats.incrementEvictions();
            logger.debug("[put] key: " + key + " value: " + value + " ttl: " + ttl);
        } catch (Exception e) {
            logger.error("err: "  + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public String get(String key) {
        lock.lock();
        try {
            stats.incrementTotalRequests();
            String value = cache.get(key);
            if (value == null) {
                stats.incrementMisses();
                logger.debug("[MISS] key: " + key + " value: " + value);
            } else {
                logger.debug("[HIT] key: " + key + " value: " + value);
                stats.incrementHits();
            }
            return value;
        } catch (Exception e) {
            logger.error("err: "  + e.getMessage());
        }
        finally {
            lock.unlock();
        }
        return "";
    }

    public void delete(String key) {
        lock.lock();
        try {
            cache.delete(key);
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        lock.lock();
        try {
            cache.clear();
            logger.debug("Cleared all available keys!");
        } catch(Exception e) {
            logger.error("err: "  + e.getMessage());
        }
        finally {
            lock.unlock();
        }
    }

    public String getStats() {
        String currStats = stats.toJSON(cache.getStore().size());
        logger.debug("[STATS]: " + currStats);
        return currStats;
    }

    public void shutdown() {
        logger.debug("Shutting down CleanupWorker!");
        cleanupWorker.shutdown();
    }
}
