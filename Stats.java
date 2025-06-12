package com.cache;

public class Stats {
    private int hits = 0;
    private int misses = 0;
    private int evictions = 0;
    private int expiredRemovals = 0;
    private int totalRequests = 0;

    public String toJSON(int currentSize) {
        double hitRate = totalRequests == 0 ? 0.0 : (double) hits / totalRequests;
        return String.format(
            "{ \"hits\": %d, \"misses\": %d, \"evictions\": %d, \"expired_removals\": %d, \"total_requests\": %d, \"current_size\": %d, \"hit_rate\": %.3f }",
            hits, misses, evictions, expiredRemovals, totalRequests, currentSize, hitRate
        );
    }
    public int getHits() {
        return hits;
    }
    public int getMisses() {
        return misses;
    }
    public int getEvictions() {
        return evictions;
    }
    public int getExpiredRemovals() {
        return expiredRemovals;
    }
    public int getTotalRequests() {
        return totalRequests;
    }

    public void incrementEvictions() {
        evictions++;
    }
    public void incrementHits() {
        hits++;
    }
    public void incrementMisses() {
        misses++;
    }
    public void incrementTotalRequests() {
        totalRequests++;
    }
    public void incrementExpiredRemovals() {
        expiredRemovals++;
    }
}
