package com.university.securetracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.university.securetracker.model.Department;
import com.university.securetracker.model.Semester;
import com.university.securetracker.model.Subject;
import com.university.securetracker.model.Unit;
import com.university.securetracker.service.DepartmentService;
import com.university.securetracker.service.SemesterService;
import com.university.securetracker.service.SubjectService;
import com.university.securetracker.service.UnitService;

@RestController
@RequestMapping("/admin/academics")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAcademicController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UnitService unitService;

    // Department
    @PostMapping("/department")
    public Department addDepartment(@RequestParam String name) {
        return departmentService.addDepartment(name);
    }

    @GetMapping("/departments")
    public List<Department> getDepartments() {
        return departmentService.getAllDepartments();
    }

    // Semester
    @PostMapping("/semester")
    public Semester addSemester(
            @RequestParam Long departmentId,
            @RequestParam Integer semesterNumber) {

        return semesterService.addSemester(departmentId, semesterNumber);
    }

    @GetMapping("/semesters")
    public List<Semester> getSemesters() {
        return semesterService.getAllSemesters();
    }

    // Subject
    @PostMapping("/subject")
    public Subject addSubject(
            @RequestParam Long semesterId,
            @RequestParam String name,
            @RequestParam String code) {

        return subjectService.addSubject(semesterId, name, code);
    }

    @GetMapping("/subjects")
    public List<Subject> getSubjects() {
        return subjectService.getAllSubjects();
    }

    // Unit
    @PostMapping("/unit")
    public Unit addUnit(
            @RequestParam Long subjectId,
            @RequestParam String unitName) {

        return unitService.addUnit(subjectId, unitName);
    }

    @GetMapping("/units")
    public List<Unit> getUnits() {
        return unitService.getAllUnits();
    }
}
