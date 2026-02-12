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

import com.university.securetracker.dto.StudentRequest;
import com.university.securetracker.model.User;
import com.university.securetracker.service.StudentService;

@RestController
@RequestMapping("/admin/students")
public class AdminStudentController {

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String addStudent(@RequestBody StudentRequest request) {
        return studentService.addStudent(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/year/{year}")
    public List<User> getByYear(@PathVariable Integer year) {
        return studentService.getStudentsByYear(year);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/year/{year}/department/{department}")
    public List<User> getByYearAndDepartment(
            @PathVariable Integer year,
            @PathVariable String department) {

        return studentService.getStudentsByYearAndDepartment(year, department);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public String updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequest request) {

        return studentService.updateStudent(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }
}
