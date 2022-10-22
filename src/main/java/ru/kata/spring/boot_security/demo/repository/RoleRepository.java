package ru.kata.spring.boot_security.demo.repository;


import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import java.util.List;
import java.util.Set;


@Repository
public interface RoleRepository{
    List<Role> findAllRoles();

    Role getRole(String role);

    public Set<Role> getSetOfRoles(String[] roleNames);
}

