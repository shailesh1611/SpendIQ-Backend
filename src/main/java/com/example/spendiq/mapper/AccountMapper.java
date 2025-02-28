package com.example.spendiq.mapper;

import com.example.spendiq.dto.account.AddAccountRequestDTO;
import com.example.spendiq.entity.Account;

public interface AccountMapper {
    Account toAccount(AddAccountRequestDTO requestDTO);
}
