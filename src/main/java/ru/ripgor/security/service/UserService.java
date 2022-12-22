package ru.ripgor.security.service;

import ru.ripgor.security.model.User;

import java.util.List;

public interface UserService {

    void saveUser(User user);

    List<User> getAllUsers();

    User getUser(int id);

    User getUser(String name);

    void updateUser(User user);

    void deleteUser(int id);
}
