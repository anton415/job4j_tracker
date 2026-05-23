package ru.job4j.tracker;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.function.Function;

public class HbmTracker implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        tx(session -> session.save(item));
        return item;
    }

    @Override
    public boolean replace(Integer id, Item item) {
        boolean rsl = tx(session -> session.createQuery(
                        "UPDATE Item SET name = :name, created = :created WHERE id = :id"
                )
                .setParameter("name", item.getName())
                .setParameter("created", item.getCreated())
                .setParameter("id", id)
                .executeUpdate() > 0);
        if (rsl) {
            item.setId(id);
        }
        return rsl;
    }

    @Override
    public boolean delete(Integer id) {
        return tx(session -> session.createQuery("DELETE FROM Item WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate() > 0);
    }

    @Override
    public List<Item> findAll() {
        return tx(session -> session.createQuery(
                "FROM Item ORDER BY id", Item.class
        ).list());
    }

    @Override
    public List<Item> findByName(String key) {
        return tx(session -> session.createQuery(
                        "FROM Item WHERE name = :key ORDER BY id", Item.class
                )
                .setParameter("key", key)
                .list());
    }

    @Override
    public Item findById(Integer id) {
        return tx(session -> session.get(Item.class, id));
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    private <T> T tx(Function<Session, T> command) {
        Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            transaction.commit();
            return rsl;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}
