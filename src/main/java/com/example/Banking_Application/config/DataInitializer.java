package com.example.Banking_Application.config;

import com.example.Banking_Application.entity.Account;
import com.example.Banking_Application.entity.User;
import com.example.Banking_Application.repository.UserRepository;
import com.example.Banking_Application.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("demo@bank.com").isEmpty()) {
            User demo = User.builder()
                    .fullName("Demo User")
                    .email("demo@bank.com")
                    .password(passwordEncoder.encode("demo123"))
                    .role("ROLE_USER")
                    .build();
            demo = userRepository.save(demo);
            Account savings = Account.builder()
                    .accountNumber("ACC1000000001")
                    .accountType(Account.AccountType.SAVINGS)
                    .balance(new BigDecimal("1000.00"))
                    .user(demo)
                    .build();
            Account checking = Account.builder()
                    .accountNumber("ACC1000000002")
                    .accountType(Account.AccountType.CHECKING)
                    .balance(new BigDecimal("500.00"))
                    .user(demo)
                    .build();
            accountRepository.save(savings);
            accountRepository.save(checking);
        }
    }
}
