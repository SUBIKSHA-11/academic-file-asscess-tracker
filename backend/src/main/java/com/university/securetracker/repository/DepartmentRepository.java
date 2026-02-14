package com.university.securetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.university.securetracker.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
