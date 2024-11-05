package com.ooops.lms.Cache;

import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> cache;
    private final Node<K, V> head;
    private final Node<K, V> tail;

    // Constructor
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    // Get method
    public V get(K key) {
        Node<K, V> node = cache.get(key);
        if (node == null) {
            return null; // Key không tồn tại trong cache
        }
        moveToHead(node); // Đưa node lên đầu danh sách
        return node.value;
    }

    // Put method
    public void put(K key, V value) {
        Node<K, V> node = cache.get(key);
        if (node != null) {
            node.value = value; // Cập nhật giá trị
            moveToHead(node);   // Đưa node lên đầu danh sách
        } else {
            // Tạo một node mới
            Node<K, V> newNode = new Node<>(key, value);
            cache.put(key, newNode);
            addToHead(newNode);

            // Nếu vượt quá capacity thì xoá node cuối
            if (cache.size() > capacity) {
                Node<K, V> tail = removeTail();
                cache.remove(tail.key);
            }
        }
    }

    // Helper method to add a node to the head
    private void addToHead(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    // Helper method to remove a node from the linked list
    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // Move a node to the head (most recently used)
    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }

    // Remove the tail node (least recently used)
    private Node<K, V> removeTail() {
        Node<K, V> node = tail.prev;
        removeNode(node);
        return node;
    }

    // Node class
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
