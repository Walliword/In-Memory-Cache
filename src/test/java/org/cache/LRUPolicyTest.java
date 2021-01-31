package org.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUPolicyTest {

    private static final String KEY1 = "first";
    private static final String KEY2 = "second";
    private static final String KEY3 = "third";
    private static final String KEY4 = "forth";
    private static final String KEY5 = "fifth";
    private static final String KEY6 = "sixth";

    private static final String VALUE1 = "one";
    private static final String VALUE2 = "two";
    private static final String VALUE3 = "three";
    private static final String VALUE4 = "four";
    private static final String VALUE5 = "five";
    private static final String VALUE6 = "six";

    private InMemoryCache<String, String> cache;

    @BeforeEach
    public void setCache() {
        cache = new InMemoryCache<>(5, Policy.LRU);
        cache.putInCache(KEY1, VALUE1);
        cache.putInCache(KEY2, VALUE2);
        cache.putInCache(KEY3, VALUE3);
        cache.putInCache(KEY4, VALUE4);
        cache.putInCache(KEY5, VALUE5);
    }

    @AfterEach
    public void clearCache() {
        cache.clearCache();
    }

    @Test
    void testReplaceLeastRecentlyUsedObject() {

        String val = cache.getFromCache(KEY1);
        assertEquals(VALUE1, val);
        val = cache.getFromCache(KEY2);
        assertEquals(VALUE2, val);
        val = cache.getFromCache(KEY3);
        assertEquals(VALUE3, val);
        val = cache.getFromCache(KEY4);
        assertEquals(VALUE4, val);
        val = cache.getFromCache(KEY5);
        assertEquals(VALUE5, val);

        assertEquals(5, cache.getSize());
        cache.putInCache(KEY6, VALUE6);
        assertEquals(5, cache.getSize());

        //Replaced as least recently used
        assertFalse(cache.containsObject(KEY1));
        assertTrue(cache.containsObject(KEY2));
        assertTrue(cache.containsObject(KEY3));
        assertTrue(cache.containsObject(KEY4));
        assertTrue(cache.containsObject(KEY5));
        assertTrue(cache.containsObject(KEY6));

        assertEquals(5, cache.getSize());
        cache.putInCache(KEY1, VALUE1);
        assertEquals(5, cache.getSize());

        assertTrue(cache.containsObject(KEY1));
        //Replaced as least recently used
        assertFalse(cache.containsObject(KEY2));
        assertTrue(cache.containsObject(KEY3));
        assertTrue(cache.containsObject(KEY4));
        assertTrue(cache.containsObject(KEY5));
        assertTrue(cache.containsObject(KEY6));

    }

}