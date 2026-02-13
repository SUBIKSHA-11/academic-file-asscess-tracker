package com.university.securetracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.securetracker.model.User;
import com.university.securetracker.repository.UserRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository repo;

    // ✅ constructor injection
    public AdminController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return repo.findAll();
    }
}
