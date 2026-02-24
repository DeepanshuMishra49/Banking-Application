package com.example.Banking_Application.service;

import com.example.Banking_Application.entity.Account;
import com.example.Banking_Application.entity.User;
import com.example.Banking_Application.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> getAccountsByUser(User user) {
        return accountRepository.findByUserOrderByAccountType(user);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    @Transactional
    public Account createAccount(User user, Account.AccountType type) {
        String accountNumber = generateAccountNumber();
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(type)
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();
        return accountRepository.save(account);
    }

    public boolean userOwnsAccount(User user, Long accountId) {
        Account account = getAccountById(accountId);
        return account.getUser().getId().equals(user.getId());
    }

    private String generateAccountNumber() {
        String num;
        do {
            num = "ACC" + System.currentTimeMillis() % 10000000000L;
            if (num.length() < 13) num = "ACC" + String.format("%010d", System.currentTimeMillis() % 10000000000L);
        } while (accountRepository.existsByAccountNumber(num));
        return num;
    }
}
