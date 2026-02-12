package com.university.securetracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.securetracker.dto.FacultyRequest;
import com.university.securetracker.model.User;
import com.university.securetracker.service.FacultyService;

@RestController
@RequestMapping("/admin/faculty")
public class AdminFacultyController {

    @Autowired
    private FacultyService facultyService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String addFaculty(@RequestBody FacultyRequest request) {
        return facultyService.addFaculty(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public User getFaculty(@PathVariable Long id) {
        return facultyService.getFacultyById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public String updateFaculty(
            @PathVariable Long id,
            @RequestBody FacultyRequest request) {
        return facultyService.updateFaculty(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteFaculty(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }
}
