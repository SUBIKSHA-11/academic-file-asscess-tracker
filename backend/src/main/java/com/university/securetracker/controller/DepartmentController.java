package com.university.securetracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.securetracker.model.Department;
import com.university.securetracker.repository.DepartmentRepository;

@RestController
@RequestMapping("/admin/departments")
public class DepartmentController {

    private final DepartmentRepository repo;

    public DepartmentController(DepartmentRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Department> getAll() {
        return repo.findAll();
    }
}
