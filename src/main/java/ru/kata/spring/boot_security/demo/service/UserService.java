package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

@Service
public interface UserService {

    User findUserByEmail(String email);

    void save(User user);

    @Transactional
    void update(int id, User updateUser);

    void deleteById(int id);

    User getById(int id);

    List<User> findAll();

}
