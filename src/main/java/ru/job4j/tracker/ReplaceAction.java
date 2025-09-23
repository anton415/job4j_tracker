package ru.job4j.tracker;

/**
 * @author Anton Serdyuchenko
 * @version 1.0
 * @since 23.09.2025 13:52
 */
public class ReplaceAction implements UserAction {
    @Override
    public String name() {
        return "Изменить заявку";
    }

    @Override
    public boolean execute(Input input, Tracker tracker) {
        System.out.println("=== Редактирование заявки ===");
        System.out.print("Введите id: ");
        int id = input.askInt("");
        System.out.print("Введите имя: ");
        String name = input.askStr("");
        Item item = new Item(name);
        if (tracker.replace(id, item)) {
            System.out.println("Заявка изменена успешно.");
        } else {
            System.out.println("Ошибка замены заявки.");
        }
        return true;
    }
}
