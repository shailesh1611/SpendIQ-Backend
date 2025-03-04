package com.example.spendiq.services;

import com.example.spendiq.dto.account.UpdateAccountRequestDTO;
import com.example.spendiq.entity.Account;
import com.example.spendiq.entity.User;
import com.example.spendiq.exception.AccountAlreadyExistsException;
import com.example.spendiq.exception.AccountNotFoundException;
import com.example.spendiq.repository.AccountRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Transactional
    public Account addAccount(Account account, User user) {

        boolean exist = accountRepository.existsByAccountNameAndUser(account.getAccountName(), user);
        if(exist) {
            throw new AccountAlreadyExistsException("Account Already exists with Account Name : "
                    + account.getAccountName());
        }

        account.setCreatedAt(LocalDateTime.now());
        if (Boolean.TRUE.equals(account.getIsDefault())) {
            accountRepository.checkAndUnsetDefaultAccount(user.getUserId());
            account.setIsDefault(true);
        } else if (user.getAccounts().isEmpty()) {
            account.setIsDefault(true);
        }
        user.addAccount(account);
        return accountRepository.save(account);
    }

    public List<Account> getAllAccount(UUID userId) {
        return accountRepository.findAllByUserUserId(userId).orElseGet(ArrayList::new);
    }

    public Account findAccountById(UUID accountId, UUID userId) {
        return accountRepository.findById(accountId)
                .filter(account -> account.getUser().getUserId().equals(userId))
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found with Account ID : "+accountId));
    }


    @Transactional
    public Account updateAccount(UUID accountId, @Valid UpdateAccountRequestDTO requestData, User user) {
        Account account = findAccountById(accountId, user.getUserId());
        boolean isUpdated = false;

        if(requestData.getAccountName() != null && !requestData.getAccountName().trim().isEmpty()
                && !requestData.getAccountName().equals(account.getAccountName())) {

            boolean exist = accountRepository.existsByAccountNameAndUser(requestData.getAccountName(), user);
            if(exist) {
                throw new AccountAlreadyExistsException("Account Already exists with Account Name : "
                        + requestData.getAccountName());
            }
            account.setAccountName(requestData.getAccountName());
            isUpdated = true;
        }

        if(Boolean.TRUE.equals(requestData.getIsDefault()) && Boolean.FALSE.equals(account.getIsDefault())) {
            accountRepository.checkAndUnsetDefaultAccount(user.getUserId());
            account.setIsDefault(true);
            isUpdated = true;
        }

        if(requestData.getAccountType() != null && !requestData.getAccountType().equals(account.getAccountType())) {
            account.setAccountType(requestData.getAccountType());
            isUpdated = true;
        }

        return isUpdated ? accountRepository.save(account) : account;
    }
}