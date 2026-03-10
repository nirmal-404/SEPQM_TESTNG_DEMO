package com.demo.oms.util;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class OrderIdGenerator {

    private final AtomicLong counter = new AtomicLong(0);

    public String nextId() {
        long next = counter.incrementAndGet();
        return "ORD-" + Instant.now().toEpochMilli() + "-" + next;
    }
}
