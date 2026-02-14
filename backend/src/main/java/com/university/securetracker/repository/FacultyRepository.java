package com.university.securetracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.university.securetracker.model.FacultyDetails;

public interface FacultyRepository extends JpaRepository<FacultyDetails, Long> {

    Page<FacultyDetails> findByDepartmentId(Long departmentId, Pageable pageable);

    Page<FacultyDetails> findByDepartmentIdAndNameContainingIgnoreCase(
            Long departmentId,
            String name,
            Pageable pageable
    );
}
