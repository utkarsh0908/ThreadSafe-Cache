package com.cache;

public class CleanupWorker extends Thread {
    private final InMemoryCache cache;
    private final long interval;
    private volatile boolean running = true;

    public CleanupWorker(InMemoryCache cache, long intervalMillis) {
        this.cache = cache;
        this.interval = intervalMillis;
    }

    // start the cleanupWorker in separate thread
    // sleep is used to create a 10sec delay between each iteration
    public void run() {
        while (running) {
            try {
                Thread.sleep(interval);
                cache.cleanupExpiredEntries();
            } catch (InterruptedException ignored) {}
        }
    }

    public void shutdown() {
        running = false;
        this.interrupt();
    }
}
