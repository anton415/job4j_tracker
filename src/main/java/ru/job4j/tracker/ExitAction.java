package ru.job4j.tracker;

/**
 * @author Anton Serdyuchenko
 * @version 1.0
 * @since 23.09.2025 13:50
 */
public class ExitAction implements UserAction {
    @Override
    public String name() {
        return "Завершить программу";
    }

    @Override
    public boolean execute(Input input, Tracker tracker) {
        System.out.println("=== Завершение программы ===");
        return false;
    }
}