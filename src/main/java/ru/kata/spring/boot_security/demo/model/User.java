package ru.kata.spring.boot_security.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@Component
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 35, message = "Name should contain up to 35 characters")
    @Column(name = "name")
    private String name;

    @Column(name = "age")
    @Min(value = 0, message = "Age cannot be negative")
    @Max(value = 122, message = "Age should be less than 122")
    private int age;

    @Column(name = "country")
    @NotEmpty(message = "Country should not be empty")
    private String country;

    @Column(unique = true, updatable = false, name = "email")
    @NotEmpty(message = "Username should not be empty")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Password should not be empty")
    private String password;

//     и так можно, работает:
//    @ManyToMany(fetch = FetchType.LAZY)
//    @Fetch(FetchMode.JOIN)
//    @JoinTable (name = "users_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new java.util.LinkedHashSet<>();

    public User(String name, int age, String country, String email, String password, Set<Role> roles) {
        this.name = name;
        this.age = age;
        this.country = country;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Set<? extends SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities =
                getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                        .collect(Collectors.toSet());
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email ;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}