package com.example.demo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.User;
import com.example.demo.services.UserService;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user) {
        user.setId(null);
        user.setUsername(user.getName()); // Set a default username same as name
        user.setPassword("default123"); // Set a default password
        user.setRole("ROLE_USER"); // Set default role
        this.userService.createUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 5;

        Page<User> userPage = this.userService.fetchAllUserWithPagination(page - 1, pageSize);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "User-list";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = this.userService.fetchAllUser().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "User-edit";
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        this.userService.updateUser(id, user);
        return "redirect:/users";
    }
}
