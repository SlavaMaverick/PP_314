package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Repository
public interface UserRepository{
    User findUserByEmail(String email);
    void save(User user);
    void deleteById(int id);
    void update(User user);
    User getById(int id);
    List<User> findAll();
}