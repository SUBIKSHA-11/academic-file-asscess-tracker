package com.university.securetracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.university.securetracker.dto.FacultyRequest;
import com.university.securetracker.dto.FacultyResponse;
import com.university.securetracker.service.FacultyService;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/admin/faculty")
public class AdminFacultyController {

    private final FacultyService service;

    public AdminFacultyController(FacultyService service) {
        this.service = service;
    }

    // ==========================
    // CREATE
    // ==========================
    @PostMapping
    public FacultyResponse create(@RequestBody FacultyRequest req){
        return service.create(req);
    }

    // ==========================
    // READ ALL
    // ==========================
    @GetMapping
    public List<FacultyResponse> all(){
        return service.getAll();
    }

    // ==========================
    // UPDATE
    // ==========================
    @PutMapping("/{id}")
    public FacultyResponse update(@PathVariable Long id,
                                  @RequestBody FacultyRequest req){
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
    // FILTER BY DEPARTMENT ID
    // ==========================
   /* @GetMapping("/department/{deptId}")
    public List<FacultyResponse> byDept(@PathVariable Long deptId){
        return service.byDept(deptId);
    }*/
    @GetMapping("/department/{deptId}/paged")
public Page<FacultyResponse> getPaged(
        @PathVariable Long deptId,
        @RequestParam(defaultValue = "") String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
) {
    return service.getByDeptWithSearch(deptId, search, page, size);
}

}
