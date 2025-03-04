package com.example.spendiq.dto.account;

import com.example.spendiq.enums.AccountType;
import com.example.spendiq.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AccountResponseDTO {
    private UUID accountId;
    private String accountName;
    private AccountType accountType;
    private Boolean isDefault;
    private double amount;
    private Currency currency;
    private LocalDateTime createdAt;
}
