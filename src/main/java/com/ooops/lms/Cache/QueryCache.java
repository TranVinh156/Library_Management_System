package com.ooops.lms.Cache;

public class QueryCache<K,V> extends LRUCache<K,V> {
    public QueryCache(int capacity) {
        super(capacity);
    }

    public void storeQueryResult(K query, V result) {
        this.put(query, result);
    }

    public V getQueryResult(K query) {
        V result = this.get(query);
        if (result == null) {
            System.out.println("Cache miss for query: " + query);
        } else {
            System.out.println("Cache hit for query: " + query);
        }
        return result;
    }

}
