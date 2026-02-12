package com.university.securetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.securetracker.model.Semester;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
}
