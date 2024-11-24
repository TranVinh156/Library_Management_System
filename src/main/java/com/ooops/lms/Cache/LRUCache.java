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

    /**
     * lấy.
     * @param key khoá
     * @return giá trị
     */
    public V get(K key) {
        Node<K, V> node = cache.get(key);
        if (node == null) {
            return null; // Key không tồn tại trong cache
        }
        moveToHead(node); // Đưa node lên đầu danh sách
        return node.value;
    }

    /**
     * Thêm.
     * @param key khoá
     * @param value giá trị
     */
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

    /**
     * kiểm tra có khoá.
     * @param key khoá
     * @return true false
     */
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    /**
     * dọn.
     */
    public void clear() {
        cache.clear();
        head.next = tail;
        tail.prev = head;
    }

    /**
     * thêm vào đầu
     * @param node
     */
    private void addToHead(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    /**
     * xoá.
     * @param node
     */
    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /**
     * di chuyển về đầu.
     * @param node
     */
    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
    }

    /**
     * xoá đuôi.
     * @return
     */
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

