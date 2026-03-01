package ru.job4j.tracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * The Item class describes a statement model.
 * @author Anton Serdyuchenko
 * @since 16.09.2025
 */
public class Item {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MMMM-EEEE-yyyy HH:mm:ss");
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return id == item.id
                && Objects.equals(name, item.name)
                && Objects.equals(created, item.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, created);
    }

    @Override
    public String toString() {
        return String.format("Item{id=%d, name='%s', created=%s}", id, name, created.format(FORMATTER));
    }
}
