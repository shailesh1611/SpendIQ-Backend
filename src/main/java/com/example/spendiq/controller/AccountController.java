package com.example.spendiq.controller;

import com.example.spendiq.api.response.StatusOk;
import com.example.spendiq.dto.account.AddAccountRequestDTO;
import com.example.spendiq.entity.Account;
import com.example.spendiq.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountMapper accountMapper;

    @Autowired
    public AccountController(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }


    @PostMapping
    ResponseEntity<?> addAccount(@RequestBody AddAccountRequestDTO requestDTO) {
        Account account = accountMapper.toAccount(requestDTO);
        return new ResponseEntity<>(new StatusOk<>(account), HttpStatus.OK);
    }
}
