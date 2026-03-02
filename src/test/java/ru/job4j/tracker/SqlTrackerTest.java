package ru.job4j.tracker;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SqlTrackerTest {

    private static Connection connection;

    @BeforeAll
    public static void initConnection() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("db/liquibase_test.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @AfterEach
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM items")) {
            statement.execute();
        }
    }

    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        assertThat(tracker.findById(item.getId())).isEqualTo(item);
    }

    @Test
    public void whenFindByIdOfMissingItemThenReturnNull() {
        SqlTracker tracker = new SqlTracker(connection);
        assertThat(tracker.findById(999)).isNull();
    }

    @Test
    public void whenFindAllThenReturnAllItemsOrderedById() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = new Item("item1");
        Item item2 = new Item("item2");
        tracker.add(item1);
        tracker.add(item2);
        assertThat(tracker.findAll()).containsExactly(item1, item2);
    }

    @Test
    public void whenFindAllOnEmptyTrackerThenReturnEmptyArray() {
        SqlTracker tracker = new SqlTracker(connection);
        assertThat(tracker.findAll()).isEmpty();
    }

    @Test
    public void whenFindByNameThenReturnOnlyMatchingItemsOrderedById() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = new Item("item");
        Item item2 = new Item("item");
        Item item3 = new Item("other");
        tracker.add(item1);
        tracker.add(item2);
        tracker.add(item3);
        assertThat(tracker.findByName("item")).containsExactly(item1, item2);
    }

    @Test
    public void whenFindByNameOfMissingItemThenReturnEmptyArray() {
        SqlTracker tracker = new SqlTracker(connection);
        assertThat(tracker.findByName("missing")).isEmpty();    
    }

    @Test
    public void whenReplaceExistingItemThenReturnTrueAndUpdateStoredItem() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        Item updatedItem = new Item("updated");
        boolean replaced = tracker.replace(item.getId(), updatedItem);
        assertThat(replaced).isTrue();
        updatedItem.setId(item.getId());
        assertThat(tracker.findById(item.getId())).isEqualTo(updatedItem);
    }

    @Test
    public void whenReplaceExistingItemThenReplacementGetsTargetId() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        Item updatedItem = new Item("updated");
        tracker.replace(item.getId(), updatedItem);
        assertThat(updatedItem.getId()).isEqualTo(item.getId());
    }

    @Test
    public void whenReplaceMissingItemThenReturnFalse() {
        SqlTracker tracker = new SqlTracker(connection);
        Item updatedItem = new Item("updated");
        boolean replaced = tracker.replace(999, updatedItem);
        assertThat(replaced).isFalse();
    }

    @Test
    public void whenDeleteExistingItemThenReturnTrueAndItemBecomesUnavailable() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        boolean deleted = tracker.delete(item.getId());
        assertThat(deleted).isTrue();
        assertThat(tracker.findById(item.getId())).isNull();
    }

    @Test
    public void whenDeleteMissingItemThenReturnFalse() {
        SqlTracker tracker = new SqlTracker(connection);
        boolean deleted = tracker.delete(999);
        assertThat(deleted).isFalse();
    }

    @Test
    public void whenCloseTrackerWithExternalConnectionThenConnectionStaysOpen() throws Exception {
        Connection externalConnection = connection;
        SqlTracker tracker = new SqlTracker(externalConnection);
        tracker.close();
        assertThat(externalConnection.isClosed()).isFalse();
    }
}