package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("Select u from User u left join fetch u.roles where u.email=:email")
    User findUserByEmail(String email);
    String findEmailByName(String name);
    User findUserByName(String name);
    User findUserById(int id);








}
