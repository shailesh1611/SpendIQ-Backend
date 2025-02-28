package com.example.spendiq.dto.account;

import com.example.spendiq.enums.AccountType;
import com.example.spendiq.enums.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAccountRequestDTO {
    private String accountName;
    private AccountType accountType;
    private boolean isDefault;
    private double amount;
    private Currency currency;
}
