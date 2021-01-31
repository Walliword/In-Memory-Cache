package org.cache;

import lombok.Getter;

public enum Policy {

    LRU("Least Recently Used Policy"),
    LFU("Least Frequently Used Policy");

    @Getter
    private final String name;

    Policy(String name) {
        this.name = name;
    }
}
