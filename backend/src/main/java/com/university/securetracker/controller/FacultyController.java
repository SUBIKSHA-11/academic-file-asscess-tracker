package com.university.securetracker.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    @PreAuthorize("hasRole('FACULTY')")
    @GetMapping("/dashboard")
    public String facultyDashboard() {
        return "Welcome Faculty Dashboard";
    }
}
