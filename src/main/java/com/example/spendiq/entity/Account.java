package com.example.spendiq.entity;

import com.example.spendiq.enums.AccountType;
import com.example.spendiq.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"accountName","userId"})
})
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;

    @NotBlank(message = "Account Name is required")
    @Column(nullable = false)
    private String accountName;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private LocalDateTime createdAt;
    private Boolean isDefault;

    @NotNull(message = "Amount is Required")
    @Positive(message = "Amount must be greater than 0")
    private double amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
