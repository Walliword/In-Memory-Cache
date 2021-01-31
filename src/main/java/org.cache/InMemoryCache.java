package org.cache;

import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class InMemoryCache<K extends Serializable, V extends Serializable> {

    private final Map<K, V> cache;
    private final int capacity;
    private final AbstractPolicy<K> policy;


    public InMemoryCache(int capacity, Policy policyType) {
        this.capacity = capacity;
        this.policy = getPolicyByType(policyType);
        this.cache = new HashMap<>(capacity);
    }

    private AbstractPolicy<K> getPolicyByType(Policy policyType) {
        log.debug(String.format("Using cache policy: %s", policyType.getName()));
        if (Policy.LRU.equals(policyType)) {
            return new LRUPolicy<>();
        }
        return new LFUPolicy<>();
    }

    public void putInCache(K key, V value) {
        log.debug(String.format("Putting object into cache with key: %s", key));
        if (cache.containsKey(key) || hasFreeSpace()) {
            cache.put(key, value);
        } else {
            replace(key, value);
        }
        if (!policy.contains(key)) {
            policy.putObject(key);
        }
    }

    public boolean hasFreeSpace() {
        return cache.size() < capacity;
    }

    private void replace(K key, V value) {
        K replaceable = policy.getReplaceable();
        log.debug(String.format("Replacing an object in cache with key: %s", replaceable));
        deleteFromCache(replaceable);
        cache.put(key, value);
    }

    public V getFromCache(K key) {
        log.debug(String.format("Getting an object from cache with key: %s", key));
        if (cache.containsKey(key)) {
            policy.putObject(key);
            return cache.get(key);
        }
        return null;
    }

    public void deleteFromCache(K key) {
        log.debug(String.format("Deleting an object from cache with key: %s", key));
        if (containsObject(key)) {
            cache.remove(key);
            policy.delete(key);
        } else {
            log.debug(String.format("An object with key %s does not exist in cache", key));
        }
    }

    public boolean containsObject(K key) {
        return cache.containsKey(key);
    }

    public int getSize() {
        log.debug(String.format("Cache size is: %d", cache.size()));
        return cache.size();
    }

    public void clearCache() {
        log.debug(String.format("Clearing cache from objects: %d", cache.size()));
        cache.clear();
        policy.clear();
        log.debug(String.format("Cache contains %d objects now", cache.size()));
    }

}
