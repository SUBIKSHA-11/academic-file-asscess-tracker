package com.university.securetracker.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.university.securetracker.dto.FacultyRequest;
import com.university.securetracker.dto.FacultyResponse;
import com.university.securetracker.model.Department;
import com.university.securetracker.model.FacultyDetails;
import com.university.securetracker.model.User;
import com.university.securetracker.repository.DepartmentRepository;
import com.university.securetracker.repository.FacultyRepository;
import com.university.securetracker.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepo;
    private final UserRepository userRepo;
    private final DepartmentRepository deptRepo;
    private final PasswordEncoder encoder;

    public FacultyService(PasswordEncoder encoder,
                          FacultyRepository facultyRepo,
                          UserRepository userRepo,
                          DepartmentRepository deptRepo) {
        this.encoder = encoder;
        this.facultyRepo = facultyRepo;
        this.userRepo = userRepo;
        this.deptRepo = deptRepo;
    }

    // ================= CREATE =================
    @Transactional
public FacultyResponse create(FacultyRequest req) {

    if (userRepo.findByEmail(req.getEmail()).isPresent()) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Email already exists"
        );
    }

    User user = new User();
    user.setEmail(req.getEmail());
    user.setPassword(encoder.encode(req.getPassword()));
    user.setRole("FACULTY");
    user.setStatus("ACTIVE");
    user = userRepo.save(user);

    Department dept = deptRepo.findById(req.getDepartmentId())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Department not found"));

    FacultyDetails faculty = new FacultyDetails();
    faculty.setUser(user);
    faculty.setFacultyCode(req.getFacultyCode());
    faculty.setName(req.getName());
    faculty.setEmail(req.getEmail());
    faculty.setPhone(req.getPhone());
    faculty.setDepartment(dept);

    faculty = facultyRepo.save(faculty);

    return mapToResponse(faculty);
}


    // ================= READ =================
    public List<FacultyResponse> getAll() {
        return facultyRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= UPDATE =================
    public FacultyResponse update(Long id, FacultyRequest req) {
   FacultyDetails f = facultyRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found"));

        Department department = deptRepo.findById(req.getDepartmentId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        f.setFacultyCode(req.getFacultyCode());
        f.setName(req.getName());
        f.setPhone(req.getPhone());
        f.setDepartment(department);

        f = facultyRepo.save(f);

        return mapToResponse(f);
    }

    // ================= DELETE =================
    public void delete(Long id) {

        FacultyDetails f = facultyRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found"));

        facultyRepo.delete(f);
        userRepo.delete(f.getUser());
    }

    // ================= FILTER =================
   /* public List<FacultyResponse> byDept(Long deptId) {

        return facultyRepo.findByDepartment_Id(deptId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }*/

    // ================= MAPPER =================
    private FacultyResponse mapToResponse(FacultyDetails f) {

        return new FacultyResponse(
                f.getId(),
                f.getFacultyCode(),
                f.getName(),
                f.getEmail(),
                f.getPhone(),
                f.getDepartment().getName()
        );
    }
    public Page<FacultyResponse> getByDeptWithSearch(
        Long deptId,
        String search,
        int page,
        int size
) {

    Pageable pageable = PageRequest.of(page, size);

    Page<FacultyDetails> facultyPage;

    if (search != null && !search.isEmpty()) {
        facultyPage = facultyRepo
                .findByDepartmentIdAndNameContainingIgnoreCase(
                        deptId, search, pageable);
    } else {
        facultyPage = facultyRepo
                .findByDepartmentId(deptId, pageable);
    }

    return facultyPage.map(this::mapToResponse);
}

}
