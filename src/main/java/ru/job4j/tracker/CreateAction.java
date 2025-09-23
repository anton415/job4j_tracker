package ru.job4j.tracker;

/**
 * @author Anton Serdyuchenko
 * @version 1.0
 * @since 23.09.2025 13:49
 */
public class CreateAction implements UserAction {
    @Override
    public String name() {
        return "Добавить новую заявку";
    }

    @Override
    public boolean execute(Input input, Tracker tracker) {
        System.out.println("=== Создание новой заявки ===");
        String name = input.askStr("Введите имя: ");
        Item item = new Item(name);
        tracker.add(item);
        System.out.println("Добавленная заявка: " + item);
        return true;
    }
}