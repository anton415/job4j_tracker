package ru.job4j.tracker.action;

import org.junit.jupiter.api.Test;
import ru.job4j.tracker.Item;
import ru.job4j.tracker.Tracker;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.output.Output;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActionMockitoTest {
    @Test
    void whenDeleteActionDeletesFoundItem() {
        Output output = mock(Output.class);
        Input input = mock(Input.class);
        Tracker tracker = mock(Tracker.class);
        Item item = new Item("Deleted item");
        when(input.askInt("")).thenReturn(1);
        when(tracker.findById(1)).thenReturn(item);
        DeleteAction action = new DeleteAction(output);

        boolean result = action.execute(input, tracker);

        assertThat(result).isTrue();
        verify(output).println("=== Удаление заявки ===");
        verify(output).println("Введите id: ");
        verify(tracker).findById(1);
        verify(tracker).delete(1);
        verify(output).println("Заявка удалена успешно.");
    }

    @Test
    void whenDeleteActionDoesNotFindItem() {
        Output output = mock(Output.class);
        Input input = mock(Input.class);
        Tracker tracker = mock(Tracker.class);
        when(input.askInt("")).thenReturn(100);
        when(tracker.findById(100)).thenReturn(null);
        DeleteAction action = new DeleteAction(output);

        boolean result = action.execute(input, tracker);

        assertThat(result).isTrue();
        verify(output).println("=== Удаление заявки ===");
        verify(output).println("Введите id: ");
        verify(tracker).findById(100);
        verify(tracker).delete(100);
        verify(output).println("Ошибка удаления заявки.");
    }

    @Test
    void whenFindByIdActionFindsItem() {
        Output output = mock(Output.class);
        Input input = mock(Input.class);
        Tracker tracker = mock(Tracker.class);
        Item item = new Item("Found item");
        when(input.askInt("")).thenReturn(1);
        when(tracker.findById(1)).thenReturn(item);
        FindByIdAction action = new FindByIdAction(output);

        boolean result = action.execute(input, tracker);

        assertThat(result).isTrue();
        verify(output).println("=== Вывод заявки по id ===");
        verify(output).println("Введите id: ");
        verify(tracker).findById(1);
        verify(output).println(item);
    }

    @Test
    void whenFindByIdActionDoesNotFindItem() {
        Output output = mock(Output.class);
        Input input = mock(Input.class);
        Tracker tracker = mock(Tracker.class);
        when(input.askInt("")).thenReturn(100);
        when(tracker.findById(100)).thenReturn(null);
        FindByIdAction action = new FindByIdAction(output);

        boolean result = action.execute(input, tracker);

        assertThat(result).isTrue();
        verify(output).println("=== Вывод заявки по id ===");
        verify(output).println("Введите id: ");
        verify(tracker).findById(100);
        verify(output).println("Заявка с введенным id: 100 не найдена.");
    }

    @Test
    void whenFindByNameActionFindsItems() {
        Output output = mock(Output.class);
        Input input = mock(Input.class);
        Tracker tracker = mock(Tracker.class);
        Item first = new Item("Found item");
        Item second = new Item("Found item");
        when(input.askStr("")).thenReturn("Found item");
        when(tracker.findByName("Found item")).thenReturn(new Item[] {first, second});
        FindByNameAction action = new FindByNameAction(output);

        boolean result = action.execute(input, tracker);

        assertThat(result).isTrue();
        verify(output).println("=== Вывод заявок по имени ===");
        verify(output).println("Введите имя: ");
        verify(tracker).findByName("Found item");
        verify(output).println(first);
        verify(output).println(second);
    }

    @Test
    void whenFindByNameActionDoesNotFindItems() {
        Output output = mock(Output.class);
        Input input = mock(Input.class);
        Tracker tracker = mock(Tracker.class);
        when(input.askStr("")).thenReturn("Missing item");
        when(tracker.findByName("Missing item")).thenReturn(new Item[0]);
        FindByNameAction action = new FindByNameAction(output);

        boolean result = action.execute(input, tracker);

        assertThat(result).isTrue();
        verify(output).println("=== Вывод заявок по имени ===");
        verify(output).println("Введите имя: ");
        verify(tracker).findByName("Missing item");
        verify(output).println("Заявки с именем: Missing item не найдены.");
    }
}
