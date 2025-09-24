package ru.job4j.tracker.output;

/**
 * @author Anton Serdyuchenko
 * @version 1.0
 * @since 23.09.2025 14:35
 */
public class StubOutput implements Output {
    private final StringBuilder buffer = new StringBuilder();

    @Override
    public void println(Object object) {
        if (object != null) {
            buffer.append(object.toString());
        } else {
            buffer.append("null");
        }
        buffer.append(System.lineSeparator());
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
}
