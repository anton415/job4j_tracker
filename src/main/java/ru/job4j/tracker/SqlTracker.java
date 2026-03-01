package ru.job4j.tracker;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * SQL implementation of tracker storage.
 *
 * @author Anton Serdyuchenko
 * @since 01.03.2026
 */
public class SqlTracker extends Tracker implements AutoCloseable {
    private static final String DEFAULT_PROPERTIES = "db/liquibase.properties";
    private static final String DEFAULT_CHANGELOG = "db/dbchangelog.xml";
    private static final String RESOURCE_PREFIX = "src/main/resources/";

    private final Connection cn;
    private final boolean closeConnection;

    public SqlTracker() {
        this(loadProperties(DEFAULT_PROPERTIES));
    }

    public SqlTracker(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
            closeConnection = true;
            initDatabase(cfg.getProperty("changeLogFile"));
        } catch (ClassNotFoundException | SQLException e) {
            throw new IllegalStateException("Cannot initialize SQL tracker", e);
        }
    }

    public SqlTracker(Connection cn) {
        this.cn = cn;
        this.closeConnection = false;
    }

    @Override
    public Item add(Item item) {
        try (PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO items(name, created) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, item.getName());
            ps.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    item.setId(keys.getInt(1));
                }
            }
            return item;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot add item", e);
        }
    }

    @Override
    public Item findById(int id) {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id, name, created FROM items WHERE id = ?"
        )) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? createItem(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find item by id", e);
        }
    }

    @Override
    public Item[] findAll() {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id, name, created FROM items ORDER BY id"
        );
             ResultSet rs = ps.executeQuery()) {
            List<Item> items = new ArrayList<>();
            while (rs.next()) {
                items.add(createItem(rs));
            }
            return items.toArray(new Item[0]);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot get all items", e);
        }
    }

    @Override
    public Item[] findByName(String key) {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id, name, created FROM items WHERE name = ? ORDER BY id"
        )) {
            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                List<Item> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(createItem(rs));
                }
                return items.toArray(new Item[0]);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot find items by name", e);
        }
    }

    @Override
    public boolean replace(int id, Item item) {
        try (PreparedStatement ps = cn.prepareStatement(
                "UPDATE items SET name = ?, created = ? WHERE id = ?"
        )) {
            ps.setString(1, item.getName());
            ps.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            ps.setInt(3, id);
            boolean updated = ps.executeUpdate() > 0;
            if (updated) {
                item.setId(id);
            }
            return updated;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot replace item", e);
        }
    }

    @Override
    public boolean delete(int id) {
        try (PreparedStatement ps = cn.prepareStatement(
                "DELETE FROM items WHERE id = ?"
        )) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot delete item", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (closeConnection) {
            cn.close();
        }
    }

    private void initDatabase(String changeLogFile) {
        String changeLog = normalizeResourcePath(changeLogFile);
        try {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(cn));
            Liquibase liquibase = new Liquibase(
                    changeLog,
                    new ClassLoaderResourceAccessor(),
                    database
            );
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException e) {
            throw new IllegalStateException("Cannot apply database migrations", e);
        }
    }

    private Item createItem(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("created");
        LocalDateTime created = timestamp == null ? LocalDateTime.now() : timestamp.toLocalDateTime();
        return new Item(rs.getInt("id"), rs.getString("name"), created);
    }

    private String normalizeResourcePath(String path) {
        if (path == null || path.isBlank()) {
            return DEFAULT_CHANGELOG;
        }
        return path.startsWith(RESOURCE_PREFIX)
                ? path.substring(RESOURCE_PREFIX.length())
                : path;
    }

    private static Properties loadProperties(String path) {
        Properties cfg = new Properties();
        try (InputStream input = SqlTracker.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new IllegalStateException("Properties resource not found: " + path);
            }
            cfg.load(input);
            return cfg;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load properties: " + path, e);
        }
    }
}
