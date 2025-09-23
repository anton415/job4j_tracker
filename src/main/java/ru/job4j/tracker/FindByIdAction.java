package ru.job4j.tracker;

/**
 * @author Anton Serdyuchenko
 * @version 1.0
 * @since 23.09.2025 13:56
 */
public class FindByIdAction implements UserAction {
    @Override
    public String name() {
        return "Показать заявку по id";
    }

    @Override
    public boolean execute(Input input, Tracker tracker) {
        System.out.println("=== Вывод заявки по id ===");
        System.out.print("Введите id: ");
        int id = input.askInt("");
        Item item = tracker.findById(id);
        if (item != null) {
            System.out.println(item);
        } else {
            System.out.println("Заявка с введенным id: " + id + " не найдена.");
        }
        return true;
    }
}
