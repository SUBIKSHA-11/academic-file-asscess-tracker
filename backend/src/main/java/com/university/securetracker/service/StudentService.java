package com.university.securetracker.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.university.securetracker.dto.StudentRequest;
import com.university.securetracker.dto.StudentResponse;
import com.university.securetracker.model.Department;
import com.university.securetracker.model.StudentDetails;
import com.university.securetracker.model.User;
import com.university.securetracker.repository.DepartmentRepository;
import com.university.securetracker.repository.StudentRepository;
import com.university.securetracker.repository.UserRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepo;
    private final UserRepository userRepo;
    private final DepartmentRepository deptRepo;
    private final PasswordEncoder encoder;

    public StudentService(PasswordEncoder encoder,
                          StudentRepository studentRepo,
                          UserRepository userRepo,
                          DepartmentRepository deptRepo) {
        this.encoder = encoder;
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.deptRepo = deptRepo;
    }

    // ==========================
    // CREATE
    // ==========================
    @Transactional
    public StudentResponse create(StudentRequest req) {

        Department dept = deptRepo.findById(req.getDepartmentId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole("STUDENT");
        user.setStatus("ACTIVE");

        user = userRepo.save(user);

        StudentDetails s = new StudentDetails();
        s.setUser(user);
        s.setRegNo(req.getRegNo());
        s.setName(req.getName());
        s.setEmail(req.getEmail());
        s.setPhone(req.getPhone());
        s.setYear(req.getYear());
        s.setDepartment(dept);

        s = studentRepo.save(s);

        return mapToResponse(s);
    }

    // ==========================
    // READ ALL (SORTED)
    // ==========================
    public List<StudentResponse> getAll() {
        return studentRepo
                .findAllByOrderByYearDescNameAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ==========================
    // UPDATE
    // ==========================
    @Transactional
    public StudentResponse update(Long id, StudentRequest req) {

        StudentDetails s = studentRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        Department dept = deptRepo.findById(req.getDepartmentId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        s.setRegNo(req.getRegNo());
        s.setName(req.getName());
        s.setPhone(req.getPhone());
        s.setYear(req.getYear());
        s.setDepartment(dept);

        s = studentRepo.save(s);

        return mapToResponse(s);
    }

    // ==========================
    // DELETE
    // ==========================
    @Transactional
    public void delete(Long id) {

        StudentDetails s = studentRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        studentRepo.delete(s);
        userRepo.delete(s.getUser());
    }

    // ==========================
    // FILTER BY DEPARTMENT
    // ==========================
    public List<StudentResponse> byDept(Long deptId) {
        return studentRepo
                .findByDepartmentIdOrderByYearDescNameAsc(deptId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ==========================
    // YEAR COUNT
    // ==========================
    public Map<Integer, Long> countByYear(Long deptId) {

        return studentRepo
                .findByDepartmentIdOrderByYearDescNameAsc(deptId)
                .stream()
                .collect(Collectors.groupingBy(
                        StudentDetails::getYear,
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    // ==========================
    // PAGINATION + SEARCH
    // ==========================
    public Page<StudentResponse> getByDeptWithSearch(
            Long deptId,
            String search,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<StudentDetails> studentPage;

        if (search != null && !search.isEmpty()) {
            studentPage =
                    studentRepo.findByDepartmentIdAndNameContainingIgnoreCaseOrderByYearDescNameAsc(
                            deptId, search, pageable);
        } else {
            studentPage =
                    studentRepo.findByDepartmentIdOrderByYearDescNameAsc(
                            deptId, pageable);
        }

        return studentPage.map(this::mapToResponse);
    }

    // ==========================
    // MAPPER
    // ==========================
    private StudentResponse mapToResponse(StudentDetails s) {

        return new StudentResponse(
                s.getId(),
                s.getRegNo(),
                s.getName(),
                s.getEmail(),
                s.getPhone(),
                s.getYear(),
                s.getDepartment().getId(),
                s.getDepartment().getName()
        );
    }
}
