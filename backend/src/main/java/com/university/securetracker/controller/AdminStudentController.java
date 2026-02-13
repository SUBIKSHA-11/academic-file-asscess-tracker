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

import com.university.securetracker.dto.StudentRequest;
import com.university.securetracker.model.StudentDetails;
import com.university.securetracker.service.StudentService;

@RestController
@RequestMapping("/admin/students")
//@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService service;

    public AdminStudentController(StudentService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public StudentDetails create(@RequestBody StudentRequest req){
        return service.create(req);
    }

    // READ
    @GetMapping
    public List<StudentDetails> all(){
        return service.getAll();
    }

    // UPDATE
    @PutMapping("/{id}")
    public StudentDetails update(@PathVariable Long id,
                                 @RequestBody StudentRequest req){
        return service.update(id, req);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        service.delete(id);
        return "Deleted";
    }

    // FILTER by dept
    @GetMapping("/department/{dept}")
    public List<StudentDetails> byDept(@PathVariable String dept){
        return service.byDept(dept);
    }

    // FILTER by year
    @GetMapping("/year/{year}")
    public List<StudentDetails> byYear(@PathVariable Integer year){
        return service.byYear(year);
    }
}
