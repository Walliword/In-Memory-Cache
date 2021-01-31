package org.cache;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPolicy<K> {

    @Getter
    private final Map<K, Long> cache;

    protected AbstractPolicy() {
        this.cache = new HashMap<>();
    }

    public abstract void putObject(K key);

    public K getReplaceable() {
        return getCache().entrySet().stream().min(Map.Entry.comparingByValue()).orElseThrow(
                () -> new RuntimeException("Valid EntrySet not found")).getKey();
    }

    public boolean contains(K key) {
        return cache.containsKey(key);
    }

    public void delete(K key) {
        if (contains(key)) {
            cache.remove(key);
        }
    }

    public void clear() {
        cache.clear();
    }
}
