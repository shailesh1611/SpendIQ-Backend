package com.example.spendiq.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")  // Defines table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Generates a unique UUID for each user
    private UUID userId;

    @Column(nullable = false, unique = true)  // Ensures email is unique
    @Email(message="Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean emailVerified;

    public void addRole(Role role) {
        this.roles.add(role);
        role.setUser(this);
    }
}
