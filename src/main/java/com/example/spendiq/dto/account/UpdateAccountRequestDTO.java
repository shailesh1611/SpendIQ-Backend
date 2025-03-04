package com.example.spendiq.dto.account;

import com.example.spendiq.enums.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountRequestDTO {
    private String accountName;

    private Boolean isDefault;
    private AccountType accountType;
}
