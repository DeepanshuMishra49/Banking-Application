package com.example.Banking_Application.service;

import com.example.Banking_Application.dto.TransactionDto;
import com.example.Banking_Application.entity.Account;
import com.example.Banking_Application.entity.Transaction;
import com.example.Banking_Application.entity.User;
import com.example.Banking_Application.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Transactional
    public Transaction deposit(Account account, TransactionDto dto) {
        account.setBalance(account.getBalance().add(dto.getAmount()));
        Transaction tx = Transaction.builder()
                .transactionType(Transaction.TransactionType.DEPOSIT)
                .amount(dto.getAmount())
                .transactionDate(LocalDateTime.now())
                .description(dto.getDescription() != null ? dto.getDescription() : "Deposit")
                .account(account)
                .build();
        return transactionRepository.save(tx);
    }

    @Transactional
    public Transaction withdraw(Account account, TransactionDto dto) {
        if (account.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(dto.getAmount()));
        Transaction tx = Transaction.builder()
                .transactionType(Transaction.TransactionType.WITHDRAWAL)
                .amount(dto.getAmount())
                .transactionDate(LocalDateTime.now())
                .description(dto.getDescription() != null ? dto.getDescription() : "Withdrawal")
                .account(account)
                .build();
        return transactionRepository.save(tx);
    }

    @Transactional
    public void transfer(User user, Long fromAccountId, TransactionDto dto) {
        if (dto.getToAccountId() == null) {
            throw new IllegalArgumentException("Destination account is required");
        }
        Account from = accountService.getAccountById(fromAccountId);
        Account to = accountService.getAccountById(dto.getToAccountId());
        if (!accountService.userOwnsAccount(user, fromAccountId)) {
            throw new IllegalArgumentException("You do not own this account");
        }
        if (from.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        from.setBalance(from.getBalance().subtract(dto.getAmount()));
        to.setBalance(to.getBalance().add(dto.getAmount()));

        String desc = dto.getDescription() != null ? dto.getDescription() : "Transfer to " + to.getAccountNumber();
        transactionRepository.save(Transaction.builder()
                .transactionType(Transaction.TransactionType.TRANSFER_OUT)
                .amount(dto.getAmount())
                .transactionDate(LocalDateTime.now())
                .description(desc)
                .account(from)
                .relatedAccount(to)
                .build());
        transactionRepository.save(Transaction.builder()
                .transactionType(Transaction.TransactionType.TRANSFER_IN)
                .amount(dto.getAmount())
                .transactionDate(LocalDateTime.now())
                .description("Transfer from " + from.getAccountNumber())
                .account(to)
                .relatedAccount(from)
                .build());
    }

    public Page<Transaction> getTransactionsForAccount(Account account, Pageable pageable) {
        return transactionRepository.findByAccountOrderByTransactionDateDesc(account, pageable);
    }
}
