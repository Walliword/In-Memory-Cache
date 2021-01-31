package org.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCacheTest {

    private static final String KEY1 = "first";
    private static final String KEY2 = "second";
    private static final String KEY3 = "third";

    private static final String VALUE1 = "one";
    private static final String VALUE2 = "two";
    private static final String VALUE3 = "three";

    private InMemoryCache<String, String> cache;

    @BeforeEach
    public void setCache() {
        cache = new InMemoryCache<>(2, Policy.LFU);

        cache.putInCache(KEY1, VALUE1);
        cache.putInCache(KEY2, VALUE2);
    }

    @AfterEach
    public void clearCache() {
        cache.clearCache();
    }

    @Test
    void testDeleteMissingKey() {
        assertEquals(2, cache.getSize());
        assertTrue(cache.containsObject(KEY1));
        assertTrue(cache.containsObject(KEY2));
        assertFalse(cache.containsObject(KEY3));

        cache.deleteFromCache(KEY3);
        //Nothing is deleted
        assertEquals(2, cache.getSize());
    }

    @Test
    void testGetMissingKey() {
        assertTrue(cache.containsObject(KEY1));
        assertTrue(cache.containsObject(KEY2));
        assertFalse(cache.containsObject(KEY3));
        //Can't get what it doesn't have
        assertNull(cache.getFromCache(KEY3));
    }

    @Test
    void testClearCache() {
        assertEquals(2, cache.getSize());
        cache.clearCache();
        assertEquals(0, cache.getSize());
    }

    @Test
    void testDeleteKeyAndAddNew() {
        assertEquals(2, cache.getSize());
        //No free space available
        assertFalse(cache.hasFreeSpace());
        cache.deleteFromCache(KEY1);
        //Has free space now
        assertTrue(cache.hasFreeSpace());
        assertEquals(1, cache.getSize());

        cache.putInCache(KEY3, VALUE3);
        assertEquals(2, cache.getSize());
        assertTrue(cache.containsObject(KEY3));
        //No free space again
        assertFalse(cache.hasFreeSpace());

    }
}