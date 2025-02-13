package com.example.spendiq.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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

    @Column(nullable = false, unique = true)  // Ensures username is unique
    private String userName;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)  // Maps the list of roles as a separate table
    private List<String> roles;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
