package ru.job4j.tracker;

/**
 * @author Anton Serdyuchenko
 * @version 1.0
 * @since 22.09.2025 11:42
 */
public class MockInput implements Input {

    @Override
    public String askStr(String question) {
        return "";
    }

    @Override
    public int askInt(String question) {
        return 0;
    }
}
