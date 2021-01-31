package org.cache;


public class LRUPolicy<K> extends AbstractPolicy<K> {

    public void putObject(K key) {
        getCache().put(key, System.nanoTime());
    }

}
