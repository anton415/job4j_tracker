package ru.job4j.tracker.input;

/**
 * @author Anton Serdyuchenko
 * @version 1.0
 * @since 25.09.2025 08:55
 */
public class ValidateInput extends ConsoleInput {

    @Override
    public int askInt(String question) {
        boolean invalid = true;
        int value = -1;
        do {
            try {
                value = super.askInt(question);
                invalid = false;
            } catch (NumberFormatException nfe) {
                System.out.println("Пожалуйста, введите корректные данные");
            }
        } while (invalid);
        return value;
    }
}
