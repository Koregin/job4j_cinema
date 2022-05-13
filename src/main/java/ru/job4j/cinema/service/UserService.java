package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.store.UserStore;

import java.util.Optional;

@Service
public class UserService {
    private final UserStore store;

    public UserService(UserStore store) {
        this.store = store;
    }

    public Optional<User> add(User user) {
        return store.create(user);
    }

    public Optional<User> findUserByEmail(String username, String email) {
        return store.findUserByEmail(username, email);
    }
}
