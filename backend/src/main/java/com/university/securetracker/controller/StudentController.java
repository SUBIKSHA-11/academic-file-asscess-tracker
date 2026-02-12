package com.university.securetracker.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/dashboard")
    public String studentDashboard() {
        return "Welcome Student Dashboard";
    }
}
