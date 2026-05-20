package ru.job4j.tracker;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * The Item class describes a statement model.
 * @author Anton Serdyuchenko
 * @since 16.09.2025
 */
@Data
public class Item {
    private int id;
    private String name;
    private final LocalDateTime created;

    public Item() {
        this(null);
    }

    public Item(String name) {
        this(0, name, LocalDateTime.now());
    }

    public Item(int id, String name, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.created = created == null ? LocalDateTime.now() : created;
    }
}
