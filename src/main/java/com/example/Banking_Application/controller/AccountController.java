package com.example.Banking_Application.controller;

import com.example.Banking_Application.entity.Account;
import com.example.Banking_Application.security.CustomUserDetails;
import com.example.Banking_Application.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public String createAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @RequestParam Account.AccountType accountType,
                               RedirectAttributes redirectAttributes) {
        try {
            accountService.createAccount(userDetails.getUser(), accountType);
            redirectAttributes.addFlashAttribute("success", "Account created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
