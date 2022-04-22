package ru.job4j.tracker.store;

import org.junit.*;
import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class SqlTrackerTest {

    static Connection connection;

    @BeforeClass
    public static void initConnection() {
        try (InputStream in = SqlTrackerTest.class.getClassLoader().getResourceAsStream("test.properties")) {
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

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @After
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
    }

    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = tracker.add(new Item("item"));
        assertThat(tracker.findById(item.getId()), is(item));
    }

    @Test
    public void whenFindAll() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = tracker.add(new Item("first"));
        Item item2 = tracker.add(new Item("second"));
        assertThat(tracker.findAll(), is(List.of(item1, item2)));
    }

    @Test
    public void whenFindByName() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = tracker.add(new Item("first"));
        tracker.add(new Item("second"));
        Item item3 = tracker.add(new Item("first"));
        assertThat(tracker.findByName("first"), is(List.of(item1, item3)));
    }

    @Test
    public void whenFindById() {
        SqlTracker tracker = new SqlTracker(connection);
        tracker.add(new Item("first"));
        Item item2 = tracker.add(new Item("second"));
        tracker.add(new Item("first"));
        Item result = tracker.findById(item2.getId());
        assertThat(result, is(item2));
    }

    @Test
    public void whenFindByIdNotFound() {
        SqlTracker tracker = new SqlTracker(connection);
        tracker.add(new Item("first"));
        tracker.add(new Item("second"));
        tracker.add(new Item("first"));
        Item result = tracker.findById(-1);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenReplace() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = tracker.add(new Item("first"));
        tracker.replace(item1.getId(), new Item("second"));
        assertThat(tracker.findById(item1.getId()).getName(), is("second"));
    }

    @Test
    public void whenDelete() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = tracker.add(new Item("first"));
        tracker.delete(item1.getId());
        assertThat(tracker.findById(item1.getId()), is(nullValue()));
    }

}