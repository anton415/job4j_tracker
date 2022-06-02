package ru.job4j.tracker;

import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.store.MemTracker;

/**
 * Running an application with different Garbage Collectors.
 * Duration:            179684708
 * Serial Duration:     134083333
 * Parallel Duration:   139325250
 * G1 Duration:         146580459
 * ZGC Duration:        140464292
 */
public class GCTest {
    public static void main(String[] args) {
        MemTracker tracker = new MemTracker();
        long startTime = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            tracker.add(new Item("item " + i));
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.printf("Duration: %d", duration);
    }
}