package com.university.securetracker.service;


import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.university.securetracker.dto.FacultyRequest;
import com.university.securetracker.model.FacultyDetails;
import com.university.securetracker.model.User;
import com.university.securetracker.repository.FacultyRepository;
import com.university.securetracker.repository.UserRepository;

@Service
//@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public FacultyService(PasswordEncoder encoder, FacultyRepository facultyRepo, UserRepository userRepo) {
        this.encoder = encoder;
        this.facultyRepo = facultyRepo;
        this.userRepo = userRepo;
    }

    // ✅ CREATE
    public FacultyDetails create(FacultyRequest req) {

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole("FACULTY");
        user.setStatus("ACTIVE");

        userRepo.save(user);

        FacultyDetails faculty = new FacultyDetails();
        faculty.setUser(user);
        faculty.setFacultyCode(req.getFacultyCode());
        faculty.setName(req.getName());
        faculty.setEmail(req.getEmail());
        faculty.setPhone(req.getPhone());
        faculty.setDepartment(req.getDepartment());

        return facultyRepo.save(faculty);
    }

    // ✅ READ
    public List<FacultyDetails> getAll() {
        return facultyRepo.findAll();
    }

    // ✅ UPDATE
    public FacultyDetails update(Long id, FacultyRequest req) {

        FacultyDetails f = facultyRepo.findById(id).orElseThrow();

        f.setFacultyCode(req.getFacultyCode());
        f.setName(req.getName());
        f.setPhone(req.getPhone());
        f.setDepartment(req.getDepartment());

        return facultyRepo.save(f);
    }

    // ✅ DELETE
    public void delete(Long id) {

        FacultyDetails f = facultyRepo.findById(id).orElseThrow();
        facultyRepo.delete(f);
        userRepo.delete(f.getUser());
        
    }

    // ✅ FILTER
    public List<FacultyDetails> byDept(String dept) {
        return facultyRepo.findByDepartment(dept);
    }
}

