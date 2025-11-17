package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {
    public User findUserByName(String name);
    public List<User> getAllUsers();
    public String findEmailByName(String name);
    public void saveUser(User user);
    public User getUser(int id);
    public void deleteUser(int id);
    public void saveUserWithRole(User user);
    public void updateUserWithRoles(int id, User user);

}
