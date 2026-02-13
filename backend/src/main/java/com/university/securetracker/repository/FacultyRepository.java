package com.university.securetracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.securetracker.model.FacultyDetails;

public interface FacultyRepository extends JpaRepository<FacultyDetails, Long> {
    
    // ✅ THIS LINE WAS MISSING
    List<FacultyDetails> findByDepartment(String department);
}
