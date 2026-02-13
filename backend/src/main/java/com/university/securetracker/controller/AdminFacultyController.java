package com.university.securetracker.controller;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.securetracker.dto.FacultyRequest;
import com.university.securetracker.model.FacultyDetails;
import com.university.securetracker.service.FacultyService;

@RestController
@RequestMapping("/admin/faculty")
//@RequiredArgsConstructor
public class AdminFacultyController {

    private final FacultyService service;

    public AdminFacultyController(FacultyService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public FacultyDetails create(@RequestBody FacultyRequest req){
        return service.create(req);
    }

    // READ
    @GetMapping
    public List<FacultyDetails> all(){
        return service.getAll();
    }

    // UPDATE
    @PutMapping("/{id}")
    public FacultyDetails update(@PathVariable Long id,
                                 @RequestBody FacultyRequest req){
        return service.update(id, req);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        service.delete(id);
        return "Deleted";
    }

    // FILTER
    @GetMapping("/department/{dept}")
    public List<FacultyDetails> byDept(@PathVariable String dept){
        return service.byDept(dept);
    }
}
