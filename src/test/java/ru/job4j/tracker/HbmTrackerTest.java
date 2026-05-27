package ru.job4j.tracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HbmTrackerTest {
    @BeforeEach
    @AfterEach
    public void wipeTable() throws Exception {
        try (var tracker = new HbmTracker()) {
            tracker.findAll().forEach(item -> tracker.delete(item.getId()));
        }
    }

    @Test
    public void whenAddNewItemThenTrackerHasSameItem() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = new Item("test1");
            tracker.add(item);
            Item result = tracker.findById(item.getId());
            assertThat(result.getName()).isEqualTo(item.getName());
        }
    }

    @Test
    public void whenFindAllThenReturnAllItemsOrderedById() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item first = tracker.add(new Item("first"));
            Item second = tracker.add(new Item("second"));
            assertThat(tracker.findAll()).extracting(Item::getId)
                    .containsExactly(first.getId(), second.getId());
        }
    }

    @Test
    public void whenFindByNameThenReturnOnlyMatchingItemsOrderedById() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item first = tracker.add(new Item("item"));
            tracker.add(new Item("other"));
            Item second = tracker.add(new Item("item"));
            assertThat(tracker.findByName("item")).extracting(Item::getId)
                    .containsExactly(first.getId(), second.getId());
        }
    }

    @Test
    public void whenReplaceThenStoredItemUpdated() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = tracker.add(new Item("item"));
            Item update = new Item("update");
            assertThat(tracker.replace(item.getId(), update)).isTrue();
            assertThat(update.getId()).isEqualTo(item.getId());
            assertThat(tracker.findById(item.getId()).getName()).isEqualTo(update.getName());
        }
    }

    @Test
    public void whenReplaceMissingItemThenReturnFalse() throws Exception {
        try (var tracker = new HbmTracker()) {
            assertThat(tracker.replace(999, new Item("update"))).isFalse();
        }
    }

    @Test
    public void whenDeleteThenItemUnavailable() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = tracker.add(new Item("item"));
            assertThat(tracker.delete(item.getId())).isTrue();
            assertThat(tracker.findById(item.getId())).isNull();
        }
    }

    @Test
    public void whenDeleteMissingItemThenReturnFalse() throws Exception {
        try (var tracker = new HbmTracker()) {
            assertThat(tracker.delete(999)).isFalse();
        }
    }
}
