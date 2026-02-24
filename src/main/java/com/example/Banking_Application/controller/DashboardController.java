package com.example.Banking_Application.controller;

import com.example.Banking_Application.entity.Account;
import com.example.Banking_Application.security.CustomUserDetails;
import com.example.Banking_Application.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final AccountService accountService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<Account> accounts = accountService.getAccountsByUser(userDetails.getUser());
        model.addAttribute("accounts", accounts);
        model.addAttribute("user", userDetails.getUser());
        return "dashboard";
    }
}
