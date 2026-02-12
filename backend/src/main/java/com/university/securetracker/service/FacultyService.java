package com.university.securetracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.university.securetracker.dto.FacultyRequest;
import com.university.securetracker.model.Role;
import com.university.securetracker.model.User;
import com.university.securetracker.repository.UserRepository;

@Service
public class FacultyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addFaculty(FacultyRequest request) {

        User faculty = new User();

        faculty.setName(request.getName());
        faculty.setEmail(request.getEmail());
        faculty.setPhone(request.getPhone());
        faculty.setDepartment(request.getDepartment());
        faculty.setFacultyCode(request.getFacultyCode());
        faculty.setPassword(passwordEncoder.encode(request.getPassword()));
        faculty.setRole(Role.FACULTY);

        userRepository.save(faculty);

        return "Faculty added successfully";
    }

    public List<User> getAllFaculties() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == Role.FACULTY)
                .toList();
    }

    public User getFacultyById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
    }

    public String updateFaculty(Long id, FacultyRequest request) {

        User faculty = getFacultyById(id);

        faculty.setName(request.getName());
        faculty.setPhone(request.getPhone());
        faculty.setDepartment(request.getDepartment());
        faculty.setFacultyCode(request.getFacultyCode());

        userRepository.save(faculty);

        return "Faculty updated successfully";
    }

    public String deleteFaculty(Long id) {

        User faculty = getFacultyById(id);

        userRepository.delete(faculty);

        return "Faculty deleted successfully";
    }
}
