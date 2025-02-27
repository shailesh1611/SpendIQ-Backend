package com.example.spendiq.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;

    @Column(nullable = false)
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
