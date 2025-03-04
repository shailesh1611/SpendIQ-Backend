package com.example.spendiq.dto.account;

import com.example.spendiq.enums.AccountType;
import com.example.spendiq.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAccountRequestDTO {
    @NotBlank(message = "Account Name is Required")
    @NotNull(message = "Account Name is Required")
    private String accountName;

    @NotNull(message = "Account Type is Required")
    private AccountType accountType;

    private Boolean isDefault;

    @NotNull(message = "Amount is Required")
    @Positive(message = "Amount should be greater than 0")
    private double amount;

    @NotNull(message = "Currency is Required")
    private Currency currency;
}
