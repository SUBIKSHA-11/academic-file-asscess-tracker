package com.university.securetracker.controller;

import com.university.securetracker.model.Alert;
import com.university.securetracker.repository.AlertRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/alerts")
public class AdminAlertController {

    private final AlertRepository repo;

    public AdminAlertController(AlertRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Alert> all() {
        return repo.findAll();
    }

    @PutMapping("/{id}/resolve")
    public String resolve(@PathVariable Long id) {

        Alert alert = repo.findById(id).orElseThrow();
        alert.setStatus("RESOLVED");
        repo.save(alert);

        return "Resolved";
    }
}
