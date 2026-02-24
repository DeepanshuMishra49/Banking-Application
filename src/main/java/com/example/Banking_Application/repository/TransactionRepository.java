package com.example.Banking_Application.repository;

import com.example.Banking_Application.entity.Account;
import com.example.Banking_Application.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByAccountOrderByTransactionDateDesc(Account account, Pageable pageable);
}
