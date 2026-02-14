package com.university.securetracker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.university.securetracker.dto.StudentRequest;
import com.university.securetracker.dto.StudentResponse;
import com.university.securetracker.service.StudentService;

@RestController
@RequestMapping("/admin/students")
public class AdminStudentController {

    private final StudentService service;

    public AdminStudentController(StudentService service) {
        this.service = service;
    }

    // ==========================
    // CREATE
    // ==========================
    @PostMapping
    public StudentResponse create(@RequestBody StudentRequest req){
        return service.create(req);
    }

    // ==========================
    // READ ALL (Sorted)
    // ==========================
    @GetMapping
    public List<StudentResponse> all(){
        return service.getAll();
    }

    // ==========================
    // UPDATE
    // ==========================
    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id,
                                  @RequestBody StudentRequest req){
        return service.update(id, req);
    }

    // ==========================
    // DELETE
    // ==========================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        service.delete(id);
        return "Deleted Successfully";
    }

    // ==========================
    // FILTER BY DEPARTMENT
    // ==========================
    @GetMapping("/department/{deptId}")
    public List<StudentResponse> byDept(@PathVariable Long deptId){
        return service.byDept(deptId);
    }

    // ==========================
    // YEAR COUNT
    // ==========================
    @GetMapping("/department/{deptId}/year-count")
    public Map<Integer, Long> yearCount(@PathVariable Long deptId){
        return service.countByYear(deptId);
    }

    // ==========================
    // PAGINATION + SEARCH
    // ==========================
    @GetMapping("/department/{deptId}/paged")
    public Page<StudentResponse> getPaged(
            @PathVariable Long deptId,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return service.getByDeptWithSearch(deptId, search, page, size);
    }
}
