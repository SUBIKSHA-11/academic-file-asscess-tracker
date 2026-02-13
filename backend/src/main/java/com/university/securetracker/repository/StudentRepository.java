package com.university.securetracker.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.securetracker.model.StudentDetails;

public interface StudentRepository extends JpaRepository<StudentDetails, Long> {
    List<StudentDetails> findByDepartment(String department);

    List<StudentDetails> findByYear(Integer year);
}
