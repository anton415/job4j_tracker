package ru.job4j.tracker;

import java.util.Arrays;

/**
 * Main class for working with Items.
 * @author Anton Serdyuchenko
 * @since 16.09.2025
 */
public class Tracker {
    private final Item[] items = new Item[100];
    private int ids = 1;
    private int size = 0;

    /**
     * Add new item.
     * @param item  - item.
     * @return      - created item with id.
     */
    public Item add(Item item) {
        item.setId(ids++);
        items[size++] = item;
        return item;
    }

    /**
     * Find item by id.
     * @param id    - id of item.
     * @return      - item.
     */
    public Item findById(int id) {
        int index = indexOf(id);
        return index != -1 ? items[index] : null;
    }

    /**
     * Get array of all items.
     * @return  - items.
     */
    public Item[] findAll() {
        return Arrays.copyOf(items, size);
    }

    /**
     * Get array of items by name.
     * @param key   - name of item.
     * @return      - array of items.
     */
    public Item[] findByName(String key) {
        Item[] rsl = new Item[size];
        int rslIndex = 0;
        for (int index = 0; index < size; index++) {
            if (key.equals(items[index].getName())) {
                rsl[rslIndex++] = items[index];
            }
        }
        return Arrays.copyOf(rsl, rslIndex);
    }

    public boolean replace(int id, Item item) {
        int index = indexOf(id);
        if (index == -1) {
            return false;
        }
        item.setId(id);
        items[index] = item;
        return true;
    }

    private int indexOf(int id) {
        int result = -1;
        for (int index = 0; index < size; index++) {
            if (items[index].getId() == id) {
                result = index;
                break;
            }
        }
        return result;
    }
}