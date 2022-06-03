package ru.job4j.tracker.action;

import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.Store;

public class CreateHundredActions implements UserAction {

    private final Output out;

    public CreateHundredActions(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "=== Create one hundred new Items ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        for (int i = 0; i < 100; i++) {
            tracker.add(new Item("item " + i));
            out.println("Item " + i + " successfully added!");
        }
        return true;
    }
}
