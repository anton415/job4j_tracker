package ru.job4j.tracker;

import java.time.LocalDateTime;

/**
 * The Item class describes a statement model.
 * @author Anton Serdyuchenko
 * @since 16.09.2025
 */
public class Item {
    /**
     * The id field is a unique application number.
     */
    private int id;
    /**
     * The name field contains the name of the application.
     */
    private String name;
    /**
     * The date and time of created item.
     */
    private final LocalDateTime created = LocalDateTime.now();

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return String.format("Item{id=%d, name='%s', created=%s}", id, name, created);
    }
}