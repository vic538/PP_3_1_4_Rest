package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByAuthority(String authority);
    List<Role> findAll();

}
