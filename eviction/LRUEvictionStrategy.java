package com.cache.eviction;

import com.cache.lru.DoublyLinkedList;
import com.cache.lru.ListNode;

import java.util.HashMap;

// Implementation of LRU Eviction Strategy - add other strategies by implementing EvictionStrategy interface
public class LRUEvictionStrategy implements EvictionStrategy {
    private final DoublyLinkedList list = new DoublyLinkedList();
    private final HashMap<String, ListNode> map = new HashMap<>();

    @Override
    public void recordAccess(String key) {
        if (map.containsKey(key)) {
            list.moveToFront(map.get(key));
        }
    }

    @Override
    public void onInsert(String key) {
        ListNode node = new ListNode(key);
        map.put(key, node);
        list.addToFront(node);
    }

    @Override
    public void onDelete(String key) {
        ListNode node = map.remove(key);
        if (node != null) list.remove(node);
    }

    @Override
    public String evict() {
        ListNode node = list.removeTail();
        if (node == null) return null;
        map.remove(node.getKey());
        return node.getKey();
    }
}
