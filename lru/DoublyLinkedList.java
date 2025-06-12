package com.cache.lru;

public class DoublyLinkedList {
    private ListNode head;
    private ListNode tail;

    public DoublyLinkedList() {
        head = new ListNode(null);
        tail = new ListNode(null);
        head.setNext(tail);
        tail.setPrev(head);
    }

    public void addToFront(ListNode node) {
        ListNode first = head.getNext();
        node.setPrev(head);
        node.setNext(first);
        first.setPrev(node);
        head.setNext(node);
    }

    public void moveToFront(ListNode node) {
        remove(node);
        addToFront(node);
    }

    public void remove(ListNode node) {
        ListNode prev = node.getPrev();
        ListNode next = node.getNext();
        if (prev != null) prev.setNext(next);
        if (next != null) next.setPrev(prev);
    }

    public ListNode removeTail() {
        ListNode last = tail.getPrev();
        if (last == head) return null;
        remove(last);
        return last;
    }
}
