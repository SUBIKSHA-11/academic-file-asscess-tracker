package com.university.securetracker.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.securetracker.model.StudentDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface StudentRepository extends JpaRepository<StudentDetails, Long> {
   List<StudentDetails> findAllByOrderByYearDescNameAsc();

List<StudentDetails> findByDepartmentIdOrderByYearDescNameAsc(Long deptId);

Page<StudentDetails> findByDepartmentIdOrderByYearDescNameAsc(
        Long deptId,
        Pageable pageable
);

Page<StudentDetails> findByDepartmentIdAndNameContainingIgnoreCaseOrderByYearDescNameAsc(
        Long deptId,
        String name,
        Pageable pageable
);
}