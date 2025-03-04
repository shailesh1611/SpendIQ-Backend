package com.example.spendiq.controller;

import com.example.spendiq.api.response.StatusOk;
import com.example.spendiq.dto.account.AddAccountRequestDTO;
import com.example.spendiq.dto.account.AccountResponseDTO;
import com.example.spendiq.dto.account.GetAllAccountResponseDTO;
import com.example.spendiq.dto.account.UpdateAccountRequestDTO;
import com.example.spendiq.entity.Account;
import com.example.spendiq.entity.User;
import com.example.spendiq.mapper.AccountMapper;
import com.example.spendiq.services.AccountService;
import com.example.spendiq.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountMapper accountMapper;
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountMapper accountMapper, UserService userService, AccountService accountService) {
        this.accountMapper = accountMapper;
        this.userService = userService;
        this.accountService = accountService;
    }


    @PostMapping
    ResponseEntity<StatusOk<AccountResponseDTO>> addAccount(@RequestBody @Valid AddAccountRequestDTO requestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Account account = accountMapper.toAccount(requestDTO);
        Account addedAccount = accountService.addAccount(account, user);
        AccountResponseDTO responseData = accountMapper.toAccountResponseDTO(addedAccount);
        return new ResponseEntity<>(new StatusOk<>(responseData), HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<StatusOk<GetAllAccountResponseDTO>> getAllAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Account> accounts = accountService.getAllAccount(user.getUserId());
        List<AccountResponseDTO> accountResponseDTOList = accountMapper.toListOfAccountResponseDTO(accounts);
        GetAllAccountResponseDTO response = new GetAllAccountResponseDTO(accountResponseDTOList);
        return new ResponseEntity<>(new StatusOk<>(response),HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    ResponseEntity<StatusOk<AccountResponseDTO>> getAccountByAccountId(@PathVariable String accountId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Account account = accountService.findAccountById(UUID.fromString(accountId), user.getUserId());
        AccountResponseDTO response = accountMapper.toAccountResponseDTO(account);
        return new ResponseEntity<>(new StatusOk<>(response),HttpStatus.OK);
    }

    @PutMapping("/{accountId}")
    ResponseEntity<StatusOk<AccountResponseDTO>> updateAccountByAccountId(@PathVariable String accountId, @RequestBody UpdateAccountRequestDTO requestData) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Account account = accountService.updateAccount(UUID.fromString(accountId), requestData, user);
        AccountResponseDTO response = accountMapper.toAccountResponseDTO(account);
        return new ResponseEntity<>(new StatusOk<>(response), HttpStatus.OK);
    }
}
