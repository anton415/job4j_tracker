package ru.job4j.tracker;

import ru.job4j.tracker.action.*;
import ru.job4j.tracker.input.ConsoleInput;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.output.ConsoleOutput;
import ru.job4j.tracker.output.Output;

/**
 * @author antonserdyuchenko
 * @since 20.09.2025
 */
public class StartUI {
    private final Output output;

    public StartUI(Output output) {
        this.output = output;
    }

    /**
     * Initializes the application and starts executing various user actions.
     */
    public void init(Input input, Tracker tracker, UserAction[] actions) {
        boolean run = true;
        while (run) {
            showMenu(actions);
            int select = input.askInt("Выбрать: ");
            UserAction action = actions[select];
            run = action.execute(input, tracker);
        }
    }

    /**
     * Displays a menu of available user actions.
     */
    private void showMenu(UserAction[] actions) {
        System.out.println("Меню:");
        for (int i = 0; i < actions.length; i++) {
            System.out.println(i + ". " + actions[i].name());
        }
    }

    /**
     * Launches application.
     */
    public static void main(String[] args) {
        Output output = new ConsoleOutput();
        Input input = new ConsoleInput();
        Tracker tracker = new Tracker();
        UserAction[] actions = {
                new CreateAction(output),
                new FindAllAction(output),
                new ReplaceAction(output),
                new DeleteAction(output),
                new FindByIdAction(output),
                new FindByNameAction(output),
                new ExitAction(output)
        };
        new StartUI(output).init(input, tracker, actions);
    }
}
