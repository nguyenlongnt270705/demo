package com.example.demo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.User;
import com.example.demo.services.UserService;

@Controller
@RequestMapping("/accounts")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AccountController {

    private UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllAccounts(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 5;
        Page<User> userPage = this.userService.fetchAllUserWithPagination(page - 1, pageSize);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "Account-list";
    }

    @PostMapping
    public String createAccount(@ModelAttribute User user) {
        user.setId(null);
        user.setUsername(user.getName());
        user.setPassword("default123");
        // SUPER_ADMIN có thể tạo USER và ADMIN
        if (user.getRole() == null) {
            user.setRole("ROLE_USER");
        }
        this.userService.createUser(user);
        return "redirect:/accounts";
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return "redirect:/accounts";
    }

    @GetMapping("/{id}/edit")
    public String showEditAccountForm(@PathVariable Long id, Model model) {
        User user = this.userService.fetchAllUser().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "Account-edit";
    }

    @PostMapping("/{id}/update")
    public String updateAccount(@PathVariable Long id, @ModelAttribute User user) {
        this.userService.updateUser(id, user);
        return "redirect:/accounts";
    }
}