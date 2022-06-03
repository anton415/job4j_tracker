package ru.job4j.tracker.action;

import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.Store;

public class DeleteHundredActions implements UserAction {

    private final Output out;

    public DeleteHundredActions(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "=== Delete one hundred items ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        for (int i = 0; i < 100; i++) {
            if (tracker.delete(i)) {
                out.println("Item is successfully deleted!");
            } else {
                out.println("Wrong id!");
            }
        }
        return true;
    }
}
