package com.university.securetracker.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.securetracker.dto.DashboardStats;
import com.university.securetracker.repository.UserRepository;

@RestController
@RequestMapping("/admin/dashboard")
//@RequiredArgsConstructor
public class AdminDashboardController {

    private final UserRepository userRepo;

    public AdminDashboardController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/stats")
    public DashboardStats getStats() {

        long totalUsers = userRepo.count();

        long faculty = userRepo.findAll()
                .stream()
                .filter(u -> u.getRole().equals("FACULTY"))
                .count();

        long students = userRepo.findAll()
                .stream()
                .filter(u -> u.getRole().equals("STUDENT"))
                .count();

        return new DashboardStats(totalUsers, faculty, students);
    }
}
