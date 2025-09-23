package ru.job4j.tracker;

/**
 * @author Anton Serdyuchenko
 * @version 1.0
 * @since 23.09.2025 13:50
 */
public class FindAllAction implements UserAction {
    @Override
    public String name() {
        return "Показать все заявки";
    }

    @Override
    public boolean execute(Input input, Tracker tracker) {
        System.out.println("=== Вывод всех заявок ===");
        Item[] items = tracker.findAll();
        if (items.length > 0) {
            for (Item item : items) {
                System.out.println(item);
            }
        } else {
            System.out.println("Хранилище еще не содержит заявок");
        }
        return true;
    }
}
