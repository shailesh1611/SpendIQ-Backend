package com.example.spendiq.dto.account;

import com.example.spendiq.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAllAccountResponseDTO {
    List<AccountResponseDTO> accounts;
}
