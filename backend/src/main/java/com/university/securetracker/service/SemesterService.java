package com.university.securetracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.securetracker.model.Department;
import com.university.securetracker.model.Semester;
import com.university.securetracker.repository.DepartmentRepository;
import com.university.securetracker.repository.SemesterRepository;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public Semester addSemester(Long departmentId, Integer semesterNumber) {

        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Semester sem = new Semester();
        sem.setSemesterNumber(semesterNumber);
        sem.setDepartment(dept);

        return semesterRepository.save(sem);
    }

    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    public void deleteSemester(Long id) {
        semesterRepository.deleteById(id);
    }
}
