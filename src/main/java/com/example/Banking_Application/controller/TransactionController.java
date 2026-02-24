package com.example.Banking_Application.controller;

import com.example.Banking_Application.dto.TransactionDto;
import com.example.Banking_Application.entity.Account;
import com.example.Banking_Application.security.CustomUserDetails;
import com.example.Banking_Application.service.AccountService;
import com.example.Banking_Application.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accounts/{accountId}")
@RequiredArgsConstructor
public class TransactionController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping
    public String accountDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @PathVariable Long accountId,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {
        if (!accountService.userOwnsAccount(userDetails.getUser(), accountId)) {
            return "redirect:/dashboard";
        }
        Account account = accountService.getAccountById(accountId);
        Page<?> transactions = transactionService.getTransactionsForAccount(
                account, PageRequest.of(page, 10));
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        model.addAttribute("transactionDto", new TransactionDto());
        model.addAttribute("userAccounts", accountService.getAccountsByUser(userDetails.getUser()));
        return "account-detail";
    }

    @PostMapping("/deposit")
    public String deposit(@AuthenticationPrincipal CustomUserDetails userDetails,
                          @PathVariable Long accountId,
                          @Valid @ModelAttribute TransactionDto dto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (!accountService.userOwnsAccount(userDetails.getUser(), accountId)) {
            return "redirect:/dashboard";
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transactionDto", result);
            redirectAttributes.addFlashAttribute("transactionDto", dto);
            return "redirect:/accounts/" + accountId;
        }
        try {
            Account account = accountService.getAccountById(accountId);
            transactionService.deposit(account, dto);
            redirectAttributes.addFlashAttribute("success", "Deposit successful");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/accounts/" + accountId;
    }

    @PostMapping("/withdraw")
    public String withdraw(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @PathVariable Long accountId,
                           @Valid @ModelAttribute TransactionDto dto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (!accountService.userOwnsAccount(userDetails.getUser(), accountId)) {
            return "redirect:/dashboard";
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transactionDto", result);
            redirectAttributes.addFlashAttribute("transactionDto", dto);
            return "redirect:/accounts/" + accountId;
        }
        try {
            Account account = accountService.getAccountById(accountId);
            transactionService.withdraw(account, dto);
            redirectAttributes.addFlashAttribute("success", "Withdrawal successful");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/accounts/" + accountId;
    }

    @PostMapping("/transfer")
    public String transfer(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @PathVariable Long accountId,
                           @Valid @ModelAttribute TransactionDto dto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (!accountService.userOwnsAccount(userDetails.getUser(), accountId)) {
            return "redirect:/dashboard";
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transactionDto", result);
            redirectAttributes.addFlashAttribute("transactionDto", dto);
            return "redirect:/accounts/" + accountId;
        }
        try {
            transactionService.transfer(userDetails.getUser(), accountId, dto);
            redirectAttributes.addFlashAttribute("success", "Transfer successful");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/accounts/" + accountId;
    }
}
