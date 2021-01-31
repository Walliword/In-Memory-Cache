package org.cache;


public class LFUPolicy<K> extends AbstractPolicy<K> {

    public void putObject(K key) {
        long amount = getCache().containsKey(key) ? getCache().get(key) + 1 : 1;
        getCache().put(key, amount);
    }

}
