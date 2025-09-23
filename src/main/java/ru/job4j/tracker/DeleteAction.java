package ru.job4j.tracker;

/**
 * @author Anton Serdyuchenko
 * @version 1.0
 * @since 23.09.2025 13:55
 */
public class DeleteAction implements UserAction {
    @Override
    public String name() {
        return "Удалить заявку";
    }

    @Override
    public boolean execute(Input input, Tracker tracker) {
        System.out.println("=== Удаление заявки ===");
        System.out.print("Введите id: ");
        int id = input.askInt("");
        Item item = tracker.findById(id);
        tracker.delete(id);
        System.out.println(item != null ? "Заявка удалена успешно." : "Ошибка удаления заявки.");
        return true;
    }
}
