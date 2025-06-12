package com.cache.lru;

public class ListNode {
    private String key;
    private ListNode prev;
    private ListNode next;

    public ListNode(String key) {
        this.key = key;
    }

    public String getKey() { return key; }
    public ListNode getPrev() { return prev; }
    public ListNode getNext() { return next; }
    public void setPrev(ListNode prev) { this.prev = prev; }
    public void setNext(ListNode next) { this.next = next; }
}
