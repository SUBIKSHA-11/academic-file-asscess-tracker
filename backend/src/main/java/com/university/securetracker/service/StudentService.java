package com.university.securetracker.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.university.securetracker.dto.StudentRequest;
import com.university.securetracker.model.StudentDetails;
import com.university.securetracker.model.User;
import com.university.securetracker.repository.StudentRepository;
import com.university.securetracker.repository.UserRepository;

@Service
//@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public StudentService(PasswordEncoder encoder, StudentRepository studentRepo, UserRepository userRepo) {
        this.encoder = encoder;
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
    }

    // ✅ CREATE
    public StudentDetails create(StudentRequest req) {

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole("STUDENT");
        user.setStatus("ACTIVE");

        userRepo.save(user);

        StudentDetails s = new StudentDetails();
        s.setUser(user);
        s.setRegNo(req.getRegNo());
        s.setName(req.getName());
        s.setEmail(req.getEmail());
        s.setPhone(req.getPhone());
        s.setDepartment(req.getDepartment());
        s.setYear(req.getYear());

        return studentRepo.save(s);
    }

    // ✅ READ
    public List<StudentDetails> getAll() {
        return studentRepo.findAll();
    }

    // ✅ UPDATE
    public StudentDetails update(Long id, StudentRequest req) {

        StudentDetails s = studentRepo.findById(id).orElseThrow();

        s.setRegNo(req.getRegNo());
        s.setName(req.getName());
        s.setPhone(req.getPhone());
        s.setDepartment(req.getDepartment());
        s.setYear(req.getYear());

        return studentRepo.save(s);
    }

    // ✅ DELETE
    public void delete(Long id) {

        StudentDetails s = studentRepo.findById(id).orElseThrow();
studentRepo.delete(s);
        userRepo.delete(s.getUser());
        
    }

    // ✅ FILTER by dept
    public List<StudentDetails> byDept(String dept) {
        return studentRepo.findByDepartment(dept);
    }

    // ✅ FILTER by year
    public List<StudentDetails> byYear(Integer year) {
        return studentRepo.findByYear(year);
    }
}
