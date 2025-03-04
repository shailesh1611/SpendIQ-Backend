package com.example.spendiq.mapper;

import com.example.spendiq.dto.account.AccountResponseDTO;
import com.example.spendiq.dto.account.AddAccountRequestDTO;
import com.example.spendiq.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(AddAccountRequestDTO requestDTO);
    AccountResponseDTO toAccountResponseDTO(Account account);
    List<AccountResponseDTO> toListOfAccountResponseDTO(List<Account> accounts);
}
