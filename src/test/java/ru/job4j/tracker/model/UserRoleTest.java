package ru.job4j.tracker.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRoleTest {
    @Test
    public void whenNewRolesHaveNoIdsThenTheyAreNotEqual() {
        assertThat(new Role()).isNotEqualTo(new Role());
    }

    @Test
    public void whenRolesHaveSameIdThenTheyAreEqual() {
        Role first = new Role();
        Role second = new Role();
        first.setId(1);
        second.setId(1);
        assertThat(first).isEqualTo(second);
    }

    @Test
    public void whenNewUsersHaveNoIdsThenTheyAreNotEqual() {
        assertThat(new User()).isNotEqualTo(new User());
    }

    @Test
    public void whenUsersHaveSameIdThenTheyAreEqual() {
        User first = new User();
        User second = new User();
        first.setId(1);
        second.setId(1);
        assertThat(first).isEqualTo(second);
    }
}
