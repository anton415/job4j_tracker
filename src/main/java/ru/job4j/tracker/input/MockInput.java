package ru.job4j.tracker.input;

/**
 * @author Anton Serdyuchenko
 * @version 1.0
 * @since 22.09.2025 11:42
 */
public class MockInput implements Input {
    private String[] answers;
    private int position = 0;

    public MockInput(String[] answers) {
        this.answers = answers;
    }

    @Override
    public String askStr(String question) {
        return answers[position++];
    }

    @Override
    public int askInt(String question) {
        return Integer.parseInt(askStr(question));
    }
}
