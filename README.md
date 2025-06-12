# Thread-Safe In-Memory Cache (Java)

A thread-safe, in-memory cache system in Java with TTL-based expiration, LRU eviction strategy (via the Strategy Pattern), and background cleanup.

---

## How to Run the Code

### 1. Clone the Repository

```bash
git clone https://github.com/utkarsh0908/ThreadSafe-Cache.git
cd ThreadSafe-Cache
```

### 2. Compile the Java Source Files

```bash
javac com/cache/**/*.java
```

### 3. Run the Example

```bash
java com.cache.Main
```

You should see TTL expiry, LRU behavior, and printed cache statistics.

---

## Dependencies

- **Java 11+**
- No third-party libraries — uses only `java.util` and core concurrency classes

---

## Design Decisions

- **Separation of Concerns**: Cache logic, eviction policy, TTL cleanup, and statistics are modularized for maintainability.
- **Strategy Pattern**: Eviction behavior is abstracted into an `EvictionStrategy` interface, making the cache easily extendable to support LFU, FIFO, etc.
- **Doubly Linked List**: Used to track usage order in LRU for efficient O(1) updates.
- **ReentrantLock**: Used to ensure thread-safety of public operations without compromising performance with heavy synchronization.

---

## Concurrency Model

- Each public method (`put`, `get`, `delete`, `clear`) in `CacheManager` is protected using a `ReentrantLock`.
- A background thread (`CleanupWorker`) safely scans and removes expired entries at fixed intervals (default: 10 seconds).

---

## Eviction Logic

### LRU Eviction Strategy (Default)
- When cache exceeds `maxSize`, the least recently used key is evicted.
- Implemented via a combination of `HashMap<String, Node>` and a `DoublyLinkedList`.
- Eviction logic is pluggable using the `EvictionStrategy` interface:

```java
public interface EvictionStrategy {
    void recordAccess(String key);
    void onInsert(String key);
    void onDelete(String key);
    String evict(); // returns key to remove
}
```

### Easily Extendable
To add new eviction strategies (e.g. LFU):
- Implement the `EvictionStrategy` interface
- Inject your strategy into `CacheManager`

---

## TTL and Cleanup

- Every cache entry is associated with an `expiresAt` timestamp.
- TTL is checked both lazily (during `get`) and eagerly (by `CleanupWorker`).
- You can specify TTL per entry during `put()`. If omitted, `defaultTTL` is used.

---

## Sample Output

```
[DEBUG]: [put] key: a value: alpha ttl: 2000
[DEBUG]: [put] key: b value: beta ttl: null
[DEBUG]: [put] key: c value: gamma ttl: null
[DEBUG]: [MISS] key: a value: null
[DEBUG]: [HIT] key: b value: beta
[DEBUG]: [HIT] key: c value: gamma
[DEBUG]: [STATS]: { "hits": 2, "misses": 1, "evictions": 0, "expired_removals": 0, "total_requests": 3, "current_size": 2, "hit_rate": 0.667 }
[DEBUG]: Shutting down CleanupWorker!
```

---

## Performance Considerations

- `O(1)` for get/put/delete due to hash map and doubly linked list
- Locking is using `ReentrantLock` — may be optimized with read-write locks if needed
- TTL cleanup is non-blocking (runs in background thread)

---

## Project Structure

```
com/
└── cache/
    ├── CacheManager.java         # Thread-safe facade
    ├── InMemoryCache.java        # Internal cache logic
    ├── CleanupWorker.java        # Background TTL cleanup
    ├── Stats.java                # Metrics tracker
    ├── Main.java                 # Usage demo
    └── eviction/
        ├── EvictionStrategy.java
        └── LRUEvictionStrategy.java
    └── lru/
        ├── DoublyLinkedList.java
        └── ListNode.java
```

---

## Example Code

```java
CacheManager cache = new CacheManager(3, 5000, new LRUEvictionStrategy());

cache.put("a", "alpha", 2000L);
cache.put("b", "beta", null);
System.out.println("GET a: " + cache.get("a")); // "alpha"

Thread.sleep(2500);
System.out.println("GET a after expiry: " + cache.get("a")); // null
System.out.println("GET b: " + cache.get("b")); // "beta"

System.out.println("STATS: " + cache.getStats());
cache.shutdown();
```