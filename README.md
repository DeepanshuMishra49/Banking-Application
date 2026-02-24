# SecureBank - Banking Application

A mid-level banking web application built with **Spring Boot 3.3** and **Thymeleaf** for the frontend.

## Features

- **User registration & login** (Spring Security)
- **Multiple account types**: Savings and Checking
- **Transactions**: Deposit, Withdraw, Transfer between your accounts
- **Transaction history** with pagination per account
- **Dashboard** with account overview and balances
- **Responsive UI** with Bootstrap 5

## Tech Stack

- Spring Boot Web, Data JPA, Security, Validation
- Thymeleaf (with Bootstrap 5)
- H2 in-memory database (with optional H2 Console)
- Lombok

## How to Run

```bash
mvn spring-boot:run
```

Then open: **http://localhost:8080**

### Demo login (pre-seeded)

- **Email:** demo@bank.com  
- **Password:** demo123  

You can also register a new user and create accounts from the dashboard.

## H2 Console (optional)

When the app is running, open **http://localhost:8080/h2-console** to inspect the database.

- JDBC URL: `jdbc:h2:mem:bankdb`
- Username: `sa`
- Password: *(leave empty)*

## Project Structure

- `entity/` – User, Account, Transaction
- `repository/` – JPA repositories
- `service/` – UserService, AccountService, TransactionService
- `controller/` – Auth, Dashboard, Account, Transaction
- `security/` – CustomUserDetails, CustomUserDetailsService
- `config/` – SecurityConfig, DataInitializer
- `templates/` – Thymeleaf HTML pages
