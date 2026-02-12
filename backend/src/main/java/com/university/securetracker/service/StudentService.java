package com.university.securetracker.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.university.securetracker.dto.StudentRequest;
import com.university.securetracker.model.Role;
import com.university.securetracker.model.User;
import com.university.securetracker.repository.UserRepository;

@Service
public class StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addStudent(StudentRequest request) {

        User student = new User();

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setDepartment(request.getDepartment());
        student.setYear(request.getYear());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRole(Role.STUDENT);

        userRepository.save(student);

        return "Student added successfully";
    }

    public List<User> getAllStudents() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == Role.STUDENT)
                .collect(Collectors.toList());
    }

    public List<User> getStudentsByYear(Integer year) {
        return getAllStudents()
                .stream()
                .filter(student -> year.equals(student.getYear()))
                .collect(Collectors.toList());
    }

    public List<User> getStudentsByYearAndDepartment(Integer year, String department) {
        return getAllStudents()
                .stream()
                .filter(student ->
                        year.equals(student.getYear()) &&
                        department.equalsIgnoreCase(student.getDepartment()))
                .collect(Collectors.toList());
    }

    public String updateStudent(Long id, StudentRequest request) {

        User student = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setName(request.getName());
        student.setPhone(request.getPhone());
        student.setDepartment(request.getDepartment());
        student.setYear(request.getYear());

        userRepository.save(student);

        return "Student updated successfully";
    }

    public String deleteStudent(Long id) {

        User student = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        userRepository.delete(student);

        return "Student deleted successfully";
    }
}
